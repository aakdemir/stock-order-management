package com.brokage.stock_order;

import com.brokage.stock_order.constant.Side;
import com.brokage.stock_order.constant.Status;
import com.brokage.stock_order.entity.Asset;
import com.brokage.stock_order.entity.Order;
import com.brokage.stock_order.model.OrderRequest;
import com.brokage.stock_order.repository.OrderRepository;
import com.brokage.stock_order.service.AssetService;
import com.brokage.stock_order.service.OrderServiceImpl;
import com.brokage.stock_order.service.TransactionService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.naming.InsufficientResourcesException;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class OrderServiceTest {

    @InjectMocks
    private OrderServiceImpl orderServiceImpl;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private AssetService assetService;

    @Mock
    private TransactionService transactionService;

    private Asset asset;
    private OrderRequest orderRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        asset = new Asset(1L, 1L, "TRY", BigDecimal.valueOf(100), BigDecimal.valueOf(100));
        orderRequest = new OrderRequest(1L, "TRY", "BUY", 1, BigDecimal.valueOf(50));
    }

    @Test
    public void testCreateOrder_Buy() throws Exception {
        // Arrange
        when(assetService.listAssetsByCustomerId(orderRequest.getCustomerId())).thenReturn(asset);
        when(orderRepository.save(any(Order.class))).thenReturn(new Order()); // Mock save

        // Act
        Order order = orderServiceImpl.createOrder(orderRequest);

        // Assert
        assertNotNull(order);
        verify(orderRepository).save(any(Order.class)); // Ensure save was called
        assertEquals(asset.getUsableSize(), BigDecimal.valueOf(50)); // Usable size after order
    }

    @Test
    public void testCreateOrder_Sell_InsufficientSize() {
        // Arrange
        OrderRequest sellOrderRequest = new OrderRequest(1L, "TRY", "SELL", 2, BigDecimal.valueOf(50));
        when(assetService.listAssetsByCustomerId(sellOrderRequest.getCustomerId())).thenReturn(asset);
        Order order = new Order(1L, 1L, "TRY", Side.BUY,
                1, BigDecimal.valueOf(50), Status.PENDING, new Date());
        when(orderRepository.findOrdersByCustomerId(sellOrderRequest.getCustomerId()))
                .thenReturn(Optional.of(Collections.singletonList(order)));

        // Act & Assert
        Exception exception = assertThrows(InsufficientResourcesException.class, () ->
                orderServiceImpl.createOrder(sellOrderRequest));
        assertEquals("Usable Size is not enough for the customer: 1", exception.getMessage());
    }

    @Test
    public void testFindOrdersByCustomerIdAndCreateDate_EmptyList() {
        // Arrange
        Date startDate = new Date();
        Date endDate = new Date();
        when(orderRepository.findOrdersByCustomerIdAndCreateDate(anyLong(), any(Date.class), any(Date.class)))
                .thenReturn(Collections.emptyList());

        // Act
        List<Order> orders = orderServiceImpl.findOrdersByCustomerIdAndCreateDate(1L, startDate, endDate);

        // Assert
        assertTrue(orders.isEmpty());
    }

    @Test
    public void testFindOrdersByCustomerIdAndCreateDate_FullList() {
        // Arrange
        Order order = new Order(1L, 1L, "TRY", Side.BUY,
                1, BigDecimal.valueOf(50), Status.PENDING, new Date());


        Date startDate = new Date();
        Date endDate = new Date();
        when(orderRepository.findOrdersByCustomerIdAndCreateDate(anyLong(), any(Date.class), any(Date.class)))
                .thenReturn(List.of(order));

        // Act
        List<Order> orders = orderServiceImpl.findOrdersByCustomerIdAndCreateDate(1L, startDate, endDate);

        // Assert
        assertEquals(orders.getFirst().getSize(), 1);
    }

    @Test
    public void testDeleteById() {
        // Arrange
        Order order = new Order(1L, 1L, "TRY", Side.BUY,
                1, BigDecimal.valueOf(50), Status.PENDING, new Date());

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(assetService.listAssetsByCustomerId(order.getCustomerId())).thenReturn(asset);



        // Act
        orderServiceImpl.deleteById(order.getId());

        verify(orderRepository).save(order); // Ensure save was called
        verify(assetService).saveAsset(asset); // Ensure save was called

        // Assert
        assertEquals(asset.getUsableSize(), BigDecimal.valueOf(150)); // Usable size after canceling order

    }

    @Test
    public void testDeleteById_OrderNotFound() {
        // Arrange
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(EntityNotFoundException.class, () ->
                orderServiceImpl.deleteById(1L));
        assertEquals("Not found with orderId: 1", exception.getMessage());
    }



    @Test
    public void testDeleteById_OrderStatusIsCanceled() {
        // Arrange
         Order order = new Order(1L, 1L, "TRY", Side.BUY,
                1, BigDecimal.valueOf(50), Status.CANCELED, new Date());

        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        // Act & Assert
        Exception exception = assertThrows(IllegalStateException.class, () ->
                orderServiceImpl.deleteById(1L));
        assertEquals("Status is not PENDING for id: 1", exception.getMessage());
    }

}
