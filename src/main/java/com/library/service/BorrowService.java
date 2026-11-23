package com.library.service;

import com.library.exception.BookNotAvailableException;
import com.library.exception.BorrowLimitExceededException;
import com.library.model.Book;
import com.library.model.BorrowRecord;
import com.library.model.User;
import com.library.repository.BookRepository;
import com.library.repository.BorrowRecordRepository;
import com.library.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BorrowService {

    private final BorrowRecordRepository borrowRepo;
    private final BookRepository bookRepo;
    private final UserRepository userRepo;

    public BorrowService(BorrowRecordRepository borrowRepo,
                         BookRepository bookRepo,
                         UserRepository userRepo) {
        this.borrowRepo = borrowRepo;
        this.bookRepo   = bookRepo;
        this.userRepo   = userRepo;
    }

    // ── Refresh overdue statuses ─────────────────────────────────

    /** Marks ISSUED records whose due date has passed as OVERDUE and
     *  calculates fines using POLYMORPHIC calculateFine(). */
    @Transactional
    public void refreshOverdue() {
        List<BorrowRecord> overdue =
                borrowRepo.findByStatusAndDueDateBefore("ISSUED", LocalDate.now());
        for (BorrowRecord r : overdue) {
            r.setStatus("OVERDUE");
            long days = ChronoUnit.DAYS.between(r.getDueDate(), LocalDate.now());
            // OOP: POLYMORPHISM — fine rate depends on user subtype
            r.setFine(r.getUser().calculateFine(days));
        }
        borrowRepo.saveAll(overdue);
    }

    // ── Issue a book ─────────────────────────────────────────────

    @Transactional
    public BorrowRecord issueBook(Long userId, Long bookId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Member not found"));
        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("Book not found"));

        // Custom exception — no copies left
        if (book.getAvailableQuantity() <= 0) {
            throw new BookNotAvailableException(
                    "'" + book.getTitle() + "' has no available copies");
        }

        // Custom exception — borrow limit (POLYMORPHIC getBorrowLimit())
        long current = borrowRepo.countByUserIdAndStatusIn(userId, List.of("ISSUED", "OVERDUE"));
        if (current >= user.getBorrowLimit()) {
            throw new BorrowLimitExceededException(
                    user.getUserType() + " borrow limit is " + user.getBorrowLimit()
                    + " books — limit reached");
        }

        BorrowRecord record = new BorrowRecord();
        record.setUser(user);
        record.setBook(book);
        record.setIssueDate(LocalDate.now());
        record.setDueDate(LocalDate.now().plusDays(14));
        record.setFine(0);
        record.setStatus("ISSUED");

        book.setAvailableQuantity(book.getAvailableQuantity() - 1);
        bookRepo.save(book);

        return borrowRepo.save(record);
    }

    // ── Return a book ────────────────────────────────────────────

    @Transactional
    public BorrowRecord returnBook(Long recordId) {
        BorrowRecord record = borrowRepo.findById(recordId)
                .orElseThrow(() -> new NoSuchElementException("Borrow record not found"));

        if ("RETURNED".equals(record.getStatus())) {
            throw new IllegalArgumentException("Book already returned");
        }

        record.setReturnDate(LocalDate.now());

        if (LocalDate.now().isAfter(record.getDueDate())) {
            long days = ChronoUnit.DAYS.between(record.getDueDate(), LocalDate.now());
            // OOP: POLYMORPHISM — fine varies by user subtype
            record.setFine(record.getUser().calculateFine(days));
        } else {
            record.setFine(0);
        }

        record.setStatus("RETURNED");

        Book book = record.getBook();
        book.setAvailableQuantity(book.getAvailableQuantity() + 1);
        bookRepo.save(book);

        return borrowRepo.save(record);
    }

    // ── Queries ──────────────────────────────────────────────────

    public List<BorrowRecord> getAllRecords() {
        refreshOverdue();
        return borrowRepo.findAll();
    }

    public List<BorrowRecord> getActiveRecords() {
        refreshOverdue();
        return borrowRepo.findByStatusIn(List.of("ISSUED", "OVERDUE"));
    }

    public List<BorrowRecord> getOverdueRecords() {
        refreshOverdue();
        return borrowRepo.findByStatus("OVERDUE");
    }

    public List<BorrowRecord> getRecentRecords() {
        refreshOverdue();
        return borrowRepo.findTop10ByOrderByIdDesc();
    }

    public long countIssued()  { return borrowRepo.countByStatus("ISSUED"); }
    public long countOverdue() {
        refreshOverdue();
        return borrowRepo.countByStatus("OVERDUE");
    }
}
