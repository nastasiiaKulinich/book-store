package com.example.bookstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.example.bookstore.dto.category.CategoryDto;
import com.example.bookstore.dto.category.CreateCategoryRequestDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.CategoryMapper;
import com.example.bookstore.model.Category;
import com.example.bookstore.repository.category.CategoryRepository;
import com.example.bookstore.service.category.impl.CategoryServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    private static final Long EXISTING_ID = 1L;
    private static final Long NON_EXISTING_ID = 100L;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;
    private Category updatedCategory;
    private CategoryDto categoryDto;
    private CategoryDto updatedCategoryDto;
    private CreateCategoryRequestDto createCategoryRequestDto;
    private CreateCategoryRequestDto updateCategoryRequestDto;

    @BeforeEach
    void setUp() {
        category = getCategory();
        updatedCategory = getUpdatedCategory();
        categoryDto = getCategoryDto();
        updatedCategoryDto = getUpdatedCategoryDto();
        createCategoryRequestDto = getCreateCategoryRequestDto();
        updateCategoryRequestDto = getUpdateCategoryRequestDto();
    }

    @Test
    @DisplayName("Verify that the find all method is working correctly")
    public void findAll_ExistingCategories_ReturnsListCategories() {
        Pageable pageable = PageRequest.of(0,10);
        List<Category> categories = List.of(category);
        Page<Category> page = new PageImpl<>(categories, pageable, categories.size());

        Mockito.when(categoryRepository.findAll(pageable)).thenReturn(page);
        Mockito.when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        List<CategoryDto> expexted = List.of(categoryDto);
        List<CategoryDto> actual = categoryService.findAll(pageable);
        assertThat(actual).isEqualTo(expexted);
        verify(categoryRepository, times(1)).findAll(pageable);
        verify(categoryMapper, times(1)).toDto(category);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName(
            "Verify that the get by id method is working correctly with existing category id"
    )
    void getById_ExistingId_ReturnsCategoryDto() {
        Mockito.when(categoryRepository.findById(EXISTING_ID)).thenReturn(Optional.of(category));
        Mockito.when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto expected = categoryDto;
        CategoryDto actual = categoryService.getById(EXISTING_ID);

        assertThat(actual).isEqualTo(expected);
        verify(categoryRepository, times(1)).findById(EXISTING_ID);
        verify(categoryMapper, times(1)).toDto(category);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName(
            "Verify that the get by id method is working correctly with non existing category id"
    )
    void getById_NonExistingId_ThrowsException() {
        Mockito.when(categoryRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.getById(NON_EXISTING_ID));

        String expected = "Can't find category by id = " + NON_EXISTING_ID;
        String actual = exception.getMessage();
        assertThat(actual).isEqualTo(expected);
        verify(categoryRepository, times(1)).findById(NON_EXISTING_ID);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    @DisplayName(
            "Verify that the save method is working correctly"
    )
    void save_ValidCreateCategoryRequestDto_ReturnsCategoryDto() {
        Mockito.when(categoryMapper.toEntity(createCategoryRequestDto)).thenReturn(category);
        Mockito.when(categoryRepository.save(category)).thenReturn(category);
        Mockito.when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto expected = categoryDto;
        CategoryDto actual = categoryService.save(createCategoryRequestDto);
        assertThat(actual).isEqualTo(expected);
        verify(categoryMapper, times(1)).toEntity(createCategoryRequestDto);
        verify(categoryRepository, times(1)).save(category);
        verify(categoryMapper, times(1)).toDto(category);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName(
            "Verify that the update method is working correctly"
    )
    void update_ExistingCategoryId_ReturnsCategoryDto() {
        Mockito.when(categoryRepository.findById(EXISTING_ID)).thenReturn(Optional.of(category));
        Mockito.when(categoryMapper.toEntity(updateCategoryRequestDto)).thenReturn(updatedCategory);
        Mockito.when(categoryRepository.save(updatedCategory)).thenReturn(updatedCategory);
        Mockito.when(categoryMapper.toDto(updatedCategory)).thenReturn(updatedCategoryDto);

        CategoryDto expected = updatedCategoryDto;
        CategoryDto actual = categoryService.update(EXISTING_ID, updateCategoryRequestDto);
        assertThat(actual).isEqualTo(expected);
        verify(categoryRepository, times(1)).findById(EXISTING_ID);
        verify(categoryMapper, times(1)).toEntity(updateCategoryRequestDto);
        verify(categoryRepository, times(1)).save(updatedCategory);
        verify(categoryMapper, times(1)).toDto(updatedCategory);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName(
            "Verify that the update method is working correctly with non existing book id"
    )
    void update_NonExistingCategoryId_ThrowsException() {
        Mockito.when(categoryRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.update(NON_EXISTING_ID, updateCategoryRequestDto));

        String expected = "Can't find category by id = " + NON_EXISTING_ID;
        String actual = exception.getMessage();
        assertThat(actual).isEqualTo(expected);
        verify(categoryRepository, times(1)).findById(NON_EXISTING_ID);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    @DisplayName("Verify that the delete by id method is working correctly")
    public void deleteById_ValidCategoryId_ReturnsNothing() {
        categoryService.deleteById(EXISTING_ID);
        verify(categoryRepository, times(1)).deleteById(EXISTING_ID);
    }

    private Category getCategory() {
        Category category = new Category();
        category.setId(EXISTING_ID);
        category.setName("name");
        category.setDescription("description");
        return category;
    }

    private Category getUpdatedCategory() {
        Category updatedCategory = new Category();
        updatedCategory.setId(EXISTING_ID);
        updatedCategory.setName("another name");
        updatedCategory.setDescription("another description");
        return updatedCategory;
    }

    private CategoryDto getCategoryDto() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(EXISTING_ID);
        categoryDto.setName("name");
        categoryDto.setDescription("description");
        return categoryDto;
    }

    private CreateCategoryRequestDto getCreateCategoryRequestDto() {
        CreateCategoryRequestDto createCategoryRequestDto = new CreateCategoryRequestDto();
        createCategoryRequestDto.setName("name");
        createCategoryRequestDto.setDescription("description");
        return createCategoryRequestDto;
    }

    private CreateCategoryRequestDto getUpdateCategoryRequestDto() {
        CreateCategoryRequestDto updateCategoryRequestDto = new CreateCategoryRequestDto();
        updateCategoryRequestDto.setName("another name");
        updateCategoryRequestDto.setDescription("another description");
        return updateCategoryRequestDto;
    }

    private CategoryDto getUpdatedCategoryDto() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(EXISTING_ID);
        categoryDto.setName("another name");
        categoryDto.setDescription("another description");
        return categoryDto;
    }
}
