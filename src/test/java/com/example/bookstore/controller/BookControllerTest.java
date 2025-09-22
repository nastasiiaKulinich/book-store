package com.example.bookstore.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.bookstore.dto.book.BookDto;
import com.example.bookstore.dto.book.BookDtoWithoutCategoryIds;
import com.example.bookstore.dto.book.BookSearchParametersDto;
import com.example.bookstore.dto.book.CreateBookRequestDto;
import com.example.bookstore.model.Category;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    protected static MockMvc mockMvc;
    private static final Long EXISTING_ID = 1L;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
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
    @DisplayName(
            "Verify that create book method is working correctly"
    )
    public void createBook_ValidCreateBookRequest_ReturnsBookDto() throws Exception {
        CreateBookRequestDto createBookRequestDto = getCreateBookRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(createBookRequestDto);

        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        BookDto expected = getCreatedBookDto();
        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
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
    @DisplayName(
            "Verify that find all method is working correctly"
    )
    void findAll_ExistingBooks_ReturnsListBookDtoWithoutCategoryIds() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/books")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDtoWithoutCategoryIds dto1 = getBookDtoWithoutCategoryIds1();
        BookDtoWithoutCategoryIds dto2 = getBookDtoWithoutCategoryIds2();
        List<BookDtoWithoutCategoryIds> expected = List.of(dto1, dto2);
        BookDtoWithoutCategoryIds[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), BookDtoWithoutCategoryIds[].class);
        Assertions.assertEquals(2, actual.length);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
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
    @DisplayName(
            "Verify that find book by id method is working correctly"
    )
    void getBookById_ValidBookId_ReturnsBookDto() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/books/1")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto expected = getExistingBookDto();
        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
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
    @DisplayName(
            "Verify that update book by id method is working correctly"
    )
    void updateBook_ExistingBookId_ReturnsBookDto() throws Exception {
        CreateBookRequestDto createBookRequestDto = getUpdateRequestBookDto();
        String jsonRequest = objectMapper.writeValueAsString(createBookRequestDto);

        MvcResult result = mockMvc.perform(
                        put("/books/1")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        BookDto expected = getUpdatedBookDto();
        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
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
    @DisplayName(
            "Verify that delete by id method is working correctly"
    )
    void deleteById_BookId_NoContent() throws Exception {
        mockMvc.perform(
                        delete("/books/1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
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
    @DisplayName(
            "Verify that search method is working correctly"
    )
    void search_ValidBookSearchParametersDto_ListBookDto() throws Exception {
        BookSearchParametersDto bookSearchParametersDto = new BookSearchParametersDto();
        bookSearchParametersDto.setFromPrice(BigDecimal.valueOf(10));
        bookSearchParametersDto.setToPrice(BigDecimal.valueOf(30));

        String jsonRequest = objectMapper.writeValueAsString(bookSearchParametersDto);

        MvcResult result = mockMvc.perform(
                        get("/books/search")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("fromPrice",
                                        String.valueOf(bookSearchParametersDto.getFromPrice()))
                                .param("toPrice",
                                        String.valueOf(bookSearchParametersDto.getToPrice())))
                .andExpect(status().isOk())
                .andReturn();

        List<BookDto> expected = List.of(getExistingBookDto());
        List<BookDto> actual = Arrays.stream(
                        objectMapper.readValue(result.getResponse().getContentAsString(),
                                BookDto[].class))
                .toList();
        Assertions.assertEquals(expected.size(), actual.size());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    private BookDto getCreatedBookDto() {
        BookDto bookDto = new BookDto();
        bookDto.setId(3L);
        bookDto.setTitle("title3");
        bookDto.setAuthor("author3");
        bookDto.setPrice(BigDecimal.valueOf(60));
        bookDto.setDescription("description3");
        bookDto.setCoverImage("cover image3");
        return bookDto;
    }

    private CreateBookRequestDto getCreateBookRequestDto() {
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setTitle("title3");
        createBookRequestDto.setAuthor("author3");
        createBookRequestDto.setIsbn("0-576-53018-9");
        createBookRequestDto.setPrice(BigDecimal.valueOf(60));
        createBookRequestDto.setDescription("description3");
        createBookRequestDto.setCoverImage("cover image3");
        return createBookRequestDto;
    }

    private BookDtoWithoutCategoryIds getBookDtoWithoutCategoryIds1() {
        BookDtoWithoutCategoryIds dto1 = new BookDtoWithoutCategoryIds();
        dto1.setId(1L);
        dto1.setTitle("title1");
        dto1.setAuthor("author1");
        dto1.setIsbn("0-596-52068-9");
        dto1.setPrice(new BigDecimal("20").setScale(2));
        dto1.setDescription("description1");
        dto1.setCoverImage("cover image1");
        return dto1;
    }

    private BookDtoWithoutCategoryIds getBookDtoWithoutCategoryIds2() {
        BookDtoWithoutCategoryIds dto2 = new BookDtoWithoutCategoryIds();
        dto2.setId(2L);
        dto2.setTitle("title2");
        dto2.setAuthor("author2");
        dto2.setIsbn("0-296-53468-9");
        dto2.setPrice(new BigDecimal("40").setScale(2));
        dto2.setDescription("description2");
        dto2.setCoverImage("cover image2");
        return dto2;
    }

    private BookDto getExistingBookDto() {
        BookDto bookDto = new BookDto();
        bookDto.setId(EXISTING_ID);
        bookDto.setTitle("title1");
        bookDto.setAuthor("author1");
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

    private BookDto getUpdatedBookDto() {
        BookDto bookDto = new BookDto();
        bookDto.setId(EXISTING_ID);
        bookDto.setTitle("new title1");
        bookDto.setAuthor("new author1");
        bookDto.setPrice(BigDecimal.valueOf(200));
        bookDto.setDescription("new description1");
        bookDto.setCoverImage("new cover image1");
        Category category = new Category();
        category.setId(EXISTING_ID);
        category.setName("name1");
        category.setDescription("description1");
        bookDto.setCategories(Set.of(category));
        return bookDto;
    }

    private CreateBookRequestDto getUpdateRequestBookDto() {
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
