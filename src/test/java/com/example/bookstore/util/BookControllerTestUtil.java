package com.example.bookstore.util;

import com.example.bookstore.dto.book.BookDto;
import com.example.bookstore.dto.book.BookDtoWithoutCategoryIds;
import com.example.bookstore.dto.book.CreateBookRequestDto;
import com.example.bookstore.model.Category;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;

public class BookControllerTestUtil {
    private static final Long EXISTING_ID = 1L;

    public static BookDto getCreatedBookDto() {
        BookDto bookDto = new BookDto();
        bookDto.setId(3L);
        bookDto.setTitle("title3");
        bookDto.setAuthor("author3");
        bookDto.setIsbn("0-576-53018-9");
        bookDto.setPrice(BigDecimal.valueOf(60));
        bookDto.setDescription("description3");
        bookDto.setCoverImage("cover image3");
        bookDto.setCategories(Collections.emptySet());
        return bookDto;
    }

    public static CreateBookRequestDto getCreateBookRequestDto() {
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setTitle("title3");
        createBookRequestDto.setAuthor("author3");
        createBookRequestDto.setIsbn("0-576-53018-9");
        createBookRequestDto.setPrice(BigDecimal.valueOf(60));
        createBookRequestDto.setDescription("description3");
        createBookRequestDto.setCoverImage("cover image3");
        return createBookRequestDto;
    }

    public static BookDtoWithoutCategoryIds getBookDtoWithoutCategoryIds1() {
        BookDtoWithoutCategoryIds dto1 = new BookDtoWithoutCategoryIds();
        dto1.setId(1L);
        dto1.setTitle("title1");
        dto1.setAuthor("author1");
        dto1.setIsbn("0-596-52068-9");
        dto1.setPrice(new BigDecimal("20"));
        dto1.setDescription("description1");
        dto1.setCoverImage("cover image1");
        return dto1;
    }

    public static BookDtoWithoutCategoryIds getBookDtoWithoutCategoryIds2() {
        BookDtoWithoutCategoryIds dto2 = new BookDtoWithoutCategoryIds();
        dto2.setId(2L);
        dto2.setTitle("title2");
        dto2.setAuthor("author2");
        dto2.setIsbn("0-296-53468-9");
        dto2.setPrice(new BigDecimal("40"));
        dto2.setDescription("description2");
        dto2.setCoverImage("cover image2");
        return dto2;
    }

    public static BookDto getExistingBookDto() {
        BookDto bookDto = new BookDto();
        bookDto.setId(EXISTING_ID);
        bookDto.setTitle("title1");
        bookDto.setAuthor("author1");
        bookDto.setIsbn("0-596-52068-9");
        bookDto.setPrice(BigDecimal.valueOf(20));
        bookDto.setDescription("description1");
        bookDto.setCoverImage("cover image1");
        Category category = new Category();
        category.setId(EXISTING_ID);
        category.setName("name1");
        category.setDescription("description1");
        bookDto.setCategories(Set.of(category));
        return bookDto;
    }

    public static BookDto getUpdatedBookDto() {
        BookDto bookDto = new BookDto();
        bookDto.setId(EXISTING_ID);
        bookDto.setTitle("new title1");
        bookDto.setAuthor("new author1");
        bookDto.setIsbn("0-576-51234-9");
        bookDto.setPrice(BigDecimal.valueOf(200));
        bookDto.setDescription("new description1");
        bookDto.setCoverImage("new cover image1");
        bookDto.setCategories(Collections.emptySet());
        return bookDto;
    }

    public static CreateBookRequestDto getUpdateRequestBookDto() {
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setTitle("new title1");
        createBookRequestDto.setAuthor("new author1");
        createBookRequestDto.setIsbn("0-576-51234-9");
        createBookRequestDto.setPrice(BigDecimal.valueOf(200));
        createBookRequestDto.setDescription("new description1");
        createBookRequestDto.setCoverImage("new cover image1");
        return createBookRequestDto;
    }
}
