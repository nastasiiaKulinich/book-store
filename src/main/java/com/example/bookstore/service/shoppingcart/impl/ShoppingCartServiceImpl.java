package com.example.bookstore.service.shoppingcart.impl;

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
import com.example.bookstore.service.shoppingcart.ShoppingCartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;
    private final BookRepository bookRepository;

    @Override
    public ShoppingCartDto getShoppingCart(Long id) {
        ShoppingCart shoppingCart = getShoppingCartByUserId(id);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCartDto addBookToShoppingCart(
            Long id, CreateCartItemRequestDto cartItemRequestDto) {
        ShoppingCart shoppingCart = getShoppingCartByUserId(id);

        Book book = bookRepository.findById(cartItemRequestDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find book by id = " + cartItemRequestDto.getBookId()));

        addOrUpdateCartItem(shoppingCart, book, cartItemRequestDto);

        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCartDto updateCartItem(
            Long id, Long cartItemId, UpdateCartItemRequestDto cartItemRequestDto) {
        cartItemRepository.updateCartItemQuantityById(
                cartItemRequestDto.getQuantity(), cartItemId);
        return shoppingCartMapper.toDto(getShoppingCartByUserId(id));
    }

    @Override
    @Transactional
    public void removeCartItem(Long cartItemId) {
        CartItem cartItem = getCartItemById(cartItemId);
        ShoppingCart shoppingCart = cartItem.getShoppingCart();

        shoppingCart.getCartItems().remove(cartItem);
        shoppingCartRepository.save(shoppingCart);

        cartItemRepository.delete(cartItem);
    }

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    private ShoppingCart getShoppingCartByUserId(Long id) {
        return shoppingCartRepository.findShoppingCartByUserId(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can`t find shopping cart by id = " + id));
    }

    private CartItem getCartItemById(Long cartItemId) {
        return cartItemRepository.findById(cartItemId).orElseThrow(
                () -> new EntityNotFoundException("Can`t find cartItem by id = " + cartItemId)
        );
    }

    private void addOrUpdateCartItem(ShoppingCart shoppingCart,
            Book book, CreateCartItemRequestDto cartItemRequestDto) {
        CartItem existingCartItem = shoppingCart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(book.getId()))
                .findFirst()
                .orElse(null);

        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity()
                    + cartItemRequestDto.getQuantity());
            cartItemRepository.save(existingCartItem);
        } else {
            CartItem newCartItem = cartItemMapper.toEntity(cartItemRequestDto);
            newCartItem.setShoppingCart(shoppingCart);
            newCartItem.setBook(book);
            cartItemRepository.save(newCartItem);
        }
    }
}
