package com.brokage.stock_order.controller;

import com.brokage.stock_order.entity.Order;
import com.brokage.stock_order.model.OrderRequest;
import com.brokage.stock_order.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import static com.brokage.stock_order.constant.Constants.DATE_AND_TIME_PATTERN;

@RestController
@RequestMapping("/order")
@Validated
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    OrderService orderService;

    @PostMapping("/createOrder")
    public ResponseEntity<Order> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        try {
            return new ResponseEntity<>(orderService.createOrder(orderRequest), HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("An error is occurred : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/listOrders/ByCustomerId/{customerId}/AndStartDate/{startDate}/EndDate/{endDate}")
    public ResponseEntity<List<Order>> listOrdersByCustomerIdAndCreateDate(@Valid
        @NotNull(message = "customerId can't be null") @PathVariable Long customerId,
        @Valid @NotNull(message = "startDate can't be null")
        @PathVariable @DateTimeFormat(pattern = DATE_AND_TIME_PATTERN) Date startDate,
        @Valid @NotNull(message = "endDate can't be null")
        @PathVariable @DateTimeFormat(pattern = DATE_AND_TIME_PATTERN) Date endDate) {
        try {
            return new ResponseEntity<>(
                    orderService.findOrdersByCustomerIdAndCreateDate(customerId, startDate, endDate), HttpStatus.OK);
        } catch(Exception e) {
            logger.error("An error is occurred : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteByOrderId/{id}")
    public ResponseEntity<Order> deleteByOrderid(@Valid @PathVariable @NotNull(message = "id can't be null") Long id) {
        orderService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}