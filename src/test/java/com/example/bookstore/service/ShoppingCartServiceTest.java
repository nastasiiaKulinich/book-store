package com.example.bookstore.service;

import static com.example.bookstore.util.ShoppingCartServiceTestUtil.getCartItem;
import static com.example.bookstore.util.ShoppingCartServiceTestUtil.getCreateCartItemRequestDto;
import static com.example.bookstore.util.ShoppingCartServiceTestUtil.getExistingCartItemWithQuantity;
import static com.example.bookstore.util.ShoppingCartServiceTestUtil.getShoppingCart;
import static com.example.bookstore.util.ShoppingCartServiceTestUtil.getShoppingCartDto;
import static com.example.bookstore.util.ShoppingCartServiceTestUtil.getUpdateCartItemRequestDto;
import static com.example.bookstore.util.ShoppingCartServiceTestUtil.getUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.example.bookstore.dto.cartitem.CreateCartItemRequestDto;
import com.example.bookstore.dto.cartitem.UpdateCartItemRequestDto;
import com.example.bookstore.dto.shoppingcart.ShoppingCartDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.CartItemMapper;
import com.example.bookstore.mapper.ShoppingCartMapper;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.CartItem;
import com.example.bookstore.model.ShoppingCart;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.book.BookRepository;
import com.example.bookstore.repository.cartitem.CartItemRepository;
import com.example.bookstore.repository.shoppingcart.ShoppingCartRepository;
import com.example.bookstore.service.shoppingcart.impl.ShoppingCartServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {
    private static final Long EXISTING_ID = 1L;
    private static final Long NON_EXISTING_ID = 100L;
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @Mock
    private CartItemMapper cartItemMapper;
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;
    private ShoppingCart shoppingCart;
    private ShoppingCartDto shoppingCartDto;
    private CreateCartItemRequestDto createCartItemRequestDto;
    private CartItem cartItem;
    private User user;

    @BeforeEach
    void setUp() {
        shoppingCart = getShoppingCart();
        shoppingCartDto = getShoppingCartDto();
        createCartItemRequestDto = getCreateCartItemRequestDto();
        cartItem = getCartItem();
        user = getUser();
    }

    @Test
    @DisplayName("Verify that the get shopping cart method works correctly")
    void getShoppingCart_ExistingId_ReturnsShoppingCartDto() {
        when(shoppingCartRepository.findShoppingCartByUserId(EXISTING_ID))
                .thenReturn(Optional.of(shoppingCart));
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(shoppingCartDto);

        ShoppingCartDto actual = shoppingCartService.getShoppingCart(EXISTING_ID);

        assertThat(actual).isEqualTo(shoppingCartDto);
        verify(shoppingCartMapper).toDto(shoppingCart);
        verifyNoMoreInteractions(shoppingCartMapper);
    }

    @Test
    @DisplayName("Verify that getShoppingCart throws exception for non-existing ID")
    void getShoppingCart_NonExistingId_ThrowsException() {
        when(shoppingCartRepository.findShoppingCartByUserId(NON_EXISTING_ID))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService.getShoppingCart(NON_EXISTING_ID));

        assertThat(exception.getMessage())
                .isEqualTo("Can`t find shopping cart by id = " + NON_EXISTING_ID);
        verify(shoppingCartRepository).findShoppingCartByUserId(NON_EXISTING_ID);
        verifyNoMoreInteractions(shoppingCartRepository);
    }

    @Test
    @DisplayName("Register new shopping cart works correctly")
    void registerNewShoppingCart_User_Ok() {
        when(shoppingCartRepository.save(any(ShoppingCart.class))).thenReturn(shoppingCart);

        shoppingCartService.registerNewShoppingCart(user);

        verify(shoppingCartRepository).save(any(ShoppingCart.class));
        verifyNoMoreInteractions(shoppingCartRepository);
    }

    @Test
    @DisplayName("Add book to shopping cart: existing book increases quantity")
    void addBookToShoppingCart_ExistingBook_UpdatesQuantity() {
        Book book = cartItem.getBook();
        CartItem existingItem = getExistingCartItemWithQuantity(2);
        shoppingCart.setCartItems(List.of(existingItem));

        when(shoppingCartRepository.findShoppingCartByUserId(EXISTING_ID))
                .thenReturn(Optional.of(shoppingCart));
        when(bookRepository.findById(EXISTING_ID)).thenReturn(Optional.of(book));
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(shoppingCartDto);

        ShoppingCartDto actual = shoppingCartService.addBookToShoppingCart(EXISTING_ID,
                createCartItemRequestDto);
        assertThat(actual).isEqualTo(shoppingCartDto);
        assertThat(existingItem.getQuantity()).isEqualTo(5);

        verify(shoppingCartRepository).findShoppingCartByUserId(EXISTING_ID);
        verify(bookRepository).findById(EXISTING_ID);
        verify(shoppingCartRepository).save(shoppingCart);
        verify(shoppingCartMapper).toDto(shoppingCart);
        verifyNoMoreInteractions(shoppingCartRepository, bookRepository,
                cartItemMapper, shoppingCartMapper, cartItemRepository);
    }

    @Test
    @DisplayName("Add book to shopping cart: new book adds CartItem")
    void addBookToShoppingCart_NewBook_AddsCartItem() {
        shoppingCart.setCartItems(new ArrayList<>());
        Book book = new Book();
        book.setId(EXISTING_ID);

        when(shoppingCartRepository.findShoppingCartByUserId(EXISTING_ID))
                .thenReturn(Optional.of(shoppingCart));
        when(bookRepository.findById(EXISTING_ID)).thenReturn(Optional.of(book));
        when(cartItemMapper.toEntity(any(CreateCartItemRequestDto.class))).thenReturn(cartItem);
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(shoppingCartDto);

        ShoppingCartDto actual = shoppingCartService.addBookToShoppingCart(EXISTING_ID,
                createCartItemRequestDto);

        assertThat(actual).isEqualTo(shoppingCartDto);
        assertThat(shoppingCart.getCartItems()).contains(cartItem);

        verify(shoppingCartRepository).findShoppingCartByUserId(EXISTING_ID);
        verify(bookRepository).findById(EXISTING_ID);
        verify(cartItemMapper).toEntity(any(CreateCartItemRequestDto.class));
        verify(shoppingCartRepository).save(shoppingCart);
        verify(shoppingCartMapper).toDto(shoppingCart);
        verifyNoMoreInteractions(shoppingCartRepository, bookRepository,
                cartItemMapper, shoppingCartMapper, cartItemRepository);
    }

    @Test
    @DisplayName("Update cart item quantity correctly")
    void updateCartItem_ExistingCartItemId_ReturnsUpdatedShoppingCartDto() {
        Long cartItemId = 1L;
        int newQuantity = 5;
        UpdateCartItemRequestDto updateRequest = getUpdateCartItemRequestDto();

        doNothing().when(cartItemRepository).updateCartItemQuantityById(newQuantity, cartItemId);
        when(shoppingCartRepository.findShoppingCartByUserId(EXISTING_ID))
                .thenReturn(Optional.of(shoppingCart));
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(shoppingCartDto);

        ShoppingCartDto actual = shoppingCartService.updateCartItem(EXISTING_ID,
                cartItemId, updateRequest);
        assertThat(actual).isEqualTo(shoppingCartDto);

        verify(cartItemRepository).updateCartItemQuantityById(newQuantity, cartItemId);
        verify(shoppingCartRepository).findShoppingCartByUserId(EXISTING_ID);
        verify(shoppingCartMapper).toDto(shoppingCart);
        verifyNoMoreInteractions(cartItemRepository, shoppingCartRepository, shoppingCartMapper);
    }

    @Test
    @DisplayName("Remove cart item: existing item is removed")
    void removeCartItem_ExistingCartItemId_Ok() {
        ShoppingCart cart = cartItem.getShoppingCart();
        cart.setCartItems(new ArrayList<>());
        cart.getCartItems().add(cartItem);

        when(cartItemRepository.findById(EXISTING_ID)).thenReturn(Optional.of(cartItem));
        doNothing().when(cartItemRepository).delete(cartItem);

        shoppingCartService.removeCartItem(EXISTING_ID);

        verify(cartItemRepository).findById(EXISTING_ID);
        verify(cartItemRepository).delete(cartItem);
        verifyNoMoreInteractions(cartItemRepository);
    }

    @Test
    @DisplayName("Remove cart item: non-existing item throws exception")
    void removeCartItem_NonExistingCartItemId_ThrowsException() {
        when(cartItemRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService.removeCartItem(NON_EXISTING_ID));

        assertThat(exception.getMessage())
                .isEqualTo("Can`t find cartItem by id = " + NON_EXISTING_ID);
        verify(cartItemRepository).findById(NON_EXISTING_ID);
        verifyNoMoreInteractions(cartItemRepository);
    }
}
