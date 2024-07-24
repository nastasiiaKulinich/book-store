package com.example.bookstore.service.user.impl;

import com.example.bookstore.dto.user.UserRegistrationRequestDto;
import com.example.bookstore.dto.user.UserResponseDto;
import com.example.bookstore.exception.RegistrationException;
import com.example.bookstore.mapper.UserMapper;
import com.example.bookstore.model.Role;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.role.RoleRepository;
import com.example.bookstore.repository.user.UserRepository;
import com.example.bookstore.service.shoppingcart.ShoppingCartService;
import com.example.bookstore.service.user.UserService;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final ShoppingCartService shoppingCartService;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException(String.format("User with this email: %s already exists",
                    requestDto.getEmail()));
        }
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findRoleByName(Role.RoleName.USER));
        user.setRoles(roles);
        User savedUser = userRepository.save(user);
        shoppingCartService.registerNewShoppingCart(savedUser);
        return userMapper.toDto(savedUser);
    }
}
