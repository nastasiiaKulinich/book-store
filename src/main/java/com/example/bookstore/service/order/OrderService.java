package com.example.bookstore.service.order;

import com.example.bookstore.dto.order.CreateOrderRequestDto;
import com.example.bookstore.dto.order.OrderResponseDto;
import com.example.bookstore.dto.order.UpdateOrderRequestDto;
import com.example.bookstore.dto.orderitem.OrderItemDto;
import java.util.List;

public interface OrderService {
    OrderResponseDto createOrder(Long userId, CreateOrderRequestDto createOrderRequestDto);

    List<OrderResponseDto> getAllOrdersByUserId(Long userId);

    OrderResponseDto updateOrderStatus(Long userId, Long orderId, UpdateOrderRequestDto requestDto);

    List<OrderItemDto> getOrderItemsByOrderId(Long userId, Long orderId);

    OrderItemDto getOrderItemById(Long userId, Long orderId, Long itemId);
}
