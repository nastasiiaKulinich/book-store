package com.example.bookstore.repository.book.spec;

import com.example.bookstore.model.Book;
import com.example.bookstore.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class PriceSpecificationLessThanProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return "priceLessThan";
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.lessThan(root.get("price"), params[0]);
    }
}
