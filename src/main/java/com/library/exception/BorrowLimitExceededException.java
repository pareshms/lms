package com.library.exception;

/**
 * OOP: CUSTOM EXCEPTION — Thrown when a patron has already reached
 * the maximum number of books they may borrow (polymorphic limit).
 */
public class BorrowLimitExceededException extends RuntimeException {
    public BorrowLimitExceededException(String message) {
        super(message);
    }
}
