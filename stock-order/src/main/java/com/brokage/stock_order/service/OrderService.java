package com.brokage.stock_order.service;

import com.brokage.stock_order.entity.Order;
import com.brokage.stock_order.model.OrderRequest;
import lombok.NonNull;

import javax.naming.InsufficientResourcesException;
import java.util.Date;
import java.util.List;

public interface OrderService {
    Order createOrder(OrderRequest orderRequest) throws InsufficientResourcesException;
    List<Order> findOrdersByCustomerIdAndCreateDate(Long customerId, Date startDate, Date endDate);
    void deleteById(@NonNull Long id);
}
