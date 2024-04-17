package noteflow.preferences.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noteflow.preferences.model.PrefsErrorResponse;
import noteflow.preferences.service.PrefsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
//TODO: @Timed annotation
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
class PrefsController {


    //TODO: make my own private final SecurityContextAuthenticationFacade securityContextAuthenticationFacade;
    private final PrefsService prefsService;

    /**
     * Method to handle posting the user's preferences
     *
     * @param jsonMap the user's preferences, which get bound as map
     */
    @PostMapping("/")
    @Operation(
            summary = "Update the user's preferences",
            description = "Create/update the user's preferences by passing in a JSON object containing all the preferences",
            tags = {"postPrefs"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "The user's preferences were saved. No content gets returned."),
            @ApiResponse(
                    responseCode = "400",
                    description = "The user's preferences weren't saved because they were invalid.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PrefsErrorResponse.class))),
            @ApiResponse(
                    responseCode = "401",
                    description = "There was a problem with the user's identity",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PrefsErrorResponse.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unable to store preferences to the repo.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PrefsErrorResponse.class)))
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void postPrefs(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "The user's preferences",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"darkMode\": \"true\", \"fontSize\": 3}")))
            //default required=true
            @RequestBody Map<String, Object> jsonMap) {
        //TODO: get user key from request.. String userKey = securityContextAuthenticationFacade.getUserKey();
        prefsService.storePrefs(jsonMap, "userKey");
    }

    /**
     * Method to handle getting the user's preferences
     *
     * @param response the HttpServletResponse object
     * @return the user's preferences
     */

    @Operation(
            summary = "Get the user's preferences",
            description = "Retrieve the user's preferences",
            tags = {"getPrefs"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "The user's preferences were found and returned.",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"darkMode\": \"true\", \"fontSize\": 3}"))),
            @ApiResponse(
                    responseCode = "204",
                    description = "There was no content for the user's preferences.",
                    content = @Content(
                            mediaType = "application/json")),
            @ApiResponse(
                    responseCode = "401",
                    description = "There was a problem with the user's identity",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PrefsErrorResponse.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unable to retrieve preferences from the repo.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PrefsErrorResponse.class))
            )
    })
    @GetMapping("/")
    @ResponseBody
    public String getPrefs(HttpServletResponse response) {
        // the HttpServletResponse is Spring's way of allowing you alter response
//        String userKey = securityContextAuthenticationFacade.getUserKey();
        String prefs = prefsService.retrievePrefs("userKey");
        if (prefs.isEmpty()) {
            response.setStatus(HttpStatus.NO_CONTENT.value());
        }
        return prefs;
    }
}
