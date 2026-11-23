package com.library.model;

/**
 * OOP: ABSTRACTION — Interface that defines a fine-calculation contract.
 * Each User subclass implements this with its own fine rate,
 * demonstrating both abstraction and polymorphism.
 */
public interface FineCalculator {
    double calculateFine(long overdueDays);
}
