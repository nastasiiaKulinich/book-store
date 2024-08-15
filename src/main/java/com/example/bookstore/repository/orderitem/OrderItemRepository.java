package com.example.bookstore.repository.orderitem;

import com.example.bookstore.model.OrderItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("SELECT item FROM OrderItem item "
            + "LEFT JOIN FETCH item.order order "
            + "LEFT JOIN FETCH order.user user "
            + "WHERE item.id = :itemId AND order.id = :orderId AND user.id = :userId")
    Optional<OrderItem> findByIdAndOrderIdAndUserId(@Param("userId") Long userId,
                                                    @Param("orderId") Long orderId,
                                                    @Param("itemId") Long itemId);

    @Query("SELECT item FROM OrderItem item "
            + "LEFT JOIN FETCH item.order order "
            + "LEFT JOIN FETCH order.user user "
            + "WHERE item.order.id = :orderId AND order.user.id = :userId")
    List<OrderItem> findAllByOrderIdAndUserId(@Param("userId") Long userId,
                                              @Param("orderId") Long orderId);
}
