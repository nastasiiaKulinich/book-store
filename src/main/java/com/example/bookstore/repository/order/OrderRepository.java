package com.example.bookstore.repository.order;

import com.example.bookstore.model.Order;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = {"orderItems"})
    Optional<Order> findById(Long id);

    @EntityGraph(attributePaths = {"orderItems"})
    List<Order> findAllByUserId(Long userId);
}
