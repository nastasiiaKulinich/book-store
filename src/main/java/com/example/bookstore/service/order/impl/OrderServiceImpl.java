package com.example.bookstore.service.order.impl;

import com.example.bookstore.dto.order.CreateOrderRequestDto;
import com.example.bookstore.dto.order.OrderResponseDto;
import com.example.bookstore.dto.order.UpdateOrderRequestDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.OrderItemMapper;
import com.example.bookstore.mapper.OrderMapper;
import com.example.bookstore.model.Order;
import com.example.bookstore.model.OrderItem;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.cartitem.CartItemRepository;
import com.example.bookstore.repository.order.OrderRepository;
import com.example.bookstore.repository.orderitem.OrderItemRepository;
import com.example.bookstore.repository.user.UserRepository;
import com.example.bookstore.service.book.BookService;
import com.example.bookstore.service.order.OrderService;
import com.example.bookstore.service.shoppingcart.ShoppingCartService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ShoppingCartService shoppingCartService;
    private final BookService bookService;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Transactional
    @Override
    public OrderResponseDto createOrder(Long userId, CreateOrderRequestDto createOrderRequestDto) {
        Order order = new Order();
        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("Can't find user by id: " + userId)
        );
        order.setUser(user);
        order.setOrderDate(LocalDate.now());
        order.setStatus(Order.Status.PENDING);
        order.setShippingAddress(createOrderRequestDto.getShippingAddress());

        Set<OrderItem> orderItems = getOrderItemsForUser(userId);
        order.setOrderItems(orderItems);

        BigDecimal total = calculateTotalPrice(orderItems);
        order.setTotal(total);

        Order savedOrder = orderRepository.save(order);
        orderItems.forEach(orderItem -> orderItem.setOrder(savedOrder));
        orderItemRepository.saveAll(orderItems);

        return orderMapper.toDto(savedOrder);
    }

    @Override
    public List<OrderResponseDto> getAllOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findAllByUserId(userId);
        return orders.stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponseDto getOrderById(Long id) {
        Order order = findOrderById(id);
        return orderMapper.toDto(order);
    }

    @Override
    public OrderResponseDto updateOrderStatus(
            Long userId,
            Long orderId,
            UpdateOrderRequestDto requestDto
    ) {
        Order order = findOrderById(orderId);
        order.setStatus(requestDto.getStatus());
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    private Set<OrderItem> getOrderItemsForUser(Long userId) {
        Set<OrderItem> orderItems =
                shoppingCartService.getShoppingCart(userId).getCartItems().stream()
                        .map(orderItemMapper::toEntity)
                        .collect(Collectors.toSet());

        orderItems.forEach(orderItem ->
                orderItem.setPrice(bookService.findById(orderItem.getBook().getId()).getPrice()));

        return orderItems;
    }

    private BigDecimal calculateTotalPrice(Set<OrderItem> orderItems) {
        BigDecimal total = BigDecimal.valueOf(0);
        for (OrderItem item : orderItems) {
            total = total.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        return total;
    }

    private Order findOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find order by id = " + id)
        );
    }
}
