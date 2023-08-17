package com.konecta.infrastructure.entry_points.product;

import com.konecta.domain.model.product.Product;
import com.konecta.domain.model.product.ProductRepository;
import com.konecta.domain.useCase.product.ProductUseCase;
import com.konecta.infrastructure.entry_points.helpers.ProductDto;
import com.konecta.infrastructure.entry_points.helpers.ResponseDto;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import javax.xml.datatype.DatatypeConfigurationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import org.junit.Before;

public class ProductControllerTest {
    private Product product;
    private ProductDto dto;

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductUseCase productUseCase;

    @Mock
    private ProductRepository productRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        dto = ProductDto.builder()
                .reference("1234567890")
                .name("Producto Arroz")
                .category("Granos")
                .price(100000)
                .weight(500)
                .stock(100)
                .build();

        product = Product.builder()
                .reference("1234567890")
                .name("Producto Arroz")
                .category("Granos")
                .price(100000)
                .weight(500)
                .stock(100).build();
    }

    @Test
    public void productSaveWhenProductRequestIsValid() throws DatatypeConfigurationException {
        when(productUseCase.save(product)).thenReturn(Mono.just(product));
        when(productRepository.save(product)).thenReturn(Mono.just(product));

        final Mono<ResponseEntity<ResponseDto>> responseEntityMono = productController.saveProduct(dto);

        StepVerifier.create(responseEntityMono).assertNext(objectResponseEntity -> {
            assertThat(objectResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        }).verifyComplete();
    }
}
