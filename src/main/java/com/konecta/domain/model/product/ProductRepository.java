package com.konecta.domain.model.product;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository {
    Mono<Product> save(Product product);
    Mono<Product> findByReference(String reference);
    Mono<Void> delete(String reference);
    Flux<Product> findAll();
    Mono<Product> mergeStock(Product product, Integer count);

    Mono<Product> findStockMax();
}
