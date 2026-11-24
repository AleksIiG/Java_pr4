package com.example.order.threading;

import com.example.order.processing.Order;
import com.example.order.processing.OrderProcessor;
import com.example.order.processing.Product;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class OrderThreadManager {
    private final ExecutorService executor;

    public OrderThreadManager(int threadCount) {
        this.executor = Executors.newFixedThreadPool(threadCount);
    }

    public void processOrdersConcurrently(List<Order<? extends Product>> orders,
                                          Consumer<Order<? extends Product>> handler) {
        // Лямбда-вираз для постановки кожної задачі в пул потоків
        orders.forEach(order -> executor.submit(() -> handler.accept(order)));
    }

    public void processWithDefaultProcessor(List<Order<? extends Product>> orders) {
        // Використання посилання на метод OrderProcessor::processOrder
        processOrdersConcurrently(orders, OrderProcessor::processOrder);
    }

    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
