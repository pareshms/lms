package com.library.controller;

import com.library.model.BorrowRecord;
import com.library.model.User;
import com.library.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> all() {
        return userService.getAllMembers();
    }

    @GetMapping("/{id}")
    public User one(@PathVariable Long id) {
        return userService.getMemberById(id);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return userService.createMember(user);
    }

    @PutMapping("/{id}")
    public User update(@PathVariable Long id, @RequestBody User user) {
        return userService.updateMember(id, user);
    }

    @PutMapping("/{id}/toggle-active")
    public User toggleActive(@PathVariable Long id) {
        return userService.deactivateMember(id);
    }

    @GetMapping("/{id}/history")
    public List<BorrowRecord> history(@PathVariable Long id) {
        return userService.getBorrowHistory(id);
    }

    @GetMapping("/search")
    public List<User> search(@RequestParam String keyword) {
        return userService.searchMembers(keyword);
    }
}
