package com.example.order.processing;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Order<T extends Product> {
    private final int id;
    private final T product;
    private final int quantity;

    public double getTotalPrice() {
        return product != null ? product.getPrice() * quantity : 0.0;
    }
}
