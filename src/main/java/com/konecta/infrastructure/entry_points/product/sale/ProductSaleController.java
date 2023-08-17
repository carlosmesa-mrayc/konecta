package com.konecta.infrastructure.entry_points.product.sale;

import com.konecta.domain.model.product.sale.ProductSale;
import com.konecta.domain.useCase.product.sale.ProductSaleUseCase;
import com.konecta.infrastructure.entry_points.helpers.ProductSaleDto;
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
@RequestMapping("/productSale")
@RequiredArgsConstructor
public class ProductSaleController {
    private final ProductSaleUseCase productSaleUseCase;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ResponseDto>> saveProduct(@Valid @RequestBody ProductSaleDto dto) {
        return productSaleUseCase.save(dto.getReference(), dto.getCount())
                .flatMap(productCreated -> productCreated.getStatus().equals("ValidSale") ?
                        Mono.just(ResponseEntity.ok().body(ResponseDto.builder()
                                .message("La venta se almacenó correctamente")
                                .object(productCreated).build())) :
                        Mono.just(ResponseEntity.ok().body(ResponseDto.builder()
                                .message("No es posible almacenar la venta del producto")
                                .object(productCreated.toBuilder().status("NotSale")).build()))
                ).onErrorResume((error) -> {
                    log.severe(String.format("Error presentado al procesar los datos de la venta del producto %s ::: %s", dto.getReference(), error.getMessage()));
                    return Mono.just(ResponseEntity.badRequest().body(ResponseDto.builder()
                            .message(error.getMessage()).build()));
                });
    }

    @GetMapping(value="/findSaleMax", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ResponseDto>> getSaleMax() {
        return productSaleUseCase.findSaleMax()
                .flatMap(productSaleCurrent -> null != productSaleCurrent.getProduct() ?
                        Mono.just(ResponseEntity.ok().body(ResponseDto.builder()
                                .message("Se retorna la información del producto más vendido correctamente")
                                .object(productSaleCurrent).build())) :
                        Mono.just(ResponseEntity.ok().body(ResponseDto.builder()
                                .message(String.format("No es posible devolver la información del producto más vendido"))
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
