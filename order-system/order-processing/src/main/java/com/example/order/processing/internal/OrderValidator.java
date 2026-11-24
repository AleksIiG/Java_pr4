package com.example.order.processing.internal;

import com.example.order.processing.InvalidOrderException;
import com.example.order.processing.Order;
import com.example.order.processing.Product;

public class OrderValidator {
    public static <T extends Product> void validate(Order<T> order) throws InvalidOrderException {
        if (order == null) {
            throw new InvalidOrderException("Замовлення не може бути null.");
        }
        if (order.getProduct() == null) {
            throw new InvalidOrderException("У замовленні #" + order.getId() + " відсутній товар.");
        }
        if (order.getProduct().getPrice() <= 0) {
            throw new InvalidOrderException("Некоректна ціна товару у замовленні #" + order.getId());
        }
        if (order.getQuantity() <= 0) {
            throw new InvalidOrderException("Кількість товару має бути більшою за 0 у замовленні #" + order.getId());
        }
    }
}
