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
import noteflow.preferences.PrefsService;
import noteflow.preferences.model.PrefsErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
//TODO: @Timed annotation
class PrefsController {
    private final PrefsService prefsService;

    /**
     * Method to handle posting the preferences
     *
     * @param jsonMap the user's preferences, which get bound as map
     */
    @PostMapping("/")
    @Operation(
            summary = "Update the user's preferences",
            description = "Update the user's preferences by passing in a JSON object containing all the preferences",
            tags = {"postPrefs"})
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Preferences were saved. No content gets returned"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Preferences weren't saved. Invalid user preferences.",
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
            @RequestBody Map<String, Object> jsonMap) {
        //TODO: get user key from request
//        String userKey
        prefsService.storePrefs(jsonMap, "userKey");
    }

    @GetMapping("/")
    @ResponseBody
    public String getPrefs(HttpServletResponse response) {
        // the HttpServletResponse is Spring's way of allowing you alter response
//        String userKey = "userKey";
        String prefs = prefsService.retrievePrefs("userKey");
        if (prefs.isEmpty()) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
        }
        return prefs;
    }
}
