package com.example.roombookingonline.controller;

import com.example.roombookingonline.convertor.DateTimeConvertor;
import com.example.roombookingonline.entity.*;
import com.example.roombookingonline.exception.CouponUsedUpException;
import com.example.roombookingonline.security.UserPrincipal;
import com.example.roombookingonline.service.*;
import com.example.roombookingonline.service.impl.VNPayService;
import com.example.roombookingonline.ulti.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
    @Autowired
    private VNPayService vnPayService;


    @GetMapping("/login")
    public String showLoginPage(Model model, @RequestParam(value = "error", defaultValue = "")String error) {
        model.addAttribute("error", error);
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

    @GetMapping("/booking/process")
    public String processBooking(HttpServletRequest request,
                                 @RequestParam(name = "couponCode", defaultValue = "none")String couponCode,
                                 @ModelAttribute OrderEntity order) {
        String redirect = vnPayService.createVnPayPayment(request, order.getAmount()
                , couponCode, order.isRoomsService(), order.isMassageServices());
        System.out.println(redirect);
        return "redirect:" + redirect;
    }

    @GetMapping("/booking/callback")
    public String processBookingCallback(HttpServletRequest request, Model model) throws Exception, CouponUsedUpException {
        String status = request.getParameter("vnp_ResponseCode");
        if (status.equals("00")) {
            try {
                OrderEntity order = new OrderEntity();
                order.setAmount(Double.parseDouble(request.getParameter("price")));
                String couponCode = request.getParameter("couponCode");
                String roomService = request.getParameter("roomService");
                String massageServices = request.getParameter("massageService");
                if(!couponCode.equals("none")) {
                    order.setCouponEntity(couponService.findByCode(couponCode));
                }
                if(roomService.equals("true")) {
                    order.setRoomsService(true);
                }
                if(massageServices.equals("true")) {
                    order.setMassageServices(true);
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
            } catch (Exception e) {
                System.out.println(e.getMessage() + " " + e.getCause());
                return "message/booking-error";
            }
        } else {
            return "message/booking-error";
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

    @GetMapping("/profile")
    public String profile(Model model){
        UserPrincipal user = SecurityUtil.currentUser();
        AccountEntity account = accountService.findById(user.getId());
        model.addAttribute("account", account);
        return "customer/account-profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute("account")AccountEntity accountEntity){
        accountService.updateAccount(accountEntity);
        return "redirect:/user/profile";
    }

}

