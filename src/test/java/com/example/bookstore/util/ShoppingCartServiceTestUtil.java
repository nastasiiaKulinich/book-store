package com.example.bookstore.util;

import com.example.bookstore.dto.cartitem.CartItemDto;
import com.example.bookstore.dto.cartitem.CreateCartItemRequestDto;
import com.example.bookstore.dto.cartitem.UpdateCartItemRequestDto;
import com.example.bookstore.dto.shoppingcart.ShoppingCartDto;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.CartItem;
import com.example.bookstore.model.ShoppingCart;
import com.example.bookstore.model.User;
import java.util.ArrayList;
import java.util.Set;

public class ShoppingCartServiceTestUtil {
    private static final Long EXISTING_ID_1 = 1L;

    public static ShoppingCart getShoppingCart() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(EXISTING_ID_1);
        User user = getUser();
        shoppingCart.setUser(user);
        shoppingCart.setCartItems(new ArrayList<>());
        return shoppingCart;
    }

    public static ShoppingCartDto getShoppingCartDto() {
        ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
        shoppingCartDto.setId(EXISTING_ID_1);
        shoppingCartDto.setUserId(EXISTING_ID_1);
        shoppingCartDto.setCartItems(Set.of(getCartItemDto()));
        return shoppingCartDto;
    }

    public static CartItem getCartItem() {
        CartItem cartItem = new CartItem();
        cartItem.setId(EXISTING_ID_1);
        cartItem.setShoppingCart(getShoppingCart());
        Book book = new Book();
        book.setId(EXISTING_ID_1);
        book.setTitle("title1");
        cartItem.setBook(book);
        cartItem.setQuantity(3);
        return cartItem;
    }

    public static CartItemDto getCartItemDto() {
        CartItemDto dto = new CartItemDto();
        dto.setId(EXISTING_ID_1);
        dto.setBookId(EXISTING_ID_1);
        dto.setBookTitle("title1");
        dto.setQuantity(3);
        return dto;
    }

    public static User getUser() {
        User user = new User();
        user.setId(EXISTING_ID_1);
        user.setEmail("email@mail.com");
        user.setFirstName("john");
        user.setLastName("doe");
        return user;
    }

    public static CreateCartItemRequestDto getCreateCartItemRequestDto() {
        CreateCartItemRequestDto cartItemRequestDto = new CreateCartItemRequestDto();
        cartItemRequestDto.setBookId(EXISTING_ID_1);
        cartItemRequestDto.setQuantity(3);
        return cartItemRequestDto;
    }

    public static UpdateCartItemRequestDto getUpdateCartItemRequestDto() {
        UpdateCartItemRequestDto dto = new UpdateCartItemRequestDto();
        dto.setQuantity(5);
        return dto;
    }

    public static CartItem getExistingCartItemWithQuantity(int quantity) {
        CartItem item = getCartItem();
        item.setQuantity(quantity);
        return item;
    }
}
