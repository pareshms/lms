package com.library.model;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * OOP: ENCAPSULATION — Private fields with controlled access.
 * Tracks the lifecycle of each book loan (issue → return).
 */
@Entity
@Table(name = "borrow_records")
public class BorrowRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(nullable = false)
    private LocalDate issueDate;

    @Column(nullable = false)
    private LocalDate dueDate;

    private LocalDate returnDate;      // null while the book is out

    private double fine;

    @Column(nullable = false)
    private String status;             // ISSUED | RETURNED | OVERDUE

    // ── Getters & Setters ────────────────────────────────────────

    public Long      getId()                       { return id; }
    public void      setId(Long id)                { this.id = id; }

    public User      getUser()                     { return user; }
    public void      setUser(User user)            { this.user = user; }

    public Book      getBook()                     { return book; }
    public void      setBook(Book book)            { this.book = book; }

    public LocalDate getIssueDate()                { return issueDate; }
    public void      setIssueDate(LocalDate d)     { this.issueDate = d; }

    public LocalDate getDueDate()                  { return dueDate; }
    public void      setDueDate(LocalDate d)       { this.dueDate = d; }

    public LocalDate getReturnDate()               { return returnDate; }
    public void      setReturnDate(LocalDate d)    { this.returnDate = d; }

    public double    getFine()                     { return fine; }
    public void      setFine(double fine)          { this.fine = fine; }

    public String    getStatus()                   { return status; }
    public void      setStatus(String status)      { this.status = status; }
}
