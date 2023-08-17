package com.konecta.infrastructure.driven_adapters.product;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ProductDataRepository extends ReactiveCrudRepository<ProductEntity, Long> {
    Mono<ProductEntity> findByReference(String reference);
    @Query("SELECT * from product_entity where stock = (select max(stock) from product_entity)")
    Mono<ProductEntity> findStockMax();
}
