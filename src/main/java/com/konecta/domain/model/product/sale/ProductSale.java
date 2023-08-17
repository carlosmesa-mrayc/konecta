package com.konecta.domain.model.product.sale;

import com.konecta.domain.model.product.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ProductSale {
    private Product product;
    private Integer count;
    private Date createdDate;
    private String status;
}
