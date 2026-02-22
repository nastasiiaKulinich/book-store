package com.example.bookstore.util;

import com.example.bookstore.dto.book.BookDtoWithoutCategoryIds;
import com.example.bookstore.dto.category.CategoryDto;
import com.example.bookstore.dto.category.CreateCategoryRequestDto;
import java.math.BigDecimal;

public class CategoryControllerTestUtil {
    private static final Long EXISTING_ID_1 = 1L;
    private static final Long EXISTING_ID_2 = 2L;
    private static final Long EXISTING_ID_3 = 3L;

    public static CreateCategoryRequestDto getCreateCategoryRequestDto() {
        CreateCategoryRequestDto createCategoryRequestDto = new CreateCategoryRequestDto();
        createCategoryRequestDto.setName("name3");
        createCategoryRequestDto.setDescription("description3");
        return createCategoryRequestDto;
    }

    public static CategoryDto getCreatedCategoryDto() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(EXISTING_ID_3);
        categoryDto.setName("name3");
        categoryDto.setDescription("description3");
        return categoryDto;
    }

    public static CategoryDto getCategoryDto1() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(EXISTING_ID_1);
        categoryDto.setName("name1");
        categoryDto.setDescription("description1");
        return categoryDto;
    }

    public static CategoryDto getCategoryDto2() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(EXISTING_ID_2);
        categoryDto.setName("name2");
        categoryDto.setDescription("description2");
        return categoryDto;
    }

    public static CreateCategoryRequestDto getUpdateCategoryRequestDto() {
        CreateCategoryRequestDto createCategoryRequestDto = new CreateCategoryRequestDto();
        createCategoryRequestDto.setName("new name1");
        createCategoryRequestDto.setDescription("new description1");
        return createCategoryRequestDto;
    }

    public static CategoryDto getUpdatedCategoryDto() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(EXISTING_ID_1);
        categoryDto.setName("new name1");
        categoryDto.setDescription("new description1");
        return categoryDto;
    }

    public static BookDtoWithoutCategoryIds getBookDtoWithoutCategoryIds() {
        BookDtoWithoutCategoryIds dto = new BookDtoWithoutCategoryIds();
        dto.setId(EXISTING_ID_1);
        dto.setTitle("title1");
        dto.setAuthor("author1");
        dto.setIsbn("0-596-52068-9");
        dto.setPrice(BigDecimal.valueOf(20));
        dto.setDescription("description1");
        dto.setCoverImage("cover image1");
        return dto;
    }
}
