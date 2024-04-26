package noteflow.preferences.exception;


import noteflow.preferences.model.UserPrefsErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.*;

public class UserPrefsGlobalExceptionHandlerTest {
    private final UserPrefsGlobalExceptionHandler handler = new UserPrefsGlobalExceptionHandler();
    private WebRequest webRequest;

    @BeforeEach
    public void setUp() {
        MockHttpServletRequest servletRequest = new MockHttpServletRequest("GET", "www.corgi.com");
        webRequest = new ServletWebRequest(servletRequest);
    }

    @Test
    public void test_handleInvalidUserPrefsException_returns400() {
        ResponseEntity<Object> response =
                handler.handleInvalidUserPrefsException(new InvalidUserPrefsException(), webRequest);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void test_handleUnableToStoreUserPrefsException_returns500() {
        ResponseEntity<Object> response =
                handler.handleUnableToUserStorePrefsException(new UnableToStoreUserPrefsException(), webRequest);
        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void test_handleUnableToReadUserPrefsException_returns500() {
        ResponseEntity<Object> response =
                handler.handleUnableToReadUserPrefsException(new UnableToReadUserPrefsException(), webRequest);
        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void test_handleExceptionInternal() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_ATOM_XML);
        ResponseEntity<Object> response =
                handler.handleExceptionInternal(
                        new RuntimeException("exception"), null, httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR, webRequest);
        verifyResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "exception");
    }

    public void verifyResponse(ResponseEntity<Object> entity, HttpStatus status, String reason) {
        assertNotNull(entity);
        assertEquals(MediaType.APPLICATION_JSON, entity.getHeaders().getContentType());
        assertTrue(entity.getBody() instanceof UserPrefsErrorResponse);
        UserPrefsErrorResponse errorResponse = (UserPrefsErrorResponse) entity.getBody();
        assertEquals(status.value(), errorResponse.getStatusCode());
        assertEquals(status.getReasonPhrase(), errorResponse.getStatusMessage());
        assertEquals(reason, errorResponse.getReason());
        assertEquals("www.corgi.com", errorResponse.getUri());

    }
}
