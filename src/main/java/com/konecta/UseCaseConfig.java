package com.konecta;

import com.konecta.domain.model.product.ProductRepository;
import com.konecta.domain.model.product.sale.ProductSaleRepository;
import com.konecta.domain.useCase.product.sale.ProductSaleUseCase;
import com.konecta.domain.useCase.product.ProductUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public ProductUseCase productUseCase(ProductRepository productRepository) {
        return new ProductUseCase(productRepository);
    }

    @Bean
    public ProductSaleUseCase productSaleUseCase(ProductSaleRepository productSaleRepository, ProductUseCase productUseCase) {
        return new ProductSaleUseCase(productSaleRepository, productUseCase);
    }
}
