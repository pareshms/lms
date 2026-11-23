package com.library.controller;

import com.library.repository.BookRepository;
import com.library.service.BorrowService;
import com.library.service.BookService;
import com.library.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final BookRepository bookRepo;
    private final BookService    bookService;
    private final BorrowService  borrowService;
    private final UserService    userService;

    public DashboardController(BookRepository bookRepo,
                               BookService bookService,
                               BorrowService borrowService,
                               UserService userService) {
        this.bookRepo      = bookRepo;
        this.bookService   = bookService;
        this.borrowService = borrowService;
        this.userService   = userService;
    }

    @GetMapping("/stats")
    public Map<String, Object> stats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalBooks",   bookRepo.count());
        stats.put("issuedBooks",  borrowService.countIssued());
        stats.put("overdueBooks", borrowService.countOverdue());
        stats.put("totalMembers", userService.countMembers());

        // Category-wise book count for the bar chart
        Map<String, Long> catMap = new LinkedHashMap<>();
        for (Object[] row : bookService.getCategoryStats()) {
            catMap.put((String) row[0], (Long) row[1]);
        }
        stats.put("categoryStats", catMap);

        return stats;
    }
}
