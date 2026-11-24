package com.example.order.processing;

import com.example.order.processing.internal.OrderValidator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderProcessor<T extends Product> {
    private final Order<T> order;

    public void process() throws InvalidOrderException {
        OrderValidator.validate(order);
        System.out.println("[" + Thread.currentThread().getName() + "] Оброблено замовлення #"
                + order.getId() + " -> " + order.getProduct().getName()
                + " | Сума: " + String.format("%.2f", order.getTotalPrice()) + " грн");
    }

    public static void processOrder(Order<? extends Product> order) {
        try {
            new OrderProcessor<>(order).process();
        } catch (InvalidOrderException e) {
            System.err.println("Помилка обробки узагальненого замовлення: " + e.getMessage());
        }
    }
}
