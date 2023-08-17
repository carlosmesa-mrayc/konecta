package com.konecta.domain.useCase.product;

import com.konecta.domain.model.product.Product;
import com.konecta.domain.model.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ProductUseCase {
    private final ProductRepository productRepository;

    public Mono<Product> save(Product product) {
        return productRepository.save(product);
    }

    public Mono<Product> findByReference(String reference) {
        return productRepository.findByReference(reference);
    }

    public Mono<Void> delete(String reference) {
        return productRepository.delete(reference);
    }

    public Flux<Product> findAll() {
        return productRepository.findAll();
    }

    public Mono<Product> findStockMax() {
        return productRepository.findStockMax();
    };
}
