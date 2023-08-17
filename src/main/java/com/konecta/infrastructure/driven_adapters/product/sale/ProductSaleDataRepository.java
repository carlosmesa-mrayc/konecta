package com.konecta.infrastructure.driven_adapters.product.sale;

import com.konecta.infrastructure.driven_adapters.product.ProductEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ProductSaleDataRepository extends ReactiveCrudRepository<ProductSaleEntity, Long> {
    @Query("SELECT product_id, sum(count) as count from product_sale_entity group by product_id order by sum(count) desc limit 1")
    Mono<ProductSaleEntity> findSaleMax();
}
