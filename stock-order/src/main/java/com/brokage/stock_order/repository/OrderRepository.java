package com.brokage.stock_order.repository;

import com.brokage.stock_order.entity.Order;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Find orders by customer Id and date range
    @Query(value = "SELECT o FROM Order o WHERE o.customerId = :customerId and o.createDate between :startDate and :endDate")
    List<Order> findOrdersByCustomerIdAndCreateDate(@Param("customerId") Long customerId,
                                                           @Param("startDate") Date startDate,
                                                           @Param("endDate") Date endDate);

    Optional<List<Order>> findOrdersByCustomerId(Long customerId);

    @Transactional
    void deleteAllByCustomerId(Long customerId);

}
