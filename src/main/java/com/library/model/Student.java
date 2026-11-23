package com.library.model;

import jakarta.persistence.*;

/**
 * OOP: INHERITANCE   — Extends abstract User class.
 * OOP: POLYMORPHISM  — Overrides getBorrowLimit() → 3 books
 *                       and calculateFine() → ₹5 / day.
 */
@Entity
@DiscriminatorValue("STUDENT")
public class Student extends User {

    // ── Student-specific fields (ENCAPSULATION — private) ────────

    private String rollNumber;
    private String department;

    @Column(name = "study_year")
    private Integer year;

    // ── OOP: POLYMORPHISM — Student may borrow up to 3 books ─────
    @Override
    public int getBorrowLimit() {
        return 3;
    }

    // ── OOP: POLYMORPHISM — Student fine is ₹5 per overdue day ───
    @Override
    public double calculateFine(long overdueDays) {
        return overdueDays * 5.0;
    }

    // ── Getters & Setters ────────────────────────────────────────

    public String  getRollNumber()                { return rollNumber; }
    public void    setRollNumber(String r)        { this.rollNumber = r; }

    public String  getDepartment()                { return department; }
    public void    setDepartment(String d)        { this.department = d; }

    public Integer getYear()                      { return year; }
    public void    setYear(Integer y)             { this.year = y; }
}
