package com.example.bookstore.service.shoppingcart;

import com.example.bookstore.dto.cartitem.CartItemDto;
import com.example.bookstore.dto.cartitem.CreateCartItemRequestDto;
import com.example.bookstore.dto.cartitem.UpdateCartItemRequestDto;
import com.example.bookstore.dto.shoppingcart.ShoppingCartDto;
import com.example.bookstore.model.User;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart(Long id);

    ShoppingCartDto addBookToShoppingCart(
            Long id, CreateCartItemRequestDto cartItemRequestDto);

    CartItemDto updateCartItem(
            Long id, Long cartItemId, UpdateCartItemRequestDto cartItemRequestDto);

    void removeCartItem(Long cartItemId);

    void registerNewShoppingCart(User user);
}
