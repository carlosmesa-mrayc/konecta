package com.konecta.infrastructure.entry_points.product;

import com.konecta.domain.model.product.Product;
import com.konecta.domain.useCase.product.ProductUseCase;
import com.konecta.infrastructure.entry_points.helpers.ProductDto;
import com.konecta.infrastructure.entry_points.helpers.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Log
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductUseCase productUseCase;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ResponseDto>> saveProduct(@Valid @RequestBody ProductDto dto) {
        return productUseCase.save(Product.builder()
                .reference(dto.getReference())
                .name(dto.getName())
                .price(dto.getPrice())
                .weight(dto.getWeight())
                .category(dto.getCategory())
                .stock(dto.getStock())
                .build())
                .flatMap(productCreated -> null != productCreated.getCreatedDate() ?
                        Mono.just(ResponseEntity.ok().body(ResponseDto.builder()
                                .message("El producto se almacenó correctamente")
                                .object(productCreated).build())) :
                        Mono.just(ResponseEntity.ok().body(ResponseDto.builder()
                                .message("No es posible almacenar el producto")
                                .object(productCreated).build()))
                ).onErrorResume((error) -> {
                    log.severe(String.format("Error presentado al procesar los datos del producto %s ::: %s", dto.getReference(), error.getMessage()));
                    return Mono.just(ResponseEntity.badRequest().body(ResponseDto.builder()
                            .message(error.getMessage()).build()));
                });
    }

    @GetMapping(value="/findByReference/{reference}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ResponseDto>> getProduct(String reference) {
        return productUseCase.findByReference(reference)
                .flatMap(productCurrent -> null != productCurrent.getReference() ?
                        Mono.just(ResponseEntity.ok().body(ResponseDto.builder()
                                .message("Se retorna la información del producto correctamente")
                                .object(productCurrent).build())) :
                        Mono.just(ResponseEntity.ok().body(ResponseDto.builder()
                                .message(String.format("No es posible devolver la información del producto con la referencia %s", reference))
                                .build()))
                );
    }

    @GetMapping(value="/findStockMax", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ResponseDto>> getStockMax() {
        return productUseCase.findStockMax()
                .flatMap(productCurrent -> null != productCurrent.getReference() ?
                        Mono.just(ResponseEntity.ok().body(ResponseDto.builder()
                                .message("Se retorna la información del producto correctamente")
                                .object(productCurrent).build())) :
                        Mono.just(ResponseEntity.ok().body(ResponseDto.builder()
                                .message(String.format("No es posible devolver la información del producto con mayor stock"))
                                .build()))
                );
    }

    @GetMapping(value="/findAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ResponseDto>> getAllProduct() {
        return productUseCase.findAll()
                .collectList()
                .flatMap(items -> null != items && !items.isEmpty() ?
                    Mono.just(ResponseEntity.ok().body(ResponseDto.builder()
                            .message("Se retornan los productos correctamente")
                            .object(items).build())) :
                    Mono.just(ResponseEntity.ok().body(ResponseDto.builder()
                            .message("No hay información para mostrar de los productos")
                            .build()))
                );

    }

    @DeleteMapping(value="/{reference}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ResponseDto>> deleteProduct(String reference) {
        return productUseCase.delete(reference)
                .then(Mono.just(ResponseEntity.ok().body(ResponseDto.builder()
                    .message("El producto fue eliminado correctamente")
                    .build()))
                );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
