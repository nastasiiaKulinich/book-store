package com.example.bookstore.mapper;

import com.example.bookstore.dto.order.OrderResponseDto;
import com.example.bookstore.model.CartItem;
import com.example.bookstore.model.Order;
import com.example.bookstore.model.ShoppingCart;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring",
        uses = OrderItemMapper.class,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface OrderMapper {
    @Mapping(target = "id", source = "id")
    OrderResponseDto toUpdateDto(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "total", source = "cart.cartItems", qualifiedByName = "total")
    @Mapping(target = "orderItems", source = "cart.cartItems")
    Order cartToOrder(ShoppingCart cart, String shippingAddress);

    @Named("toOrderDto")
    @Mapping(target = "userId", source = "user.id")
    OrderResponseDto toOrderDto(Order order);

    @IterableMapping(qualifiedByName = "toOrderDto")
    List<OrderResponseDto> toOrderDtoList(List<Order> orders);

    @AfterMapping
    default void updateOrder(@MappingTarget Order order) {
        order.getOrderItems().forEach(oi -> oi.setOrder(order));
        order.setOrderDate(LocalDateTime.now());
    }

    @Named("total")
    default BigDecimal getTotal(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(i -> i.getBook().getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
