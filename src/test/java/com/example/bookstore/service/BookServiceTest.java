package com.example.bookstore.service;

import static com.example.bookstore.util.BookServiceTestUtil.getBook;
import static com.example.bookstore.util.BookServiceTestUtil.getBookDto;
import static com.example.bookstore.util.BookServiceTestUtil.getBookDtoWithoutCategoryIds;
import static com.example.bookstore.util.BookServiceTestUtil.getBookSearchParametersDto;
import static com.example.bookstore.util.BookServiceTestUtil.getCreateBookRequestDto;
import static com.example.bookstore.util.BookServiceTestUtil.getUpdateRequestDto;
import static com.example.bookstore.util.BookServiceTestUtil.getUpdatedBook;
import static com.example.bookstore.util.BookServiceTestUtil.getUpdatedBookDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

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
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
    private BookDto updatedBookDto;
    private BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds;
    private BookSearchParametersDto bookSearchParametersDto;

    @BeforeEach
    void setUp() {
        createBookRequestDto = getCreateBookRequestDto();
        updateBookRequestDto = getUpdateRequestDto();
        book = getBook();
        updatedBook = getUpdatedBook();
        bookDto = getBookDto();
        updatedBookDto = getUpdatedBookDto();
        bookDtoWithoutCategoryIds = getBookDtoWithoutCategoryIds();
        bookSearchParametersDto = getBookSearchParametersDto();
    }

    @Test
    @DisplayName("Verify that the save method is working correctly")
    public void save_ValidCreateBookRequestDto_ReturnsBookDto() {
        when(bookMapper.toModel(createBookRequestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto expected = bookDto;
        BookDto actual = bookService.save(createBookRequestDto);

        assertThat(actual).isEqualTo(expected);
        verify(bookRepository).save(book);
        verify(bookMapper).toModel(createBookRequestDto);
        verify(bookMapper).toDto(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Verify that the find all method is working correctly")
    public void findAll_ExistingBooks_ReturnsListBookDtoWithoutCategoryIds() {
        Pageable pageable = PageRequest.of(0,10);
        List<Book> books = List.of(book);
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDtoWithoutCategoryIds(book))
                .thenReturn(bookDtoWithoutCategoryIds);

        List<BookDtoWithoutCategoryIds> expected = List.of(bookDtoWithoutCategoryIds);
        List<BookDtoWithoutCategoryIds> actual = bookService.findAll(pageable);

        assertThat(actual).isEqualTo(expected);
        verify(bookRepository).findAll(pageable);
        verify(bookMapper).toDtoWithoutCategoryIds(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Verify that the find by id method is working correctly with existing book id")
    public void findById_ExistingBookId_ReturnsBookDto() {
        when(bookRepository.findById(EXISTING_ID)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto expected = bookDto;
        BookDto actual = bookService.findById(EXISTING_ID);

        assertThat(actual).isEqualTo(expected);
        verify(bookRepository).findById(EXISTING_ID);
        verify(bookMapper).toDto(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Verify that the find by id method is working correctly with non existing book id")
    public void findById_NonExistingBookId_ThrowsException() {
        when(bookRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.findById(NON_EXISTING_ID));

        String expected = "Can't find book by id = " + NON_EXISTING_ID;
        String actual = exception.getMessage();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Verify that the update method is working correctly with existing book id")
    public void update_ExistingBookId_ReturnsBookDto() {
        when(bookRepository.findById(EXISTING_ID)).thenReturn(Optional.of(book));
        when(bookMapper.toModel(updateBookRequestDto)).thenReturn(updatedBook);
        when(bookRepository.save(updatedBook)).thenReturn(updatedBook);
        when(bookMapper.toDto(updatedBook)).thenReturn(updatedBookDto);

        BookDto expected = updatedBookDto;
        expected.setTitle("another title");
        BookDto actual = bookService.updateById(EXISTING_ID, updateBookRequestDto);

        assertThat(actual).isEqualTo(expected);
        verify(bookRepository).findById(EXISTING_ID);
        verify(bookMapper).toModel(updateBookRequestDto);
        verify(bookRepository).save(updatedBook);
        verify(bookMapper).toDto(updatedBook);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Verify that the update method is working correctly with non existing book id")
    public void update_NonExistingBookId_ThrowsException() {
        when(bookRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.updateById(NON_EXISTING_ID, updateBookRequestDto));

        String expected = "Can't find book by id = " + NON_EXISTING_ID;
        String actual = exception.getMessage();

        assertThat(actual).isEqualTo(expected);
        verify(bookRepository).findById(NON_EXISTING_ID);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("Verify that the delete by id method is working correctly")
    public void deleteById_ValidBookId_ReturnsNothing() {
        bookService.deleteById(EXISTING_ID);

        verify(bookRepository).deleteById(EXISTING_ID);
    }

    @Test
    @DisplayName("Verify that search method is working correctly")
    public void search_ValidBookSearchParametersDto_ReturnsListBookDto() {
        Specification<Book> specification = mock(Specification.class);
        when(bookSpecificationBuilder.build(bookSearchParametersDto))
                .thenReturn(specification);
        when(bookRepository.findAll(specification)).thenReturn(List.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

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
        when(bookRepository.findAllByCategoryId(EXISTING_ID, pageable)).thenReturn(books);
        when(bookMapper.toDtoWithoutCategoryIds(book))
                .thenReturn(bookDtoWithoutCategoryIds);

        List<BookDtoWithoutCategoryIds> expected = List.of(bookDtoWithoutCategoryIds);
        List<BookDtoWithoutCategoryIds> actual = bookService
                .findAllByCategoryId(EXISTING_ID, pageable);
        assertThat(actual).isEqualTo(expected);
        verify(bookRepository, times(1)).findAllByCategoryId(EXISTING_ID, pageable);
        verify(bookMapper, times(1)).toDtoWithoutCategoryIds(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }
}
