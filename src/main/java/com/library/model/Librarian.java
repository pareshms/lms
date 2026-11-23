package com.library.model;

import jakarta.persistence.*;

/**
 * OOP: INHERITANCE   — Extends abstract User class.
 * OOP: POLYMORPHISM  — Overrides getBorrowLimit() → 10
 *                       and calculateFine() → 0 (librarians don't pay fines).
 */
@Entity
@DiscriminatorValue("LIBRARIAN")
public class Librarian extends User {

    // ── Librarian-specific field ─────────────────────────────────

    private String employeeId;

    // ── OOP: POLYMORPHISM ────────────────────────────────────────

    @Override
    public int getBorrowLimit() {
        return 10;
    }

    @Override
    public double calculateFine(long overdueDays) {
        return 0;   // librarians are exempt from fines
    }

    // ── Getters & Setters ────────────────────────────────────────

    public String getEmployeeId()           { return employeeId; }
    public void   setEmployeeId(String e)   { this.employeeId = e; }
}
