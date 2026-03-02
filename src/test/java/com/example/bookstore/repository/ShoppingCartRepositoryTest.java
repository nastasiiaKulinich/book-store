package com.example.bookstore.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.bookstore.model.ShoppingCart;
import com.example.bookstore.repository.shoppingcart.ShoppingCartRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShoppingCartRepositoryTest {
    private static final Long EXISTING_USER_ID = 1L;
    private static final Long NON_EXISTING_USER_ID = 100L;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    @DisplayName("Verify that the find shopping cart by user id method is working correctly")
    @Sql(scripts = {
            "classpath:database/delete-from-cart-items-table.sql",
            "classpath:database/delete-from-shopping-carts-table.sql",
            "classpath:database/delete-from-users-roles-table.sql",
            "classpath:database/delete-from-users-table.sql",
            "classpath:database/delete-from-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/fill-users-table.sql",
            "classpath:database/fill-users-roles-table.sql",
            "classpath:database/fill-books-table.sql",
            "classpath:database/fill-shopping-carts-table.sql",
            "classpath:database/fill-cart-items-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findShoppingCartByUserId_ExistingUserId_ReturnsOptionalShoppingCart() {
        Optional<ShoppingCart> actual =
                shoppingCartRepository.findShoppingCartByUserId(EXISTING_USER_ID);
        assertNotNull(actual);
        assertEquals(1, actual.get().getCartItems().size());
    }

    @Test
    @DisplayName(
            "Verify that the find shopping cart by user id method is working correctly"
    )
    @Sql(scripts = {
            "classpath:database/delete-from-cart-items-table.sql",
            "classpath:database/delete-from-shopping-carts-table.sql",
            "classpath:database/delete-from-users-roles-table.sql",
            "classpath:database/delete-from-users-table.sql",
            "classpath:database/delete-from-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/fill-users-table.sql",
            "classpath:database/fill-users-roles-table.sql",
            "classpath:database/fill-books-table.sql",
            "classpath:database/fill-shopping-carts-table.sql",
            "classpath:database/fill-cart-items-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findShoppingCartByUserId_NonExistingUserId_ReturnsOptionalShoppingCart() {
        Optional<ShoppingCart> actual
                = shoppingCartRepository.findShoppingCartByUserId(NON_EXISTING_USER_ID);
        assertNotNull(actual);
        assertEquals(Optional.empty(), actual);
    }
}

