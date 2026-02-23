package com.example.bookstore.util;

import com.example.bookstore.dto.category.CategoryDto;
import com.example.bookstore.dto.category.CreateCategoryRequestDto;
import com.example.bookstore.model.Category;

public class CategoryServiceTestUtil {
    private static final Long EXISTING_ID = 1L;

    public static Category getCategory() {
        Category category = new Category();
        category.setId(EXISTING_ID);
        category.setName("name");
        category.setDescription("description");
        return category;
    }

    public static Category getUpdatedCategory() {
        Category updatedCategory = new Category();
        updatedCategory.setId(EXISTING_ID);
        updatedCategory.setName("another name");
        updatedCategory.setDescription("another description");
        return updatedCategory;
    }

    public static CategoryDto getCategoryDto() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(EXISTING_ID);
        categoryDto.setName("name");
        categoryDto.setDescription("description");
        return categoryDto;
    }

    public static CreateCategoryRequestDto getCreateCategoryRequestDto() {
        CreateCategoryRequestDto createCategoryRequestDto = new CreateCategoryRequestDto();
        createCategoryRequestDto.setName("name");
        createCategoryRequestDto.setDescription("description");
        return createCategoryRequestDto;
    }

    public static CreateCategoryRequestDto getUpdateCategoryRequestDto() {
        CreateCategoryRequestDto updateCategoryRequestDto = new CreateCategoryRequestDto();
        updateCategoryRequestDto.setName("another name");
        updateCategoryRequestDto.setDescription("another description");
        return updateCategoryRequestDto;
    }

    public static CategoryDto getUpdatedCategoryDto() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(EXISTING_ID);
        categoryDto.setName("another name");
        categoryDto.setDescription("another description");
        return categoryDto;
    }
}
