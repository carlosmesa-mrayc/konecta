package com.konecta.domain.model.product.sale;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductSaleRepository {
    Mono<ProductSale> save(ProductSale productSale);
    Mono<ProductSale> findSaleMax();
}
