package com.example.bookstore.controller;

import com.example.bookstore.dto.book.BookDtoWithoutCategoryIds;
import com.example.bookstore.dto.category.CategoryDto;
import com.example.bookstore.dto.category.CreateCategoryRequestDto;
import com.example.bookstore.service.book.BookService;
import com.example.bookstore.service.category.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Category management", description = "Endpoints for managing categories")
@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final BookService bookService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new category", description = "Create a new category")
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryDto createCategory(@RequestBody @Valid CreateCategoryRequestDto categoryDto) {
        return categoryService.save(categoryDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all categories", description = "Get a list of all categories")
    @PreAuthorize("hasRole('USER')")
    public List<CategoryDto> getAll(Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a category by id", description = "Get a category by id")
    @PreAuthorize("hasRole('USER')")
    public CategoryDto getCategoryById(@PathVariable @Positive Long id) {
        return categoryService.getById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update a category by id", description = "Update a category by id")
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryDto updateCategory(@PathVariable @Positive Long id,
                                      @RequestBody @Valid CreateCategoryRequestDto categoryDto) {
        return categoryService.update(id, categoryDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a category by id", description = "Delete a category by id")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCategory(@PathVariable @Positive Long id) {
        categoryService.deleteById(id);
    }

    @GetMapping("/{id}/books")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get books by category id",
            description = "Retrieve books by a specific category")
    @PreAuthorize("hasRole('USER')")
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(@PathVariable @Positive Long id,
                                                                Pageable pageable) {
        return bookService.findAllByCategoryId(id, pageable);
    }
}
