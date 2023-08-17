package com.konecta.infrastructure.driven_adapters.product;

import com.konecta.domain.model.product.Product;
import com.konecta.domain.model.product.ProductRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Date;
import java.util.Calendar;

@Log
@Service
public class ProductAdapter implements ProductRepository {

    @Autowired
    private ProductDataRepository productDataRepository;

    private Calendar calendar;

    @Override
    public Mono<Product> save(Product product) {
        return productDataRepository.findByReference(product.getReference())
                .flatMap(patientCurrent -> productDataRepository.save(toEntity(patientCurrent.getId(), product)))
                .switchIfEmpty(productDataRepository.save(toEntity(null, product)))
                .flatMap(productEntityCreated -> Mono.just(toModel(productEntityCreated)))
                .onErrorResume(error -> {
                    log.severe(String.format("Error presentado al guardar el producto con la referencia %s ::: %s", product.getReference(), error.getMessage()));
                    return Mono.just(Product.builder().build());
                });
    }
    @Override
    public Mono<Product> findByReference(String reference) {
        return productDataRepository.findByReference(reference)
                .flatMap(productEntity -> Mono.just(toModel(productEntity)))
                .switchIfEmpty(Mono.defer(() -> Mono.just(Product.builder().build())))
                .onErrorResume(error -> {
                    log.severe(String.format("Error presentado al buscar el producto por la referencia %s ::: %s", reference, error.getMessage()));
                    return Mono.just(Product.builder().build());
                });
    }

    @Override
    public Mono<Void> delete(String reference) {
        return productDataRepository.findByReference(reference)
                .flatMap(productEntity -> productDataRepository.delete(productEntity))
                .then()
                .onErrorResume(error -> {
                    log.severe(String.format("Error presentado al eliminar el producto por la referencia %s ::: %s", reference, error.getMessage()));
                    return Mono.empty();
                });
    }

    @Override
    public Flux<Product> findAll() {
        return productDataRepository.findAll()
                .flatMap(items -> Mono.just(toModel(items)))
                .onErrorResume(error -> {
                    log.severe(String.format("Error presentado al devolver todos los productos ::: %s", error.getMessage()));
                    return Mono.just(Product.builder().build());
                });
    }

    @Override
    public Mono<Product> mergeStock(Product product, Integer count) {
        return productDataRepository.findByReference(product.getReference())
                .flatMap(productEntity -> mergeCountStock(count, productEntity))
                .flatMap(productMerged -> productDataRepository.save(productMerged))
                .flatMap(productMerged -> Mono.just(toModel(productMerged))
                        .onErrorResume(error -> {
                            log.severe(String.format("Error presentado al actualizar el stock del producto con referencia %s ::: %s", product.getReference(), error.getMessage()));
                            return Mono.just(Product.builder().build());
                        }));
    }

    @Override
    public Mono<Product> findStockMax() {
        return productDataRepository.findStockMax()
                .flatMap(productEntity -> Mono.just(toModel(productEntity)))
                .switchIfEmpty(Mono.defer(() -> Mono.just(Product.builder().build())))
                .onErrorResume(error -> {
                    log.severe(String.format("Error presentado al mostrar el producto con máximo stock ::: %s", error.getMessage()));
                    return Mono.just(Product.builder().build());
                });
    }

    private Mono<ProductEntity> mergeCountStock(Integer count, ProductEntity productEntity) {
        return Mono.just(productEntity.toBuilder().stock(productEntity.getStock() - count).build());
    }

    private ProductEntity toEntity(Long id, Product product) {
        calendar = Calendar.getInstance();
        return ProductEntity.builder()
                .id(id)
                .reference(product.getReference())
                .category(product.getCategory())
                .stock(product.getStock())
                .created_date(calendar.toInstant())
                .price(product.getPrice())
                .weight(product.getWeight())
                .name(product.getName())
                .build();
    }

    private Product toModel(ProductEntity entity) {
        return Product.builder()
                .reference(entity.getReference())
                .category(entity.getCategory())
                .stock(entity.getStock())
                .createdDate(Date.from(entity.getCreated_date()))
                .price(entity.getPrice())
                .weight(entity.getWeight())
                .name(entity.getName())
                .build();
    }
}
