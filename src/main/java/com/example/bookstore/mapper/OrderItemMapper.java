package com.example.bookstore.mapper;

import com.example.bookstore.config.MapperConfig;
import com.example.bookstore.dto.cartitem.CartItemDtoForOrder;
import com.example.bookstore.dto.orderitem.OrderItemDto;
import com.example.bookstore.model.OrderItem;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    OrderItemDto toDto(OrderItem orderItem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "price", source = "bookPrice")
    OrderItem toEntity(CartItemDtoForOrder cartItemDto);

    List<OrderItemDto> toOrderItemDtoList(List<OrderItem> orderItems);
}
