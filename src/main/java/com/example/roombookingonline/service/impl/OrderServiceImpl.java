package com.example.roombookingonline.service.impl;

import com.example.roombookingonline.entity.*;
import com.example.roombookingonline.exception.CouponUsedUpException;
import com.example.roombookingonline.repository.*;
import com.example.roombookingonline.service.OrderService;
import com.example.roombookingonline.ulti.QRCodeUtil;
import com.google.zxing.WriterException;
import jakarta.transaction.Transactional;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RoomOrderRepository roomOrderRepository;
    @Autowired
    private MassageServiceRepository massageServiceRepository;
    @Autowired
    private RoomsServiceRepository roomsServiceRepository;
    @Autowired
    private ServiceInvoiceRepository serviceInvoiceRepository;
    @Autowired
    private CouponRepository couponRepository;

    @Override
    @Transactional
    public OrderEntity createOrder(OrderEntity order, Map<RoomDetailEntity, Integer> roomDetailOrder,
                                   double massageServiceAmount, double roomServiceAmount) throws CouponUsedUpException {
        if(order.getCouponEntity() != null){
            CouponEntity couponEntity = order.getCouponEntity();
            if(couponEntity.getQuantity() == 0){
                throw new CouponUsedUpException("Coupon Is Used Up!");
            }
            couponEntity.setQuantity(couponEntity.getQuantity() -1);
            couponRepository.save(couponEntity);
        }
        order.setAmountRefunds(0);
        order.setExist(true);
        order.setActive(true);
        order.setOrderDate(LocalDateTime.now());
        order.setCode(order.getAccountEntity().getId() + order.getStartDatetime().toString()
                + order.getEndDatetime().toString() + order.getOrderDate().toString());
        OrderEntity orderSaved = orderRepository.save(order);

        for (Map.Entry<RoomDetailEntity, Integer> roomDetail : roomDetailOrder.entrySet()) {
            RoomOrderEntity roomOrderEntity = new RoomOrderEntity();
            roomOrderEntity.setOrderEntity(orderSaved);
            roomOrderEntity.setRoomDetailEntities(roomDetail.getKey());
            roomOrderEntity.setAdults(Integer.parseInt(String.valueOf(roomDetail.getValue()).substring(0,1)));
            roomOrderEntity.setChilds(Integer.parseInt(String.valueOf(roomDetail.getValue()).substring(1)));
            roomOrderRepository.save(roomOrderEntity);
        }
        double serviceAmount = 0;
        if(orderSaved.isMassageServices()){
            serviceAmount += createMassageService(massageServiceAmount, orderSaved);
        }
        if(orderSaved.isRoomsService()){
            serviceAmount += createRoomService(roomServiceAmount, orderSaved);
        }
        if(serviceAmount != 0){
            updateServiceInvoice(serviceAmount, orderSaved);
        }
        return orderSaved;
    }
    @Override
    public double createMassageService(double massageServiceAmount, OrderEntity orderSaved){
        MassageServiceEntity massageService = new MassageServiceEntity();
        massageService.setStartDatetime(orderSaved.getStartDatetime());
        massageService.setEndDatetime(orderSaved.getEndDatetime());
        massageService.setOrderEntity(orderSaved);
        massageService.setAmount(massageServiceAmount);
        massageServiceRepository.save(massageService);
        return massageServiceAmount;
    }
    @Override
    public double createRoomService(double roomServiceAmount, OrderEntity orderSaved){
        RoomsServiceEntity roomsService = new RoomsServiceEntity();
        roomsService.setStartDatetime(orderSaved.getStartDatetime());
        roomsService.setEndDatetime(orderSaved.getEndDatetime());
        roomsService.setOrderEntity(orderSaved);
        roomsService.setAmount(roomServiceAmount);
        roomsServiceRepository.save(roomsService);
        return roomServiceAmount;
    }
    @Override
    public void updateServiceInvoice(double serviceAmount, OrderEntity orderSaved){
        ServiceInvoiceEntity serviceInvoiceEntity = new ServiceInvoiceEntity();
        if(orderSaved.getServiceInvoiceEntity() != null){
            serviceInvoiceEntity = orderSaved.getServiceInvoiceEntity();
            serviceAmount += serviceInvoiceEntity.getTotalAmount();
        }
        orderSaved.setAmount(orderSaved.getAmount() + serviceAmount);
        orderRepository.save(orderSaved);
        serviceInvoiceEntity.setOrderEntity(orderSaved);
        serviceInvoiceEntity.setTotalAmount(serviceAmount);
        serviceInvoiceRepository.save(serviceInvoiceEntity);
    }
    @Override
    public void generateQRCode(OrderEntity order) throws IOException, WriterException {
        String pathDir = "other-resource/orders-code";
        String fileName = String.valueOf(order.getId());
        Base64 base64 = new Base64();
        String encodedString = base64.encodeToString(order.getCode().getBytes());
        QRCodeUtil.saveFile(encodedString, pathDir, fileName);
    }

    @Override
    public OrderEntity findOrderForCustomer(Long orderId, Long accountId){
        return orderRepository.findByIdAndAccountEntity_IdAndExistIsTrue(orderId, accountId);
    }

    @Override
    public List<OrderEntity> findAllOrderForCustomer(Long accountId){
        return orderRepository.findAllByExistIsTrueAndAccountEntity_Id(accountId);
    }

    @Override
    public void cancelOrder(Long orderId){
        OrderEntity order = orderRepository.findById(orderId).orElseThrow();
        Duration duration = Duration.between(LocalDateTime.now(), order.getStartDatetime());
        if(duration.toDays() > 2){
            order.setActive(false);
            double refunds = (double) 20/100 * order.getAmount();
            order.setAmountRefunds(refunds);
            orderRepository.save(order);
        }
    }

    @Override
    public void removeOrder(Long orderId){
        OrderEntity order = orderRepository.findById(orderId).orElseThrow();
        order.setExist(false);
        orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long orderId){
        orderRepository.deleteById(orderId);
    }

    @Override
    public MassageServiceEntity getMassageService(Long orderId){
        MassageServiceEntity massageService = massageServiceRepository.findByOrderId(orderId);
        if(massageService == null){
            massageService = new MassageServiceEntity();
        }
        return massageService;
    }

    @Override
    public RoomsServiceEntity getRoomService(Long orderId){
        RoomsServiceEntity roomsService = roomsServiceRepository.findByOrderId(orderId);
        if(roomsService == null){
            roomsService = new RoomsServiceEntity();
        }
        return roomsService;
    }

    @Override
    public OrderEntity findByCode(String code){
        return orderRepository.findByCode(code);
    }

    @Override
    public OrderEntity findById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow();
    }

    @Override
    public List<OrderEntity> findAllOrderForManager() {
        return orderRepository.findAll();
    }

}
