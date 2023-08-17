package com.konecta.infrastructure.entry_points.helpers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.Date;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    @NotNull(message = "El nombre del producto no puede ser nulo")
    @NotBlank(message = "El nombre del producto no puede ser vacio")
    @Size(min = 1, max = 255, message = "El nombre del producto no cumple con la longitud válida de máximo 255 carácteres")
    private String name;
    @NotNull(message = "La referencia del producto no puede ser nula")
    @NotBlank(message = "La referencia del producto no puede ser vacia")
    @Size(min = 4, max = 20, message = "La referencia del producto no cumple con la longitud válida de 4 a 20 carácteres")
    private String reference;
    @NotNull(message = "La categoria del producto no puede ser nula")
    @NotBlank(message = "La categoria del producto no puede ser vacia")
    @Size(min = 1, max = 50, message = "La categoria del producto no cumple con la longitud válida de 50 carácteres")
    private String category;
    @NotNull(message = "El precio de venta para el producto no puede estar nulo")
    @Min(value = 1, message = "El precio de venta para el producto debe ser como minimo 1 peso")
    private Integer price;
    @NotNull(message = "El peso para el producto no puede estar nulo")
    @Min(value = 1, message = "El peso para el producto debe ser como minimo 1")
    private Integer weight;
    private Integer stock;
    private Date createdDate;
}
