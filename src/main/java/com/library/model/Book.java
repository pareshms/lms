package com.library.model;

import jakarta.persistence.*;

/**
 * OOP: ENCAPSULATION — Private fields with getters / setters.
 *                       Setter validation ensures quantity ≥ 0.
 */
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false, unique = true)
    private String isbn;

    private String category;

    private int quantity;

    private int availableQuantity;

    // ── Computed property — not persisted ─────────────────────────

    /** Returns "Available" when copies remain, else "Issued". */
    @Transient
    public String getStatus() {
        return availableQuantity > 0 ? "Available" : "Issued";
    }

    // ── Getters & Setters ────────────────────────────────────────

    public Long   getId()                   { return id; }
    public void   setId(Long id)            { this.id = id; }

    public String getTitle()                { return title; }
    public void   setTitle(String title)    { this.title = (title != null) ? title.trim() : null; }

    public String getAuthor()               { return author; }
    public void   setAuthor(String a)       { this.author = a; }

    public String getIsbn()                 { return isbn; }
    public void   setIsbn(String isbn)      { this.isbn = isbn; }

    public String getCategory()             { return category; }
    public void   setCategory(String c)     { this.category = c; }

    public int  getQuantity()               { return quantity; }
    /** OOP: ENCAPSULATION — validates non-negative quantity */
    public void setQuantity(int quantity) {
        if (quantity < 0) throw new IllegalArgumentException("Quantity cannot be negative");
        this.quantity = quantity;
    }

    public int  getAvailableQuantity()      { return availableQuantity; }
    /** OOP: ENCAPSULATION — validates non-negative available quantity */
    public void setAvailableQuantity(int q) {
        if (q < 0) throw new IllegalArgumentException("Available quantity cannot be negative");
        this.availableQuantity = q;
    }
}
