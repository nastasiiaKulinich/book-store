package com.example.bookstore.repository.role;

import com.example.bookstore.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findRoleByName(Role.RoleName name);
}
