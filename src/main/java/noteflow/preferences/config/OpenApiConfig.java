package noteflow.preferences.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
//    https://springdoc.org/#how-can-i-set-a-global-header

    @Bean
    public OpenAPI customOpenAPI(@Value("${springdoc.version}") String appVersion, OpenApiProperties openApiProperties) {
        OpenAPI openAPI = new OpenAPI();

        // map the contact props into the Swagger Contact object
        OpenApiProperties.OpenApiContactProps contactProps = openApiProperties.getOpenApi().getContact();
        Contact contact = new Contact()
                .name(contactProps.getName())
                .url(contactProps.getUrl())
                .email(contactProps.getEmail());

        // add all the Swagger properties
        //TODO: set up security scheme
        openAPI.components(new Components()
                        .addSecuritySchemes("basicScheme", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP).scheme("basic"))
                        .addParameters("myHeader1", new Parameter().in("header")
                                .schema(new StringSchema())
                                .name("myHeader1"))
                        .addHeaders("myHeader2", new Header()
                                .description("myHeader2 header")
                                .schema(new StringSchema())))
                .info(new Info()
                        .title(openApiProperties.getOpenApi().getTitle())
                        .version(openApiProperties.getOpenApi().getVersion())
                        .description(openApiProperties.getOpenApi().getDescription())
                        .contact(contact));
        return openAPI;
    }
}
