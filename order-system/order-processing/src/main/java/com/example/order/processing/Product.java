package com.example.order.processing;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@ToString
public abstract class Product {
    private final String name;
    private final double price;
}
