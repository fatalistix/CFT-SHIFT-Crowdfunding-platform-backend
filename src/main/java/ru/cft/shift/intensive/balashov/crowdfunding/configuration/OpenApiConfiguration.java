package ru.cft.shift.intensive.balashov.crowdfunding.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.utils.PropertyResolverUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Configuration
public class OpenApiConfiguration {
    private final PropertyResolverUtils propertyResolverUtils;

    @Autowired
    public OpenApiConfiguration(PropertyResolverUtils propertyResolverUtils) {
        this.propertyResolverUtils = propertyResolverUtils;
    }

    @Bean
    public OpenAPI openApi() {
        Logger log = LoggerFactory.getLogger(OpenApiConfiguration.class);
        log.info(message("api.title"));
        return new OpenAPI()
                .info(new Info().title(message("api.title"))
                        .description(message("api.description"))
                        .version(message("api.version"))
                        .license(new License().name(message("api.license.name"))
                                .url("https://cft.ru")));
    }

    private String message(String property) {
        return this.propertyResolverUtils.resolve(property, Locale.getDefault());
    }
}
