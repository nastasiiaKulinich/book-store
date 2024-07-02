package com.example.bookstore.config;

import com.example.bookstore.model.Role;
import com.example.bookstore.repository.role.RoleRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RoleInitializer {
    private final RoleRepository roleRepository;
    private final List<Role.RoleName> roleNames = List.of(Role.RoleName.values());

    @Bean
    public void init() {
        Set<Role.RoleName> existingRoleNames = roleRepository.findAll()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        Set<Role.RoleName> newRoleNames = Stream.of(Role.RoleName.values())
                .filter(roleName -> !existingRoleNames.contains(roleName))
                .collect(Collectors.toSet());

        for (Role.RoleName roleName : newRoleNames) {
            Role role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
        }
    }
}
