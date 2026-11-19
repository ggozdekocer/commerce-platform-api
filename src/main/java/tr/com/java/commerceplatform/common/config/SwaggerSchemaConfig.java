package tr.com.java.commerceplatform.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.jackson.ModelResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Makes the Swagger examples use the same field naming as the real JSON (snake_case). */
@Configuration
public class SwaggerSchemaConfig {

    /**
     * Builds the Swagger model resolver from the application's own ObjectMapper,
     * so it follows the snake_case setting in application.properties.
     */
    @Bean
    public ModelResolver modelResolver(ObjectMapper objectMapper) {
        return new ModelResolver(objectMapper);
    }
}