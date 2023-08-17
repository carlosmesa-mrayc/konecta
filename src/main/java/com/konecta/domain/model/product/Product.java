package com.konecta.domain.model.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private String name;
    private String reference;
    private String category;
    private Integer price;
    private Integer weight;
    private Integer stock;
    private Date createdDate;
}
