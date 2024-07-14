package com.example.bookstore.repository.book;

import com.example.bookstore.model.Book;
import java.util.Collection;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    Collection<Object> findAll(Specification<Book> bookSpecification);
}
