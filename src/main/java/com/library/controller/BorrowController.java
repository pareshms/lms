package com.library.controller;

import com.library.model.BorrowRecord;
import com.library.service.BorrowService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/borrows")
public class BorrowController {

    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @GetMapping
    public List<BorrowRecord> all() {
        return borrowService.getAllRecords();
    }

    @GetMapping("/active")
    public List<BorrowRecord> active() {
        return borrowService.getActiveRecords();
    }

    @GetMapping("/overdue")
    public List<BorrowRecord> overdue() {
        return borrowService.getOverdueRecords();
    }

    @PostMapping("/issue")
    public BorrowRecord issue(@RequestBody Map<String, Long> body) {
        return borrowService.issueBook(body.get("userId"), body.get("bookId"));
    }

    @PostMapping("/return/{id}")
    public BorrowRecord returnBook(@PathVariable Long id) {
        return borrowService.returnBook(id);
    }
}
