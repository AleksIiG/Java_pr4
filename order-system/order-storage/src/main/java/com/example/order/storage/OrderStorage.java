package com.example.order.storage;

import com.example.order.processing.InvalidOrderException;
import com.example.order.processing.Order;
import com.example.order.processing.Product;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

public class OrderStorage {
    private final List<Order<? extends Product>> orders = new CopyOnWriteArrayList<>();

    public <T extends Product> void addOrder(Order<T> order) throws InvalidOrderException {
        if (order == null || order.getProduct() == null) {
            throw new InvalidOrderException("Неможливо зберегти порожнє замовлення або замовлення без товару.");
        }
        orders.add(order);
    }

    public List<Order<? extends Product>> getAllOrders() {
        return List.copyOf(orders);
    }

    public List<Order<? extends Product>> filterOrders(Predicate<Order<? extends Product>> condition) {
        return orders.stream()
                .filter(condition)
                .toList();
    }
}
