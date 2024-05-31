package com.example.bookstore.repository.book;

import com.example.bookstore.dto.BookSearchParametersDto;
import com.example.bookstore.model.Book;
import com.example.bookstore.repository.SpecificationBuilder;
import com.example.bookstore.repository.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParametersDto) {
        Specification<Book> spec = Specification.where(null);
        if (searchParametersDto.getTitles() != null
                && searchParametersDto.getTitles().length > 0) {
            spec = spec.and(bookSpecificationProviderManager.getSpecificationProvider("title")
                    .getSpecification(searchParametersDto.getTitles()));
        }
        if (searchParametersDto.getAuthors() != null
                && searchParametersDto.getAuthors().length > 0) {
            spec = spec.and(bookSpecificationProviderManager.getSpecificationProvider("author")
                    .getSpecification(searchParametersDto.getAuthors()));
        }
        if (searchParametersDto.getFromPrice() != null) {
            String[] arrOfParameter = {String.valueOf(searchParametersDto.getFromPrice())};
            spec = spec.and(bookSpecificationProviderManager
                    .getSpecificationProvider("priceGreaterThan")
                    .getSpecification(arrOfParameter));
        }
        if (searchParametersDto.getToPrice() != null) {
            String[] arrOfParameter = {String.valueOf(searchParametersDto.getToPrice())};
            spec = spec.and(bookSpecificationProviderManager
                    .getSpecificationProvider("priceLessThan")
                    .getSpecification(arrOfParameter));
        }
        if (searchParametersDto.getDescriptions() != null
                && searchParametersDto.getDescriptions().length > 0) {
            spec = spec.and(bookSpecificationProviderManager.getSpecificationProvider("description")
                    .getSpecification(searchParametersDto.getDescriptions()));
        }
        if (searchParametersDto.getCoverImages() != null
                && searchParametersDto.getCoverImages().length > 0) {
            spec = spec.and(bookSpecificationProviderManager.getSpecificationProvider("coverImage")
                    .getSpecification(searchParametersDto.getCoverImages()));
        }
        return spec;
    }
}
