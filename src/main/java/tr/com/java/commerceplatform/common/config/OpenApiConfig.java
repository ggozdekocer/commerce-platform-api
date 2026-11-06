package tr.com.java.commerceplatform.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Configures the Swagger / OpenAPI documentation page of the API. */
@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    /**
     * Builds the OpenAPI definition with the API information and JWT bearer authentication.
     *
     * @return the OpenAPI definition shown on the Swagger page
     */
    @Bean
    public OpenAPI commercePlatformOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Commerce Platform API")
                        .description("REST API of an e-commerce platform: authentication, products, "
                                + "categories, orders, addresses and saved cards.")
                        .version("0.0.1"))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components().addSecuritySchemes(SECURITY_SCHEME_NAME,
                        new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}