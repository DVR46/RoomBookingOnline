package com.example.roombookingonline.service;

import com.example.roombookingonline.entity.*;
import com.example.roombookingonline.exception.CouponUsedUpException;
import com.google.zxing.WriterException;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface OrderService {
    @Transactional
    OrderEntity createOrder(OrderEntity order, Map<RoomDetailEntity, Integer> roomDetail, double massageServiceAmount,
                            double roomServiceAmount) throws Exception, CouponUsedUpException;

    double createMassageService(double massageServiceAmount, OrderEntity orderSaved);

    double createRoomService(double roomServiceAmount, OrderEntity orderSaved);

    void updateServiceInvoice(double serviceAmount, OrderEntity orderSaved);

    void generateQRCode(OrderEntity order) throws IOException, WriterException;

    OrderEntity findOrderForCustomer(Long orderId, Long accountId);

    List<OrderEntity> findAllOrderForCustomer(Long accountId);

    void cancelOrder(Long orderId);

    void removeOrder(Long orderId);

    void deleteOrder(Long orderId);

    MassageServiceEntity getMassageService(Long orderId);

    RoomsServiceEntity getRoomService(Long orderId);

    OrderEntity findByCode(String code);

    OrderEntity findById(Long orderId);

    List<OrderEntity> findAllOrderForManager();
}
