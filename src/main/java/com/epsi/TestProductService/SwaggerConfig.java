package com.epsi.TestProductService;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Produits - PayeTonKawa")
                        .version("v0.0.1")
                        .description("Documentation de l'API Produit pour la gestion du catalogue café."));
    }
}
