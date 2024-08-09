package com.example.bookstore.dto.order;

import com.example.bookstore.model.Order;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateOrderRequestDto {
    @NotNull
    private Order.Status status;
}
