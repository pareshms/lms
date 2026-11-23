package com.library.controller;

import com.library.model.Book;
import com.library.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<Book> all() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public Book one(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PostMapping
    public Book create(@RequestBody Book book) {
        return bookService.addBook(book);
    }

    @PutMapping("/{id}")
    public Book update(@PathVariable Long id, @RequestBody Book book) {
        return bookService.updateBook(id, book);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok().build();
    }

    /** OOP: ABSTRACTION — delegates to Searchable<Book>.search() */
    @GetMapping("/search")
    public List<Book> search(@RequestParam String keyword) {
        return bookService.search(keyword);
    }
}
