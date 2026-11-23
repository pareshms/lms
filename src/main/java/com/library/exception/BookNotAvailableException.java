package com.library.exception;

/**
 * OOP: CUSTOM EXCEPTION — Thrown when a patron tries to borrow
 * a book that has no available copies.
 */
public class BookNotAvailableException extends RuntimeException {
    public BookNotAvailableException(String message) {
        super(message);
    }
}
