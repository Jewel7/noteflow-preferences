package noteflow.preferences.model;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserPrefsErrorResponse {
    private String uri;
    private String reason;
    private int statusCode;
    private String statusMessage;

}
