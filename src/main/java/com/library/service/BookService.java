package com.library.service;

import com.library.model.Book;
import com.library.model.Searchable;
import com.library.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * OOP: ABSTRACTION — Implements the Searchable<Book> interface,
 * providing a concrete keyword search over the book catalogue.
 */
@Service
public class BookService implements Searchable<Book> {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Book not found with id " + id));
    }

    public Book addBook(Book book) {
        book.setAvailableQuantity(book.getQuantity());
        return bookRepository.save(book);
    }

    public Book updateBook(Long id, Book data) {
        Book existing = getBookById(id);
        int diff = data.getQuantity() - existing.getQuantity();
        existing.setTitle(data.getTitle());
        existing.setAuthor(data.getAuthor());
        existing.setIsbn(data.getIsbn());
        existing.setCategory(data.getCategory());
        existing.setQuantity(data.getQuantity());
        existing.setAvailableQuantity(existing.getAvailableQuantity() + diff);
        return bookRepository.save(existing);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    // ── OOP: ABSTRACTION — Searchable interface implementation ───
    @Override
    public List<Book> search(String keyword) {
        if (keyword == null || keyword.isBlank()) return bookRepository.findAll();
        return bookRepository.search(keyword.trim());
    }

    /** Category → count map used by the dashboard chart. */
    public List<Object[]> getCategoryStats() {
        return bookRepository.countByCategory();
    }
}
