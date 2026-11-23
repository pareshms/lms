package com.library.controller;

import com.library.model.BorrowRecord;
import com.library.service.BorrowService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final BorrowService borrowService;

    public ReportController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    /** Export all currently-issued records as CSV. */
    @GetMapping("/issued/csv")
    public void issuedCsv(HttpServletResponse resp) throws IOException {
        resp.setContentType("text/csv");
        resp.setHeader("Content-Disposition", "attachment; filename=issued_books.csv");
        List<BorrowRecord> records = borrowService.getActiveRecords();
        writeCsv(resp.getWriter(), records);
    }

    /** Export all overdue records as CSV. */
    @GetMapping("/overdue/csv")
    public void overdueCsv(HttpServletResponse resp) throws IOException {
        resp.setContentType("text/csv");
        resp.setHeader("Content-Disposition", "attachment; filename=overdue_books.csv");
        List<BorrowRecord> records = borrowService.getOverdueRecords();
        writeCsv(resp.getWriter(), records);
    }

    private void writeCsv(PrintWriter w, List<BorrowRecord> records) {
        w.println("ID,Member,Book,ISBN,Issue Date,Due Date,Return Date,Fine (Rs),Status");
        for (BorrowRecord r : records) {
            w.printf("%d,\"%s\",\"%s\",\"%s\",%s,%s,%s,%.2f,%s%n",
                    r.getId(),
                    r.getUser().getName(),
                    r.getBook().getTitle(),
                    r.getBook().getIsbn(),
                    r.getIssueDate(),
                    r.getDueDate(),
                    r.getReturnDate() != null ? r.getReturnDate() : "",
                    r.getFine(),
                    r.getStatus());
        }
        w.flush();
    }
}
