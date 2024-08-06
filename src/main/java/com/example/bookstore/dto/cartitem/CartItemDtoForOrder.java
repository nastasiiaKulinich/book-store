package com.example.bookstore.dto.cartitem;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class CartItemDtoForOrder {
    private Long id;
    private Long bookId;
    private String bookTitle;
    private BigDecimal bookPrice;
    private int quantity;
}
