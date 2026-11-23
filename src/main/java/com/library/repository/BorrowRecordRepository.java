package com.library.repository;

import com.library.model.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {

    List<BorrowRecord> findByStatus(String status);

    List<BorrowRecord> findByStatusIn(List<String> statuses);

    List<BorrowRecord> findByUserId(Long userId);

    /** Records that are still "ISSUED" but past their due date. */
    List<BorrowRecord> findByStatusAndDueDateBefore(String status, LocalDate date);

    long countByStatus(String status);

    long countByUserIdAndStatusIn(Long userId, List<String> statuses);

    /** Most-recent records first. */
    List<BorrowRecord> findTop10ByOrderByIdDesc();
}
