package com.example.bookstore.service.order.impl;

import com.example.bookstore.dto.order.CreateOrderRequestDto;
import com.example.bookstore.dto.order.OrderResponseDto;
import com.example.bookstore.dto.order.UpdateOrderRequestDto;
import com.example.bookstore.dto.orderitem.OrderItemDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.exception.OrderProcessingException;
import com.example.bookstore.mapper.OrderItemMapper;
import com.example.bookstore.mapper.OrderMapper;
import com.example.bookstore.model.Order;
import com.example.bookstore.model.OrderItem;
import com.example.bookstore.model.ShoppingCart;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.order.OrderRepository;
import com.example.bookstore.repository.orderitem.OrderItemRepository;
import com.example.bookstore.repository.shoppingcart.ShoppingCartRepository;
import com.example.bookstore.repository.user.UserRepository;
import com.example.bookstore.service.order.OrderService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Transactional
    @Override
    public OrderResponseDto createOrder(Long userId, CreateOrderRequestDto createOrderRequestDto) {
        Optional<ShoppingCart> cartOptional =
                shoppingCartRepository.findShoppingCartByUserId(userId);

        if (!cartOptional.isPresent() || cartOptional.get().getCartItems().isEmpty()) {
            throw new OrderProcessingException("Cart is empty for user: " + userId);
        }

        ShoppingCart cart = cartOptional.get();
        Order order = orderMapper.cartToOrder(cart, createOrderRequestDto.getShippingAddress());
        orderRepository.save(order);
        cart.clearCart();

        return orderMapper.toOrderDto(order);
    }

    @Override
    public List<OrderResponseDto> getAllOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findAllByUserId(userId);
        return orderMapper.toOrderDtoList(orders);
    }

    @Override
    public OrderResponseDto updateOrderStatus(
            Long userId,
            Long orderId,
            UpdateOrderRequestDto requestDto
    ) {
        Order order = findOrderById(orderId);
        order.setStatus(requestDto.getStatus());
        orderRepository.save(order);
        return orderMapper.toUpdateDto(order);
    }

    @Override
    public List<OrderItemDto> getOrderItemsByOrderId(Long userId, Long orderId) {
        findUserById(userId);
        Order orderById = findOrderById(orderId);
        return orderById.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemDto getOrderItemById(Long userId, Long orderId, Long itemId) {
        findUserById(userId);
        Order orderById = findOrderById(orderId);
        OrderItem requiredItem = orderById.getOrderItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Can't find order"
                + " with orderId: " + orderId));
        return orderItemMapper.toDto(requiredItem);
    }

    private Order findOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find order by id = " + id)
        );
    }

    private User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find user by id = " + id)
        );
    }
}
