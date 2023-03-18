package org.zeveon.drones.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.media.StringSchema;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zeveon.drones.dto.MedicationDto;

/**
 * @author Stanislav Vafin
 */
@Configuration
public class OpenApiConfig {

    private static final String MEDICATION_IMAGE_FIELD = "image";

    @Value("${drones.swagger.examples.image}")
    private String imageExample;

    @Bean
    public ModelResolver modelResolver(ObjectMapper objectMapper) {
        return new ModelResolver(objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE));
    }

    @Bean
    public OpenApiCustomizer customize() {
        return openApi -> {
            var components = openApi.getComponents();
            var medicationSchema = components.getSchemas().get(MedicationDto.class.getSimpleName());
            if (medicationSchema != null) {
                var image = medicationSchema.getProperties().get(MEDICATION_IMAGE_FIELD);
                if (image instanceof StringSchema) {
                    ((StringSchema) image).example(imageExample);
                }
            }
        };
    }
}
