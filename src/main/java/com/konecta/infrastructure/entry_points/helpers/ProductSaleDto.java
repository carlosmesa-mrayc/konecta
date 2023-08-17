package com.konecta.infrastructure.entry_points.helpers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ProductSaleDto {
    @NotNull(message = "La cantidad a vender para el producto no puede estar nulo")
    @Min(value = 1, message = "La cantidad a vender para el producto debe ser como minimo 1 unidad")
    private Integer count;
    @NotNull(message = "La referencia del producto no puede ser nula")
    @NotBlank(message = "La referencia del producto no puede ser vacia")
    @Size(min = 4, max = 20, message = "La referencia del producto no cumple con la longitud válida de 4 a 20 carácteres")
    private String reference;
}
