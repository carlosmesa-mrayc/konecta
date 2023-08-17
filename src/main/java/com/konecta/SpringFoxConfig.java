package com.konecta;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.service.ApiInfo;

import java.util.Collections;

@Configuration
public class SpringFoxConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "API de Administración de Productos y Venta",
                "Permite administrar todo lo relacionado con un producto y registrar a su vez las ventas generadas de estos",
                "API V.08.2023",
                "Uso Exclusivo para prueba de concepto Konecta",
                new Contact("Carlos Alberto Mesa Rivillas", "", "carlosalberto.mesarivillas@gmail.com"),
                null, null, Collections.emptyList());
    }
}
