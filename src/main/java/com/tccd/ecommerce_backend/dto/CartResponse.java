package com.tccd.ecommerce_backend.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CartResponse
{
    private Long id;
    private Long userId;
    private List<CartItemResponse> items;

}
