package com.library;

import com.library.model.*;
import com.library.repository.BookRepository;
import com.library.repository.BorrowRecordRepository;
import com.library.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * OOP: POLYMORPHISM IN ACTION — Seeds initial data using object-oriented
 * instantiation of Student, Faculty, and Librarian subtypes.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepo;
    private final BookRepository bookRepo;
    private final BorrowRecordRepository borrowRepo;

    public DataInitializer(UserRepository userRepo, BookRepository bookRepo, BorrowRecordRepository borrowRepo) {
        this.userRepo = userRepo;
        this.bookRepo = bookRepo;
        this.borrowRepo = borrowRepo;
    }

    @Override
    public void run(String... args) {
        if (userRepo.count() > 0) return; // already seeded

        // 1. Admin / Librarian
        Librarian admin = new Librarian();
        admin.setName("Admin User");
        admin.setEmail("admin@library.com");
        admin.setPhone("9999999999");
        admin.setUsername("admin");
        admin.setPassword("admin123");
        admin.setActive(true);
        admin.setEmployeeId("LIB001");
        userRepo.save(admin);

        // 2. Students (Subclass of User)
        Student s1 = new Student();
        s1.setName("Rahul Sharma");
        s1.setEmail("rahul@college.edu");
        s1.setPhone("9876543210");
        s1.setUsername("rahul");
        s1.setPassword("pass123");
        s1.setActive(true);
        s1.setRollNumber("CS2024001");
        s1.setDepartment("Computer Science");
        s1.setYear(3);
        userRepo.save(s1);

        Student s2 = new Student();
        s2.setName("Priya Patel");
        s2.setEmail("priya@college.edu");
        s2.setPhone("9876543211");
        s2.setUsername("priya");
        s2.setPassword("pass123");
        s2.setActive(true);
        s2.setRollNumber("EC2024002");
        s2.setDepartment("Electronics");
        s2.setYear(2);
        userRepo.save(s2);

        Student s3 = new Student();
        s3.setName("Amit Kumar");
        s3.setEmail("amit@college.edu");
        s3.setPhone("9876543212");
        s3.setUsername("amit");
        s3.setPassword("pass123");
        s3.setActive(true);
        s3.setRollNumber("ME2024003");
        s3.setDepartment("Mechanical");
        s3.setYear(4);
        userRepo.save(s3);

        // 3. Faculty (Subclass of User)
        Faculty f1 = new Faculty();
        f1.setName("Dr. Anjali Desai");
        f1.setEmail("anjali@college.edu");
        f1.setPhone("9876543213");
        f1.setUsername("anjali");
        f1.setPassword("pass123");
        f1.setActive(true);
        f1.setEmployeeId("FAC001");
        f1.setDepartment("Computer Science");
        f1.setDesignation("Professor");
        userRepo.save(f1);

        Faculty f2 = new Faculty();
        f2.setName("Prof. Vikram Singh");
        f2.setEmail("vikram@college.edu");
        f2.setPhone("9876543214");
        f2.setUsername("vikram");
        f2.setPassword("pass123");
        f2.setActive(true);
        f2.setEmployeeId("FAC002");
        f2.setDepartment("Mathematics");
        f2.setDesignation("Associate Professor");
        userRepo.save(f2);

        // 4. Seed 20 Books (OOP: Encapsulation with setters)
        List<Book> books = List.of(
            createBook("Introduction to Algorithms", "Thomas H. Cormen", "978-0262033848", "Algorithms", 5, 5),
            createBook("Java: The Complete Reference", "Herbert Schildt", "978-1260440232", "Programming", 3, 2),
            createBook("Design Patterns", "Erich Gamma et al.", "978-0201633610", "Software Engineering", 4, 3),
            createBook("Database System Concepts", "Abraham Silberschatz", "978-0078022159", "Database", 3, 3),
            createBook("Computer Networks", "Andrew S. Tanenbaum", "978-0132126953", "Networking", 3, 2),
            createBook("Operating System Concepts", "Silberschatz, Galvin", "978-1118063330", "Operating Systems", 4, 4),
            createBook("Clean Code", "Robert C. Martin", "978-0132350884", "Software Engineering", 5, 5),
            createBook("The C Programming Language", "Kernighan & Ritchie", "978-0131103627", "Programming", 3, 3),
            createBook("AI: A Modern Approach", "Stuart Russell", "978-0134610993", "Artificial Intelligence", 4, 4),
            createBook("Discrete Mathematics", "Kenneth H. Rosen", "978-0073383095", "Mathematics", 3, 2),
            createBook("Digital Electronics", "Morris Mano", "978-0132774208", "Electronics", 4, 4),
            createBook("Software Engineering", "Ian Sommerville", "978-0133943030", "Software Engineering", 3, 3),
            createBook("Theory of Computation", "Michael Sipser", "978-1133187790", "Computer Science", 3, 3),
            createBook("Computer Organization", "Carl Hamacher", "978-0073380650", "Computer Architecture", 2, 2),
            createBook("Python Crash Course", "Eric Matthes", "978-1593279288", "Programming", 5, 5),
            createBook("Web Development with Node.js", "David Herron", "978-1838987572", "Web Development", 3, 3),
            createBook("Machine Learning", "Tom M. Mitchell", "978-0070428072", "Artificial Intelligence", 3, 3),
            createBook("Linear Algebra", "Gilbert Strang", "978-0980232776", "Mathematics", 4, 4),
            createBook("The Pragmatic Programmer", "Andy Hunt & Dave Thomas", "978-0135957059", "Software Engineering", 4, 4),
            createBook("Head First Java", "Kathy Sierra", "978-0596009205", "Programming", 5, 5)
        );
        bookRepo.saveAll(books);

        // 5. Seed Initial Borrow Records
        Book b1 = bookRepo.findById(1L).orElse(null);
        Book b2 = bookRepo.findById(2L).orElse(null);
        Book b3 = bookRepo.findById(3L).orElse(null);
        Book b5 = bookRepo.findById(5L).orElse(null);
        Book b10 = bookRepo.findById(10L).orElse(null);

        // Record 1: Returned
        if (b1 != null) {
            BorrowRecord r1 = new BorrowRecord();
            r1.setUser(s1);
            r1.setBook(b1);
            r1.setIssueDate(LocalDate.now().minusDays(30));
            r1.setDueDate(LocalDate.now().minusDays(16));
            r1.setReturnDate(LocalDate.now().minusDays(20));
            r1.setStatus("RETURNED");
            r1.setFine(0);
            borrowRepo.save(r1);
        }

        // Record 2: Overdue (Student Priya - fine calculated via s2.calculateFine())
        if (b2 != null) {
            BorrowRecord r2 = new BorrowRecord();
            r2.setUser(s2);
            r2.setBook(b2);
            r2.setIssueDate(LocalDate.now().minusDays(20));
            r2.setDueDate(LocalDate.now().minusDays(6));
            r2.setStatus("OVERDUE");
            r2.setFine(s2.calculateFine(6)); // OOP: POLYMORPHISM ₹30
            borrowRepo.save(r2);
        }

        // Record 3: Issued
        if (b5 != null) {
            BorrowRecord r3 = new BorrowRecord();
            r3.setUser(s1);
            r3.setBook(b5);
            r3.setIssueDate(LocalDate.now().minusDays(5));
            r3.setDueDate(LocalDate.now().plusDays(9));
            r3.setStatus("ISSUED");
            r3.setFine(0);
            borrowRepo.save(r3);
        }

        // Record 4: Overdue (Faculty Dr. Anjali - fine calculated via f1.calculateFine())
        if (b3 != null) {
            BorrowRecord r4 = new BorrowRecord();
            r4.setUser(f1);
            r4.setBook(b3);
            r4.setIssueDate(LocalDate.now().minusDays(18));
            r4.setDueDate(LocalDate.now().minusDays(4));
            r4.setStatus("OVERDUE");
            r4.setFine(f1.calculateFine(4)); // OOP: POLYMORPHISM ₹12
            borrowRepo.save(r4);
        }

        // Record 5: Issued
        if (b10 != null) {
            BorrowRecord r5 = new BorrowRecord();
            r5.setUser(s3);
            r5.setBook(b10);
            r5.setIssueDate(LocalDate.now().minusDays(2));
            r5.setDueDate(LocalDate.now().plusDays(12));
            r5.setStatus("ISSUED");
            r5.setFine(0);
            borrowRepo.save(r5);
        }
    }

    private Book createBook(String title, String author, String isbn, String cat, int qty, int avail) {
        Book b = new Book();
        b.setTitle(title);
        b.setAuthor(author);
        b.setIsbn(isbn);
        b.setCategory(cat);
        b.setQuantity(qty);
        b.setAvailableQuantity(avail);
        return b;
    }
}
