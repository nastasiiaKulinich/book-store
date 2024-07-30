package com.example.bookstore.mapper;

import com.example.bookstore.config.MapperConfig;
import com.example.bookstore.dto.shoppingcart.ShoppingCartDto;
import com.example.bookstore.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    @Mappings({
            @Mapping(source = "user.id", target = "userId")
    })
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

    @Mappings({
            @Mapping(source = "userId", target = "user.id")
    })
    ShoppingCart toEntity(ShoppingCartDto shoppingCartDto);
}