package com.brokage.stock_order.service;

import com.brokage.stock_order.constant.Side;
import com.brokage.stock_order.constant.Status;
import com.brokage.stock_order.constant.TransactionType;
import com.brokage.stock_order.entity.Asset;
import com.brokage.stock_order.entity.Order;
import com.brokage.stock_order.entity.Transaction;
import com.brokage.stock_order.model.OrderRequest;
import com.brokage.stock_order.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.InsufficientResourcesException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.brokage.stock_order.constant.Constants.DATE_AND_TIME_PATTERN;
import static com.brokage.stock_order.constant.Constants.INPUT_DATE_AND_TIME_PATTERN;

@Service
public class OrderServiceImpl implements OrderService{
    Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    AssetService assetService;


    @Autowired
    TransactionService transactionService;


    /**
     * Create an order for the customer
     * @param orderRequest Takes Order variables to save Order table
     * @return Order returns Order type
     */
    @Override
    public Order createOrder(OrderRequest orderRequest) throws InsufficientResourcesException {
        BigDecimal spentAmount = orderRequest.getPrice().multiply(BigDecimal.valueOf(orderRequest.getSize()));
        Asset customerAsset = assetService.listAssetsByCustomerId(orderRequest.getCustomerId());


        if(Side.valueOf(orderRequest.getOrderSide().toUpperCase()).equals(Side.BUY) &&
                customerAsset.getUsableSize().compareTo(spentAmount) >= 0) {
            Order order = new Order(orderRequest.getCustomerId(),
                    orderRequest.getAssetName(),
                    Side.valueOf(orderRequest.getOrderSide().toUpperCase()),
                    orderRequest.getSize(),
                    orderRequest.getPrice(),
                    Status.PENDING,//All the insertion will be in PENDING
                    new Date()); //to update date with the called time

            transactionService.save(convertToTransaction(order, spentAmount)); // All logs will be saved to transaction table

            customerAsset.setUsableSize(customerAsset.getUsableSize().subtract(spentAmount));
            assetService.saveAsset(customerAsset);
            return orderRepository.save(order);
        } else if (Side.valueOf(orderRequest.getOrderSide().toUpperCase()).equals(Side.SELL)) {
            Optional<List<Order>> orders = orderRepository.findOrdersByCustomerId(orderRequest.getCustomerId());
            if(orders.isPresent()){
                List<Order> orderList = orders.get();
                int sumSize = 0;
                for(Order order: orderList) {
                    System.out.println("order: " + order.toString());
                    sumSize += order.getSize();
                }

                //orderRepository.deleteAllByCustomerId(orderRequest.getCustomerId());

                System.out.println("sumSize: " + sumSize + " orderRequest.getSize(): " + orderRequest.getSize());

                if(sumSize >= orderRequest.getSize()) {
                    int finalSize = sumSize - orderRequest.getSize();
                    Order firstOrder = orders.get().getFirst();
                    firstOrder.setSize(finalSize);
                    firstOrder.setOrderSide(Side.SELL);


                    BigDecimal trxAmount = BigDecimal.valueOf(orderRequest.getSize()).multiply(firstOrder.getPrice());

                    System.out.println("finalSize: " + finalSize + "firstOrder.getPrice(): " + firstOrder.getPrice()
                            + "trxAmount: " + trxAmount);

                    customerAsset.setUsableSize(customerAsset.getUsableSize()
                            .add(trxAmount));

                    assetService.saveAsset(customerAsset);
                    transactionService.save(convertToTransaction(firstOrder, trxAmount));

                    if(firstOrder.getSize() > 0)
                        return orderRepository.save(firstOrder);

                    return new Order();
                } else {
                    throw new InsufficientResourcesException("Usable Size is not enough for the customer: "
                            + orderRequest.getCustomerId());
                }

            } else {
                throw new EntityNotFoundException("There is no order with customerId: " + orderRequest.getCustomerId());
            }

        } else {
            throw new InsufficientResourcesException("Usable Money is not enough for the customer: "
                    + orderRequest.getCustomerId());
        }
    }

