package com.konecta.infrastructure.driven_adapters.product.sale;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_sale_entity")
public class ProductSaleEntity {
    @Id
    private Long id;
    private Long product_id;
    private Integer count;
    private Instant created_date;
}
