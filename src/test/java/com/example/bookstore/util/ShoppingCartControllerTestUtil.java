package com.example.bookstore.util;

import com.example.bookstore.dto.cartitem.CartItemDto;
import com.example.bookstore.dto.shoppingcart.ShoppingCartDto;
import com.example.bookstore.model.Role;
import com.example.bookstore.model.User;
import java.util.Set;

public class ShoppingCartControllerTestUtil {
    public static User getUser() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("admin.doe@example.com");
        user.setPassword("securePassword");
        user.setShippingAddress("123 Main St, City, Country");
        Role role = new Role();
        role.setId(2L);
        role.setName(Role.RoleName.USER);
        Role role2 = new Role();
        role2.setId(1L);
        role2.setName(Role.RoleName.ADMIN);
        user.setRoles(Set.of(role, role2));
        return user;
    }

    public static ShoppingCartDto getShoppingCartDto() {
        ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
        shoppingCartDto.setId(1L);
        shoppingCartDto.setUserId(1L);
        shoppingCartDto.setCartItems(Set.of(getCartItemDto()));
        return shoppingCartDto;
    }

    public static CartItemDto getCartItemDto() {
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setId(1L);
        cartItemDto.setBookId(1L);
        cartItemDto.setBookTitle("title1");
        cartItemDto.setQuantity(13);
        return cartItemDto;
    }
}