    private Transaction convertToTransaction(Order order, BigDecimal spentAmount) {
        Transaction transaction = new Transaction();
        transaction.setTransactionType(
                Side.SELL.equals(order.getOrderSide()) ? TransactionType.DEPOSIT
                        : TransactionType.WITHDRAW);
        transaction.setAmount(spentAmount);
        transaction.setCustomerId(order.getCustomerId());
        transaction.setCreateDate(new Date());

        return transaction;
    }

    /**
     * Lists according to customer, start and end date
     * @param customerId customer
     * @param startDate begin date
     * @param endDate end date
     * @return List of Order
     */
    @Override
    public List<Order> findOrdersByCustomerIdAndCreateDate(Long customerId, Date startDate, Date endDate) {

        try {
            // Format the Date object into the desired string format
            Date formattedStartDate = formatDate(startDate);
            Date formattedEndDate = formatDate(endDate);
            List<Order> orders = orderRepository
                    .findOrdersByCustomerIdAndCreateDate(customerId, formattedStartDate, formattedEndDate);

            orders.forEach(order -> order.setCreateDate(convertUtcToGmt3(order.getCreateDate())));

            return orders;


        } catch (Exception e) {
            logger.error("An error occurred: {}", e.getMessage());
        }

        return new ArrayList<>();
    }

    /**
     * @param id orderId for deleting(updating the status)
     */
    @Override
    public void deleteById(@NonNull Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found with orderId: " + id));

            if(order.getStatus().equals(Status.PENDING)) {
                order.setStatus(Status.CANCELED);
                orderRepository.save(order);

                Asset asset = assetService.listAssetsByCustomerId(order.getCustomerId());
                asset.setUsableSize(asset.getUsableSize().add(
                        BigDecimal.valueOf(order.getSize()).multiply(order.getPrice())));
                assetService.saveAsset(asset);
            } else {
                throw new IllegalStateException("Status is not PENDING for id: " + id);
            }

        }

    private Date formatDate(Date inputDate) throws ParseException {
        SimpleDateFormat inputFormat = new SimpleDateFormat(INPUT_DATE_AND_TIME_PATTERN, Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat(DATE_AND_TIME_PATTERN);

        try {
            Date parsedInputDate = inputFormat.parse(inputDate.toString());

            // Log the parsed date
            System.out.println("Parsed Input Date: " + parsedInputDate);

            // Format the parsed Date to the desired output format
            String outputDateString = outputFormat.format(parsedInputDate);

            // Log the formatted date string
            System.out.println("Formatted Date: " + outputDateString);

            // Return the parsed Date object, as the return type is Date
            return outputFormat.parse(outputDateString); // If you want to return the original parsed Date
        } catch (ParseException e) {
            System.err.println("Error parsing date: " + e.getMessage());
            throw e; // Rethrow the exception for handling upstream
        }
    }

    private Date convertUtcToGmt3(Date createDate) {
        TimeZone gmt3 = TimeZone.getTimeZone("GMT+3");
        TimeZone utc = TimeZone.getTimeZone("UTC");
        //Converting order create date to calendar gmt3.
        // Create date is in (gmt+0)utc format so we set it to utc and set the time to create date.
        //Then converting t to gmt3 will give the TR time zone.z
        Calendar calendarUtc = Calendar.getInstance(utc);
        calendarUtc.setTime(createDate);
        long calendarUtcInMs = calendarUtc.getTimeInMillis();

        Calendar calendarGmt3 = Calendar.getInstance(gmt3);
        calendarGmt3.setTimeInMillis(calendarUtcInMs +
                gmt3.getOffset(calendarUtc.getTimeInMillis()));
        return calendarGmt3.getTime();
    }
}
