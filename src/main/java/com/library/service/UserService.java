package com.library.service;

import com.library.model.*;
import com.library.repository.BorrowRecordRepository;
import com.library.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BorrowRecordRepository borrowRecordRepository;

    public UserService(UserRepository userRepository,
                       BorrowRecordRepository borrowRecordRepository) {
        this.userRepository = userRepository;
        this.borrowRecordRepository = borrowRecordRepository;
    }

    /** All members (excludes LIBRARIAN accounts). */
    public List<User> getAllMembers() {
        return userRepository.findByUserTypeNot("LIBRARIAN");
    }

    public User getMemberById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Member not found with id " + id));
    }

    public User createMember(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username '" + user.getUsername() + "' already exists");
        }
        user.setActive(true);
        return userRepository.save(user);
    }

    public User updateMember(Long id, User data) {
        User existing = getMemberById(id);

        existing.setName(data.getName());
        existing.setEmail(data.getEmail());
        existing.setPhone(data.getPhone());

        if (data.getPassword() != null && !data.getPassword().isEmpty()) {
            existing.setPassword(data.getPassword());
        }

        // OOP: POLYMORPHISM — update type-specific fields via downcasting
        if (existing instanceof Student s && data instanceof Student ds) {
            s.setRollNumber(ds.getRollNumber());
            s.setDepartment(ds.getDepartment());
            s.setYear(ds.getYear());
        } else if (existing instanceof Faculty f && data instanceof Faculty df) {
            f.setEmployeeId(df.getEmployeeId());
            f.setDepartment(df.getDepartment());
            f.setDesignation(df.getDesignation());
        }

        return userRepository.save(existing);
    }

    public User deactivateMember(Long id) {
        User user = getMemberById(id);
        user.setActive(!user.isActive());
        return userRepository.save(user);
    }

    /** Borrowing history for a specific member. */
    public List<BorrowRecord> getBorrowHistory(Long userId) {
        return borrowRecordRepository.findByUserId(userId);
    }

    public long countMembers() {
        return userRepository.countByUserTypeNot("LIBRARIAN");
    }

    public List<User> searchMembers(String keyword) {
        return userRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword, keyword);
    }
}
