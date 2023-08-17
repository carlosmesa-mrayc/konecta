package com.konecta.infrastructure.driven_adapters.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_entity")
public class ProductEntity {
    @Id
    private Long id;
    private String name;
    private String reference;
    private String category;
    private Integer price;
    private Integer weight;
    private Integer stock;
    private Instant created_date;
}
