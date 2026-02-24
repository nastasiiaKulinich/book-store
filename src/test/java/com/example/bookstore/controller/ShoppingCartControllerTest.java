package com.example.bookstore.controller;

import static com.example.bookstore.util.ShoppingCartControllerTestUtil.getShoppingCartDto;
import static com.example.bookstore.util.ShoppingCartControllerTestUtil.getUser;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.bookstore.dto.shoppingcart.ShoppingCartDto;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShoppingCartControllerTest {
    protected static MockMvc mockMvc;
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

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
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
    @DisplayName(
            "Verify that get shopping cart method is working correctly"
    )
    void getShoppingCart_User_ReturnsShoppingCartDto() throws Exception {
        MvcResult result = mockMvc.perform(get("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(getUser()))
                )
                .andExpect(status().isOk())
                .andReturn();

        ShoppingCartDto expected = getShoppingCartDto();
        ShoppingCartDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), ShoppingCartDto.class);
        assertTrue(reflectionEquals(actual, expected, "id"));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
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
    @DisplayName(
            "Verify that remove cart item method is working correctly"
    )
    void removeCartItem_CartItemId_NoContent() throws Exception {
        mockMvc.perform(delete("/cart/items/1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andReturn();
    }
}
