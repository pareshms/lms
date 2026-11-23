package com.library.repository;

import com.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /** Full-text keyword search across title, author, ISBN, and category. */
    @Query("SELECT b FROM Book b WHERE " +
           "LOWER(b.title)    LIKE LOWER(CONCAT('%',:kw,'%')) OR " +
           "LOWER(b.author)   LIKE LOWER(CONCAT('%',:kw,'%')) OR " +
           "LOWER(b.isbn)     LIKE LOWER(CONCAT('%',:kw,'%')) OR " +
           "LOWER(b.category) LIKE LOWER(CONCAT('%',:kw,'%'))")
    List<Book> search(@Param("kw") String keyword);

    /** Category-wise book count for the dashboard chart. */
    @Query("SELECT b.category, COUNT(b) FROM Book b GROUP BY b.category")
    List<Object[]> countByCategory();
}
