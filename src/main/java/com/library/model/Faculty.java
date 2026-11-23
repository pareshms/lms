package com.library.model;

import jakarta.persistence.*;

/**
 * OOP: INHERITANCE   — Extends abstract User class.
 * OOP: POLYMORPHISM  — Overrides getBorrowLimit() → 5 books
 *                       and calculateFine() → ₹3 / day (faculty privilege).
 */
@Entity
@DiscriminatorValue("FACULTY")
public class Faculty extends User {

    // ── Faculty-specific fields (ENCAPSULATION — private) ────────

    private String employeeId;
    private String department;
    private String designation;

    // ── OOP: POLYMORPHISM — Faculty may borrow up to 5 books ─────
    @Override
    public int getBorrowLimit() {
        return 5;
    }

    // ── OOP: POLYMORPHISM — Faculty fine is ₹3 per overdue day ───
    @Override
    public double calculateFine(long overdueDays) {
        return overdueDays * 3.0;
    }

    // ── Getters & Setters ────────────────────────────────────────

    public String getEmployeeId()                 { return employeeId; }
    public void   setEmployeeId(String e)         { this.employeeId = e; }

    public String getDepartment()                 { return department; }
    public void   setDepartment(String d)         { this.department = d; }

    public String getDesignation()                { return designation; }
    public void   setDesignation(String d)        { this.designation = d; }
}
