package com.tccd.ecommerce_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class ProductResponse
{
    private Long id;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private Long categoryId;
    private String categoryName;

}
