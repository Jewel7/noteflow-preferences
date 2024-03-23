package noteflow.preferences.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.annotations.NotNull;

@Data
@Configuration
@ConfigurationProperties(prefix = "noteflow")
public class OpenApiProperties {


    @NotNull
    private OpenApiProps openApi;

    @Data
    public static class OpenApiProps {
        @NotNull
        OpenApiContactProps contact;
        @NotNull
        String title;
        @NotNull
        String description;
        @NotNull
        String version;
        @NotNull
        String contactName;
        @NotNull
        String contactUrl;
        @NotNull
        String contactEmail;
    }

    @Data
    public static class OpenApiContactProps {
        @NotNull
        String name;
        @NotNull
        String url;
        @NotNull
        String email;
    }
}
