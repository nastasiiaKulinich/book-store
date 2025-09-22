package com.example.bookstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.example.bookstore.dto.book.BookDto;
import com.example.bookstore.dto.book.BookDtoWithoutCategoryIds;
import com.example.bookstore.dto.book.BookSearchParametersDto;
import com.example.bookstore.dto.book.CreateBookRequestDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.BookMapper;
import com.example.bookstore.model.Book;
import com.example.bookstore.repository.book.BookRepository;
import com.example.bookstore.repository.book.BookSpecificationBuilder;
import com.example.bookstore.service.book.impl.BookServiceImpl;
import java.math.BigDecimal;
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
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    private static final Long EXISTING_ID = 1L;
    private static final Long NON_EXISTING_ID = 100L;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;
    @InjectMocks
    private BookServiceImpl bookService;

    private CreateBookRequestDto createBookRequestDto;
    private CreateBookRequestDto updateBookRequestDto;
    private Book book;
    private Book updatedBook;
    private BookDto bookDto;
    private BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds;
    private BookDto updatedBookDto;
    private BookSearchParametersDto bookSearchParametersDto;

    @BeforeEach
    void setUp() {
        createBookRequestDto = getCreateBookRequestDto();
        updateBookRequestDto = getUpdateRequestDto();
        book = getBook();
        updatedBook = getUpdatedBook();
        bookDto = getBookDto();
        bookDtoWithoutCategoryIds = getBookDtoWithoutCategoryIds();
        updatedBookDto = getUpdatedBookDto();
        bookSearchParametersDto = getBookSearchParametersDto();
    }

    @Test
    @DisplayName("Verify that the save method is working correctly")
    public void save_ValidCreateBookRequestDto_ReturnsBookDto() {
        Mockito.when(bookMapper.toModel(createBookRequestDto)).thenReturn(book);
        Mockito.when(bookRepository.save(book)).thenReturn(book);
        Mockito.when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto expected = bookDto;
        BookDto actual = bookService.save(createBookRequestDto);

        assertThat(actual).isEqualTo(expected);
        verify(bookRepository, times(1)).save(book);
        verify(bookMapper, times(1)).toModel(createBookRequestDto);
        verify(bookMapper, times(1)).toDto(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Verify that the find all method is working correctly")
    public void findAll_ExistingBooks_ReturnsListBookDtoWithoutCategoryIds() {
        Pageable pageable = PageRequest.of(0,10);
        List<Book> books = List.of(book);
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());

        Mockito.when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        Mockito.when(bookMapper.toDtoWithoutCategoryIds(book))
                .thenReturn(bookDtoWithoutCategoryIds);

        List<BookDtoWithoutCategoryIds> expected = List.of(bookDtoWithoutCategoryIds);
        List<BookDtoWithoutCategoryIds> actual = bookService.findAll(pageable);

        assertThat(actual).isEqualTo(expected);
        verify(bookRepository, times(1)).findAll(pageable);
        verify(bookMapper, times(1)).toDtoWithoutCategoryIds(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Verify that the find by id method is working correctly with existing book id")
    public void findById_ExistingBookId_ReturnsBookDto() {
        Mockito.when(bookRepository.findById(EXISTING_ID)).thenReturn(Optional.of(book));
        Mockito.when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto expected = bookDto;
        BookDto actual = bookService.findById(EXISTING_ID);

        assertThat(actual).isEqualTo(expected);
        verify(bookRepository, times(1)).findById(EXISTING_ID);
        verify(bookMapper, times(1)).toDto(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Verify that the find by id method is working correctly with non existing book id")
    public void findById_NonExistingBookId_ThrowsException() {
        Mockito.when(bookRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.findById(NON_EXISTING_ID));

        String expected = "Can't find book by id = " + NON_EXISTING_ID;
        String actual = exception.getMessage();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Verify that the update method is working correctly with existing book id")
    public void update_ExistingBookId_ReturnsBookDto() {
        Mockito.when(bookRepository.findById(EXISTING_ID)).thenReturn(Optional.of(book));
        Mockito.when(bookMapper.toModel(updateBookRequestDto)).thenReturn(updatedBook);
        Mockito.when(bookRepository.save(updatedBook)).thenReturn(updatedBook);
        Mockito.when(bookMapper.toDto(updatedBook)).thenReturn(updatedBookDto);

        BookDto expected = updatedBookDto;
        expected.setTitle("another title");
        BookDto actual = bookService.updateById(EXISTING_ID, updateBookRequestDto);

        assertThat(actual).isEqualTo(expected);
        verify(bookRepository, times(1)).findById(EXISTING_ID);
        verify(bookMapper, times(1)).toModel(updateBookRequestDto);
        verify(bookRepository, times(1)).save(updatedBook);
        verify(bookMapper, times(1)).toDto(updatedBook);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Verify that the update method is working correctly with non existing book id")
    public void update_NonExistingBookId_ThrowsException() {
        Mockito.when(bookRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.updateById(NON_EXISTING_ID, updateBookRequestDto));

        String expected = "Can't find book by id = " + NON_EXISTING_ID;
        String actual = exception.getMessage();

        assertThat(actual).isEqualTo(expected);
        verify(bookRepository, times(1)).findById(NON_EXISTING_ID);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("Verify that the delete by id method is working correctly")
    public void deleteById_ValidBookId_ReturnsNothing() {
        bookService.deleteById(EXISTING_ID);

        verify(bookRepository, times(1)).deleteById(EXISTING_ID);
    }

    @Test
    @DisplayName("Verify that search method is working correctly")
    public void search_ValidBookSearchParametersDto_ReturnsListBookDto() {
        Specification<Book> specification = Mockito.mock(Specification.class);
        Mockito.when(bookSpecificationBuilder.build(bookSearchParametersDto))
                .thenReturn(specification);
        Mockito.when(bookRepository.findAll(specification)).thenReturn(List.of(book));
        Mockito.when(bookMapper.toDto(book)).thenReturn(bookDto);

        List<BookDto> expected = List.of(bookDto);
        List<BookDto> actual = bookService.search(bookSearchParametersDto);
        assertThat(actual).isEqualTo(expected);
        verify(bookSpecificationBuilder, times(1)).build(bookSearchParametersDto);
        verify(bookRepository, times(1)).findAll(specification);
        verify(bookMapper, times(1)).toDto(book);
        verifyNoMoreInteractions(bookRepository, bookMapper, bookSpecificationBuilder);
    }

    @Test
    @DisplayName("Verify that the find all by category id method is working correctly")
    public void findAllByCategoryId_ExistingBookCategoryId_ReturnsListBookDtoWithoutCategoryIds() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> books = List.of(book);
        Mockito.when(bookRepository.findAllByCategoryId(EXISTING_ID, pageable)).thenReturn(books);
        Mockito.when(bookMapper.toDtoWithoutCategoryIds(book))
                .thenReturn(bookDtoWithoutCategoryIds);

        List<BookDtoWithoutCategoryIds> expected = List.of(bookDtoWithoutCategoryIds);
        List<BookDtoWithoutCategoryIds> actual = bookService
                .findAllByCategoryId(EXISTING_ID, pageable);
        assertThat(actual).isEqualTo(expected);
        verify(bookRepository, times(1)).findAllByCategoryId(EXISTING_ID, pageable);
        verify(bookMapper, times(1)).toDtoWithoutCategoryIds(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    private CreateBookRequestDto getCreateBookRequestDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("title");
        requestDto.setAuthor("author");
        requestDto.setIsbn("0-596-42238-9");
        requestDto.setDescription("description");
        requestDto.setPrice(BigDecimal.valueOf(11));
        requestDto.setCoverImage("coverimage");
        return requestDto;
    }

    private CreateBookRequestDto getUpdateRequestDto() {
        CreateBookRequestDto updateRequestDto = new CreateBookRequestDto();
        updateRequestDto.setTitle("another title");
        updateRequestDto.setAuthor("another author");
        updateRequestDto.setIsbn("0-196-32222-9");
        updateRequestDto.setDescription("another description");
        updateRequestDto.setPrice(BigDecimal.valueOf(45));
        updateRequestDto.setCoverImage("another coverimage");
        return updateRequestDto;
    }

    private Book getBook() {
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

    private Book getUpdatedBook() {
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

    private BookDto getBookDto() {
        BookDto bookDto = new BookDto();
        bookDto.setId(EXISTING_ID);
        bookDto.setTitle("title");
        bookDto.setAuthor("author");
        bookDto.setDescription("description");
        bookDto.setPrice(BigDecimal.valueOf(11));
        bookDto.setCoverImage("coverimage");
        return bookDto;
    }

    private BookDto getUpdatedBookDto() {
        BookDto bookDto = new BookDto();
        bookDto.setId(EXISTING_ID);
        bookDto.setTitle("another title");
        bookDto.setAuthor("another author");
        bookDto.setDescription("another description");
        bookDto.setPrice(BigDecimal.valueOf(45));
        bookDto.setCoverImage("another coverimage");
        return bookDto;
    }

    private BookDtoWithoutCategoryIds getBookDtoWithoutCategoryIds() {
        BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds = new BookDtoWithoutCategoryIds();
        bookDtoWithoutCategoryIds.setId(EXISTING_ID);
        bookDtoWithoutCategoryIds.setTitle("another title");
        bookDtoWithoutCategoryIds.setAuthor("another author");
        bookDtoWithoutCategoryIds.setDescription("another description");
        bookDtoWithoutCategoryIds.setPrice(BigDecimal.valueOf(45));
        bookDtoWithoutCategoryIds.setCoverImage("another coverimage");
        return bookDtoWithoutCategoryIds;
    }

    private BookSearchParametersDto getBookSearchParametersDto() {
        return new BookSearchParametersDto();
    }
}
