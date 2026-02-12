package org.pmt.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
  @Bean
  public OpenAPI pmtOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("PMT API")
            .description("Project Management Tool API")
            .version("v1"));
  }
}
