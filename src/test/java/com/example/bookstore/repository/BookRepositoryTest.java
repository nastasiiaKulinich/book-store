package com.example.bookstore.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.bookstore.model.Book;
import com.example.bookstore.repository.book.BookRepository;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {
    private static final Long EXISTING_CATEGORY_ID = 1L;
    private static final Long NON_EXISTING_CATEGORY_ID = 100L;

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Verify that the find all by category id method is working correctly")
    @Sql(scripts = {
            "classpath:database/fill-books-table.sql",
            "classpath:database/fill-categories-table.sql",
            "classpath:database/fill-books-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/delete-from-books-categories-table.sql",
            "classpath:database/delete-from-books-table.sql",
            "classpath:database/delete-from-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByCategoryId_ExistingCategoryId_ReturnsListCategories() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> actual = bookRepository.findAllByCategoryId(EXISTING_CATEGORY_ID, pageable);
        assertEquals(1, actual.size());
        assertEquals("title1", actual.get(0).getTitle());
    }

    @Test
    @DisplayName("Verify that the find all by category id method is working correctly")
    @Sql(scripts = {
            "classpath:database/fill-books-table.sql",
            "classpath:database/fill-categories-table.sql",
            "classpath:database/fill-books-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/delete-from-books-categories-table.sql",
            "classpath:database/delete-from-books-table.sql",
            "classpath:database/delete-from-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByCategoryId_NonExistingId_ReturnsEmptyList() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> actual = bookRepository.findAllByCategoryId(NON_EXISTING_CATEGORY_ID, pageable);
        assertEquals(Collections.emptyList(), actual);
    }
}
