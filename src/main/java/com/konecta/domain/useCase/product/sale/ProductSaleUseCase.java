package com.konecta.domain.useCase.product.sale;

import com.konecta.domain.model.product.sale.ProductSale;
import com.konecta.domain.model.product.sale.ProductSaleRepository;
import com.konecta.domain.useCase.product.ProductUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ProductSaleUseCase {
    private final ProductSaleRepository productSaleRepository;
    private final ProductUseCase productUseCase;

    public Mono<ProductSale> save(String reference, Integer count) {
        return null != reference ?
                productUseCase.findByReference(reference)
                        .flatMap(productSearch ->
                                null != productSearch.getReference() ?
                                        productSearch.getStock() > 0 ?
                                                productSaleRepository.save(ProductSale.builder()
                                                    .product(productSearch)
                                                    .count(count)
                                                    .build())
                                                        .flatMap(productSale -> {
                                                            return productUseCase.save(productSearch.toBuilder().stock(productSearch.getStock() - count).build())
                                                                    .flatMap(productMerged ->
                                                                            Mono.just(ProductSale.builder()
                                                                                    .product(productMerged)
                                                                                    .count(productSale.getCount())
                                                                                    .createdDate(productSale.getCreatedDate())
                                                                                    .status("ValidSale").build()));
                                                        }) :
                                        Mono.just(ProductSale.builder().status("NotStock").build()) :
                                Mono.just(ProductSale.builder().status("NotSearch").build()))
        : Mono.just(ProductSale.builder().build());
    };
    public Mono<ProductSale> findSaleMax() {
        return productSaleRepository.findSaleMax();
    };
}
