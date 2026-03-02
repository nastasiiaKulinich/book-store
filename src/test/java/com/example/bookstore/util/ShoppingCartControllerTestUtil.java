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
        Role roleUser = new Role();
        roleUser.setId(2L);
        roleUser.setName(Role.RoleName.USER);
        Role roleAdmin = new Role();
        roleAdmin.setId(1L);
        roleAdmin.setName(Role.RoleName.ADMIN);
        user.setRoles(Set.of(roleUser, roleAdmin));
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

    public static ShoppingCartDto getShoppingCartDtoAfterAdding() {
        ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
        shoppingCartDto.setId(1L);
        shoppingCartDto.setUserId(1L);

        CartItemDto cartItemDto = getCartItemDto();
        cartItemDto.setQuantity(26);

        shoppingCartDto.setCartItems(Set.of(cartItemDto));
        return shoppingCartDto;
    }

    public static ShoppingCartDto getShoppingCartDtoAfterUpdate() {
        ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
        shoppingCartDto.setId(1L);
        shoppingCartDto.setUserId(1L);

        CartItemDto cartItemDto = getCartItemDto();
        cartItemDto.setQuantity(5);

        shoppingCartDto.setCartItems(Set.of(cartItemDto));
        return shoppingCartDto;
    }
}
