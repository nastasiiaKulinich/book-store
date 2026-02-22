package com.example.bookstore.util;

import com.example.bookstore.dto.book.BookDto;
import com.example.bookstore.dto.book.BookDtoWithoutCategoryIds;
import com.example.bookstore.dto.book.BookSearchParametersDto;
import com.example.bookstore.dto.book.CreateBookRequestDto;
import com.example.bookstore.model.Book;
import java.math.BigDecimal;

public class BookServiceTestUtil {
    private static final Long EXISTING_ID = 1L;

    public static CreateBookRequestDto getCreateBookRequestDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("title");
        requestDto.setAuthor("author");
        requestDto.setIsbn("0-596-42238-9");
        requestDto.setDescription("description");
        requestDto.setPrice(BigDecimal.valueOf(11));
        requestDto.setCoverImage("coverimage");
        return requestDto;
    }

    public static CreateBookRequestDto getUpdateRequestDto() {
        CreateBookRequestDto updateRequestDto = new CreateBookRequestDto();
        updateRequestDto.setTitle("another title");
        updateRequestDto.setAuthor("another author");
        updateRequestDto.setIsbn("0-196-32222-9");
        updateRequestDto.setDescription("another description");
        updateRequestDto.setPrice(BigDecimal.valueOf(45));
        updateRequestDto.setCoverImage("another coverimage");
        return updateRequestDto;
    }

    public static Book getBook() {
        Book book = new Book();
        book.setId(EXISTING_ID);
        book.setTitle("title");
        book.setAuthor("author");
        book.setIsbn("0-596-42238-9");
        book.setDescription("description");
        book.setPrice(BigDecimal.valueOf(11));
        book.setCoverImage("coverimage");
        return book;
    }

    public static Book getUpdatedBook() {
        Book updated = new Book();
        updated.setId(EXISTING_ID);
        updated.setTitle("another title");
        updated.setAuthor("another author");
        updated.setIsbn("0-196-32222-9");
        updated.setDescription("another description");
        updated.setPrice(BigDecimal.valueOf(45));
        updated.setCoverImage("another coverimage");
        return updated;
    }

    public static BookDto getBookDto() {
        BookDto bookDto = new BookDto();
        bookDto.setId(EXISTING_ID);
        bookDto.setTitle("title");
        bookDto.setAuthor("author");
        bookDto.setDescription("description");
        bookDto.setPrice(BigDecimal.valueOf(11));
        bookDto.setCoverImage("coverimage");
        return bookDto;
    }

    public static BookDto getUpdatedBookDto() {
        BookDto bookDto = new BookDto();
        bookDto.setId(EXISTING_ID);
        bookDto.setTitle("another title");
        bookDto.setAuthor("another author");
        bookDto.setDescription("another description");
        bookDto.setPrice(BigDecimal.valueOf(45));
        bookDto.setCoverImage("another coverimage");
        return bookDto;
    }

    public static BookDtoWithoutCategoryIds getBookDtoWithoutCategoryIds() {
        BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds = new BookDtoWithoutCategoryIds();
        bookDtoWithoutCategoryIds.setId(EXISTING_ID);
        bookDtoWithoutCategoryIds.setTitle("another title");
        bookDtoWithoutCategoryIds.setAuthor("another author");
        bookDtoWithoutCategoryIds.setDescription("another description");
        bookDtoWithoutCategoryIds.setPrice(BigDecimal.valueOf(45));
        bookDtoWithoutCategoryIds.setCoverImage("another coverimage");
        return bookDtoWithoutCategoryIds;
    }

    public static BookSearchParametersDto getBookSearchParametersDto() {
        return new BookSearchParametersDto();
    }
}
