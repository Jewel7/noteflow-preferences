package noteflow.preferences.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("noteflow.preferences.s3")
public class S3Properties {
    public String bucketName;
}
