package com.konecta.infrastructure.driven_adapters.product.sale;

import com.konecta.domain.model.product.Product;
import com.konecta.domain.model.product.sale.ProductSale;
import com.konecta.domain.model.product.sale.ProductSaleRepository;
import com.konecta.infrastructure.driven_adapters.product.ProductDataRepository;
import com.konecta.infrastructure.driven_adapters.product.ProductEntity;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Date;
import java.util.Calendar;

@Log
@Service
public class ProductSaleAdapter implements ProductSaleRepository {
    @Autowired
    private ProductSaleDataRepository productSaleDataRepository;
    @Autowired
    private ProductDataRepository productDataRepository;
    private Calendar calendar;

    @Override
    public Mono<ProductSale> save(ProductSale productSale) {
        return productDataRepository.findByReference(productSale.getProduct().getReference())
                .flatMap(productEntityCurrent -> null != productEntityCurrent.getId() ?
                        productSaleDataRepository.save(toEntity(productEntityCurrent.getId(), productSale))
                                .flatMap(productSaleEntityCreated -> null != productSaleEntityCreated.getId() ?
                                        Mono.just(toModel(productSaleEntityCreated)) : Mono.just(ProductSale.builder().build()))
                                .onErrorResume(error -> {
                                    log.severe(String.format("Error presentado al guardar la venta del producto con la referencia %s ::: %s", productSale.getProduct().getReference(), error.getMessage()));
                                    return Mono.just(ProductSale.builder().build());
                                }) : Mono.just(ProductSale.builder().build()));
    }
    @Override
    public Flux<ProductSale> findAll() {
        return productSaleDataRepository.findAll()
                .flatMap(items -> Mono.just(toModel(items)))
                .onErrorResume(error -> {
                    log.severe(String.format("Error presentado al devolver todas las ventas de productos ::: %s", error.getMessage()));
                    return Mono.just(ProductSale.builder().build());
                });
    }

    @Override
    public Mono<ProductSale> findSaleMax() {
        return productSaleDataRepository.findSaleMax()
                .flatMap(productSaleEntity ->
                        productDataRepository.findById(productSaleEntity.getProduct_id())
                            .flatMap(productEntity ->
                                    Mono.just(toModelComplete(productSaleEntity, productEntity)))
                )
                .switchIfEmpty(Mono.defer(() -> Mono.just(ProductSale.builder().build())))
                .onErrorResume(error -> {
                    log.severe(String.format("Error presentado al mostrar el producto más vendido ::: %s", error.getMessage()));
                    return Mono.just(ProductSale.builder().build());
                });
    }

    private ProductSaleEntity toEntity(Long productID, ProductSale productSale) {
        calendar = Calendar.getInstance();
        return ProductSaleEntity.builder()
                .product_id(productID)
                .count(productSale.getCount())
                .created_date(calendar.toInstant())
                .build();
    }

    private ProductSale toModel(ProductSaleEntity entity) {
        return null != entity && null != entity.getId() ? ProductSale.builder()
                .createdDate(Date.from(entity.getCreated_date()))
                .count(entity.getCount())
                .product(Product.builder().build()).build() : ProductSale.builder().build();
    }

    private ProductSale toModelComplete(ProductSaleEntity entity, ProductEntity productEntity) {
        return null != entity && null != entity.getProduct_id() ? ProductSale.builder()
                .count(entity.getCount())
                .product(Product.builder()
                        .price(productEntity.getPrice())
                        .stock(productEntity.getStock())
                        .weight(productEntity.getWeight())
                        .category(productEntity.getCategory())
                        .createdDate(Date.from(productEntity.getCreated_date()))
                        .name(productEntity.getName())
                        .reference(productEntity.getReference())
                        .build()).build() : ProductSale.builder().build();
    }
}
