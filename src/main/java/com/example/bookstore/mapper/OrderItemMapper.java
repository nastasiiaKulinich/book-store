package com.example.bookstore.mapper;

import com.example.bookstore.config.MapperConfig;
import com.example.bookstore.dto.orderitem.OrderItemDto;
import com.example.bookstore.model.CartItem;
import com.example.bookstore.model.Order;
import com.example.bookstore.model.OrderItem;
import com.example.bookstore.model.ShoppingCart;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    OrderItemDto toDto(OrderItem orderItem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "price", source = "book.price")
    OrderItem toEntity(CartItem cartItem);

    default void setOrderItemsFromCart(Order order, ShoppingCart cart) {
        List<OrderItem> orderItems = cart.getCartItems().stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
        order.setOrderItems(orderItems);
        orderItems.forEach(oi -> oi.setOrder(order));
    }
}
