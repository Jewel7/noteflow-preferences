package noteflow.preferences.exception;

import lombok.extern.slf4j.Slf4j;
import noteflow.preferences.model.PrefsErrorResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
//TODO: what is @Order for?
public class PrefsGlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(UnableToReadPrefsException.class)
    public ResponseEntity<Object> handleUnableToReadPrefsException(UnableToReadPrefsException ex, final WebRequest request) {
        return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(UnableToStorePrefsException.class)
    public ResponseEntity<Object> handleUnableToStorePrefsException(UnableToStorePrefsException ex, final WebRequest request) {
        return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(InvalidUserPrefsException.class)
    public ResponseEntity<Object> handleInvalidUserPrefsException(InvalidUserPrefsException ex, final WebRequest request) {
        return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    /**
     * Every exception handled calls this method.
     * By overriding the handleExceptionInternal method from the ResponseEntityExceptionHandler, we can set a
     * consistent and customized error response to return to the client.
     *
     * @param ex         The {@link Exception}
     * @param body       The response body
     * @param headers    Any {@link HttpHeaders}
     * @param statusCode The {@link HttpStatusCode}
     * @param request    The {@link WebRequest}
     * @return A {@link ResponseEntity} with the error response
     */
    @Override
    public ResponseEntity<Object> handleExceptionInternal(
            Exception ex,
            @Nullable Object body,
            HttpHeaders headers,
            HttpStatusCode statusCode,
            WebRequest request) {
        //create the parameters needed for the response entity
        PrefsErrorResponse prefsErrorResponse = createErrorResponse(statusCode, ((ServletWebRequest) request).getRequest().getRequestURI(), ex);
        HttpHeaders newHeaders = new HttpHeaders();
        newHeaders.addAll(headers);
        newHeaders.setContentType(MediaType.APPLICATION_JSON);
        log.info("Exception handled:\n%s".formatted(prefsErrorResponse));
        return createResponseEntity(prefsErrorResponse, newHeaders, statusCode, request);
    }


    /**
     * Creates the error response body for the exception that was thrown
     *
     * @param statusCode The {@link HttpStatusCode} of the response
     * @param requestURI The URI of the request
     * @param ex         The {@link Exception} that was thrown
     * @return An error response that has the thrown exception's message, status code, and status message
     */
    private PrefsErrorResponse createErrorResponse(HttpStatusCode statusCode, String requestURI, Exception ex) {
        HttpStatus httpStatus = HttpStatus.resolve(statusCode.value());
        return PrefsErrorResponse.builder()
                .uri(requestURI)
                .reason(ex.getMessage())
                .statusCode(statusCode.value())
                .statusMessage(httpStatus != null ? httpStatus.getReasonPhrase() : "unknown")
                .build();
    }


}
