package com.library.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;

/**
 * OOP: INHERITANCE   — Abstract base class for Student, Faculty, and Librarian.
 * OOP: ENCAPSULATION — Private fields exposed only through getters / setters
 *                       with validation logic.
 * OOP: ABSTRACTION   — Implements the FineCalculator interface; each subclass
 *                       must supply its own calculateFine() and getBorrowLimit().
 */
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)          // ← single-table strategy
@DiscriminatorColumn(name = "user_type")                       // ← discriminator column
@JsonTypeInfo(
    use      = JsonTypeInfo.Id.NAME,
    include  = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "userType"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Student.class,   name = "STUDENT"),
    @JsonSubTypes.Type(value = Faculty.class,   name = "FACULTY"),
    @JsonSubTypes.Type(value = Librarian.class, name = "LIBRARIAN")
})
public abstract class User implements FineCalculator {

    // ── OOP: ENCAPSULATION — all fields are private ──────────────

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    private String phone;

    @Column(nullable = false, unique = true)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)   // never sent in responses
    @Column(nullable = false)
    private String password;

    private boolean active = true;

    /** Read-only mirror of the discriminator column so Jackson can serialise it. */
    @Column(name = "user_type", insertable = false, updatable = false)
    private String userType;

    // ── OOP: POLYMORPHISM — subclasses override this ─────────────
    public abstract int getBorrowLimit();

    // ── Getters & Setters (with validation) ──────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    /** OOP: ENCAPSULATION — setter trims whitespace */
    public void setName(String name) {
        this.name = (name != null) ? name.trim() : null;
    }

    public String getEmail() { return email; }
    /** OOP: ENCAPSULATION — setter trims whitespace */
    public void setEmail(String email) {
        this.email = (email != null) ? email.trim().toLowerCase() : null;
    }

    public String getPhone()            { return phone; }
    public void   setPhone(String phone){ this.phone = phone; }

    public String getUsername()              { return username; }
    public void   setUsername(String username){ this.username = username; }

    public String getPassword()              { return password; }
    public void   setPassword(String password){ this.password = password; }

    public boolean isActive()           { return active; }
    public void    setActive(boolean a) { this.active = a; }

    public String getUserType()              { return userType; }
    public void   setUserType(String t)      { this.userType = t; }
}
