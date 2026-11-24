package com.example.order.app;

import com.example.order.processing.*;
import com.example.order.storage.OrderStorage;
import com.example.order.threading.OrderThreadManager;
import com.github.javafaker.Faker;

import java.util.List;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        Faker faker = new Faker(Locale.ENGLISH);
        OrderStorage storage = new OrderStorage();
        String[] sizes = {"S", "M", "L", "XL"};

        try {
            // Створення об'єктів Electronics та Clothing через патерн Builder і наповнення JavaFaker
            for (int i = 1; i <= 3; i++) {
                Electronics electronics = Electronics.builder()
                        .name(faker.commerce().productName() + " (Electronics)")
                        .price(Double.parseDouble(faker.commerce().price(500.0, 25000.0).replace(",", ".")))
                        .warrantyMonths(faker.number().numberBetween(6, 36))
                        .build();

                storage.addOrder(Order.<Electronics>builder()
                        .id(i)
                        .product(electronics)
                        .quantity(faker.number().numberBetween(1, 3))
                        .build());
            }

            for (int i = 4; i <= 6; i++) {
                Clothing clothing = Clothing.builder()
                        .name(faker.commerce().productName() + " (Clothing)")
                        .price(Double.parseDouble(faker.commerce().price(300.0, 5000.0).replace(",", ".")))
                        .size(sizes[faker.random().nextInt(sizes.length)])
                        .material(faker.commerce().material())
                        .build();

                storage.addOrder(Order.<Clothing>builder()
                        .id(i)
                        .product(clothing)
                        .quantity(faker.number().numberBetween(1, 5))
                        .build());
            }

            // Демонстрація обробки виключення для узагальненого замовлення з некоректними даними
            Order<Electronics> invalidOrder = Order.<Electronics>builder()
                    .id(99)
                    .product(Electronics.builder().name("Broken Gadget").price(-100.0).warrantyMonths(0).build())
                    .quantity(1)
                    .build();
            storage.addOrder(invalidOrder);

        } catch (InvalidOrderException e) {
            System.err.println("Виняток при додаванні замовлення: " + e.getMessage());
        }

        // Фільтрація замовлень за типом товару за допомогою лямбда-виразу
        List<Order<? extends Product>> electronicsOrders = storage.filterOrders(
                order -> order.getProduct() instanceof Electronics
        );

        System.out.println("\n--- Фільтрація: лише електроніка (" + electronicsOrders.size() + " шт.) ---");
        electronicsOrders.forEach(System.out::println);

        // Багатопотокова обробка всіх замовлень із використанням посилання на метод
        System.out.println("\n--- Багатопотокова обробка всіх замовлень ---");
        OrderThreadManager threadManager = new OrderThreadManager(3);
        threadManager.processWithDefaultProcessor(storage.getAllOrders());
        threadManager.shutdown();
    }
}