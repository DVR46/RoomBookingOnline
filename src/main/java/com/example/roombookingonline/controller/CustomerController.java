package com.example.roombookingonline.controller;

import com.example.roombookingonline.convertor.DateTimeConvertor;
import com.example.roombookingonline.entity.*;
import com.example.roombookingonline.exception.CouponUsedUpException;
import com.example.roombookingonline.security.UserPrincipal;
import com.example.roombookingonline.service.*;
import com.example.roombookingonline.ulti.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class CustomerController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CouponService couponService;
    @Autowired
    private BookingCartService bookingCartService;

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        return "customer/login";
    }

    @GetMapping("/booking")
    public String showBookingPage(Model model,
                                  @RequestParam(name = "coupon", defaultValue = "")String coupon) {
        Map<RoomTypeEntity, List<Integer>> roomMap = bookingCartService.getBookingCart();
//        LocalDateTime startDateTime = LocalDateTime.parse(start, formatter);
//        LocalDateTime endDateTime = LocalDateTime.parse(end, formatter);
        OrderEntity order = new OrderEntity();
        CouponEntity couponEntity = new CouponEntity();
        double amountDiscount = 0;
        for (RoomTypeEntity roomTypeEntity : roomMap.keySet()) {
            couponEntity = couponService.findCouponToUse(coupon, roomTypeEntity.getId());
            if(couponEntity.getCouponTypeEntity() != null) {
                amountDiscount = bookingCartService.getAmountDiscount(couponEntity);
                break;
            }
        }
        DateTimeFormatter formatter = DateTimeConvertor.singleFormatter();
        model.addAttribute("formatter", formatter);
        model.addAttribute("checkIn", bookingCartService.getStart());
        model.addAttribute("checkOut", bookingCartService.getEnd());
        model.addAttribute("roomAmount", bookingCartService.getRoomAmount());
        model.addAttribute("amountDiscount", amountDiscount);
        model.addAttribute("coupon", coupon);
        model.addAttribute("couponEntity", couponEntity);
        model.addAttribute("order", order);
        model.addAttribute("totalRoom", bookingCartService.getTotalRoom());
        model.addAttribute("totalAdults", bookingCartService.getTotalAdults());
        model.addAttribute("totalChilds", bookingCartService.getTotalChilds());
        model.addAttribute("massageServiceAmount", bookingCartService.getMassageServiceAmount());
        model.addAttribute("roomServiceAmount", bookingCartService.getRoomServiceAmount());
        return "customer/booking";
    }

    @PostMapping("/booking/process")
    public String processBooking(@RequestParam(name = "couponCode", defaultValue = "none")String couponCode,
                                 @ModelAttribute OrderEntity order,
                                 Model model) throws Exception {
        try {
            if(!couponCode.equals("none")) {
                order.setCouponEntity(couponService.findByCode(couponCode));
            }
            order.setStartDatetime(bookingCartService.getStart());
            order.setEndDatetime(bookingCartService.getEnd());
            UserPrincipal user = SecurityUtil.currentUser();
            order.setAccountEntity(accountService.findById(user.getId()));
            Map<RoomDetailEntity, Integer> roomDetailOrder = bookingCartService.getListRoomForOrder();
            double massageServiceAmount = bookingCartService.getMassageServiceAmount();
            double roomServiceAmount = bookingCartService.getRoomServiceAmount();
            OrderEntity orderSuccess = orderService.createOrder(order, roomDetailOrder, massageServiceAmount, roomServiceAmount);
            orderService.generateQRCode(orderSuccess);
            model.addAttribute("orderSuccess", orderSuccess);
            bookingCartService.deleteCart();
            return "message/booking-success";
        }
        catch (Exception e) {
            System.out.println(e.getMessage() + " " + e.getCause());
            return "message/booking-error";
        } catch (CouponUsedUpException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/order")
    public String showOrderDetail(@RequestParam("orderId") Long orderId,
                            Model model){
        UserPrincipal user = SecurityUtil.currentUser();
        OrderEntity order = orderService.findOrderForCustomer(orderId, user.getId());
        Duration duration = Duration.between(order.getStartDatetime(), order.getEndDatetime());
        long hours = duration.toHours();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy HH:mm:ss");
        model.addAttribute("dateTimeFormatter", dateTimeFormatter);
        model.addAttribute("now", LocalDateTime.now());
        model.addAttribute("duration", hours);
        model.addAttribute("order", order);
        model.addAttribute("formatter", DateTimeConvertor.singleFormatter());
        return "customer/order-detail";
    }

    @GetMapping("/orders")
    public String showOrders(Model model){
        UserPrincipal user = SecurityUtil.currentUser();
        AccountEntity accountEntity = accountService.findById(user.getId());
        List<OrderEntity> orderList = orderService.findAllOrderForCustomer(accountEntity.getId());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy HH:mm:ss");
        model.addAttribute("dateTimeFormatter", dateTimeFormatter);
        model.addAttribute("orderList", orderList);
        model.addAttribute("formatter", DateTimeConvertor.singleFormatter());
        return "customer/orders";
    }

    @PostMapping("/order/cancel")
    private String cancelOrder(@RequestParam("orderId")Long orderId){
        orderService.cancelOrder(orderId);
        return "redirect:/user/orders";
    }

    @GetMapping("/order/delete")
    public String deleteOrder(@RequestParam("orderId")Long orderId){
        orderService.removeOrder(orderId);
        return "redirect:/user/orders";
    }
    @GetMapping("/order/billing")
    public String billing(@RequestParam("orderId") Long bookingId, Model model){
        OrderEntity order = orderService.findById(bookingId);
        RoomBookingEntity roomBooking = order.getRoomBookingEntity();
        model.addAttribute("roomBooking", roomBooking);
        model.addAttribute("formatter", DateTimeConvertor.singleFormatter());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy HH:mm:ss");
        model.addAttribute("dateTimeFormatter", dateTimeFormatter);
        return "receptionist/booking-invoice";
    }

}

//class OrderClone {
//    private Long id;
//    private double amount;
//    private boolean messageServices;
//    private boolean roomsService;
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public double getAmount() {
//        return amount;
//    }
//
//    public void setAmount(double amount) {
//        this.amount = amount;
//    }
//
//    public boolean isMessageServices() {
//        return messageServices;
//    }
//
//    public void setMessageServices(boolean messageServices) {
//        this.messageServices = messageServices;
//    }
//
//    public boolean isRoomsService() {
//        return roomsService;
//    }
//
//    public void setRoomsService(boolean roomsService) {
//        this.roomsService = roomsService;
//    }
//}
