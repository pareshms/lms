package com.library.model;

import java.util.List;

/**
 * OOP: ABSTRACTION — Interface that defines a search contract.
 * Any service that supports keyword search implements this interface.
 */
public interface Searchable<T> {
    List<T> search(String keyword);
}
