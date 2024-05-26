package com.example.roombookingonline.controller;

import com.example.roombookingonline.convertor.DateTimeConvertor;
import com.example.roombookingonline.entity.*;
import com.example.roombookingonline.repository.RoomBedTypeRepository;
import com.example.roombookingonline.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/manager")
public class ManagerController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private CouponService couponService;

    @GetMapping
    public String home(Model model) {
        return "manager/dash-board";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "manager/login";
    }

    @GetMapping("/room")
    public String showRoomPage(Model model) {
        model.addAttribute("roomList", roomService.findAllRoom());
        model.addAttribute("newRoom", new RoomEntity());
        model.addAttribute("newRoomDetail", new RoomDetailEntity());
        model.addAttribute("roomTypeList", roomService.getAllRoomType());
        model.addAttribute("statusList", roomService.getStatus());
        return "manager/room-manage";
    }
    @GetMapping("/room/create")
    public String showCreateRoomPage(Model model) {
        model.addAttribute("room", new RoomEntity());
        model.addAttribute("roomDetail", new RoomDetailEntity());
        model.addAttribute("roomTypeList", roomService.getAllRoomType());
        model.addAttribute("statusList", roomService.getStatus());
        return "manager/create-room";
    }

    @PostMapping("/room/create")
    public String createRoom(@ModelAttribute("room") RoomEntity room,
                             @ModelAttribute("roomDetail")RoomDetailEntity roomDetail) {
//        System.out.println(roomDetail.getId());
        roomService.saveRoom(room, roomDetail);
        return "redirect:/manager/room";
    }

    @GetMapping("/room-detail/update/{room-id}")
    public String showUpdateRoomDetailPage(Model model, @PathVariable("room-id") Long roomId) {
        RoomDetailEntity roomDetail = roomService.findRoomDetailById(roomId);
        model.addAttribute("roomTypeList", roomService.getAllRoomType());
        model.addAttribute("statusList", roomService.getStatus());
        return null;
    }

    @GetMapping("/room/enable")
    public String enableRoom(@RequestParam("room-number")Long roomNumber){
        roomService.enableRoom(roomNumber);
        return "redirect:/manager/room";
    }

    @GetMapping("/room/disable")
    public String disableRoom(@RequestParam("room-number")Long roomNumber){
        roomService.disableRoom(roomNumber);
        return "redirect:/manager/room";
    }

    @GetMapping("/bed-type")
    public String showBedTypePage(Model model) {
        model.addAttribute("bedTypeList", roomService.getAllBedType());
        model.addAttribute("newType", new RoomBedTypeEntity());
        return "manager/room-bed-type-manage";
    }

    @PostMapping("/bed-type/create")
    public String createBedType(@ModelAttribute("newType") RoomBedTypeEntity roomBedType){
        roomService.saveBedType(roomBedType);
        return "redirect:/manager/bed-type";
    }

    @GetMapping("/bed-type/delete/{bedTypeId}")
    public String deleteBedType(@PathVariable("bedTypeId") Long bedTypeId) {
        roomService.deleteBedType(bedTypeId);
        return "redirect:/manager/bed-type";
    }

    @GetMapping("/room-type")
    public String showRoomTypePage(Model model) {
        model.addAttribute("roomTypeList", roomService.getAllRoomType());
        return "manager/room-type-manage";
    }

    @GetMapping("/room-type/create")
    public String showCreateRoomTypePage(Model model) {
        RoomTypeDetailEntity roomTypeDetail = new RoomTypeDetailEntity();
        RoomTypeEntity roomType = new RoomTypeEntity();
        roomType.setRoomTypeImageEntities(roomService.getNewRoomTypeImage());
        roomTypeDetail.setRoomType(roomType);
        model.addAttribute("bedList", roomService.getTypeBed());
        model.addAttribute("roomType", roomType);
        model.addAttribute("roomTypeDetail", roomTypeDetail);
        return "manager/room-type-detail-update";
    }
    @PostMapping("/room-type/create")
    public String createRoomType(@ModelAttribute("roomType") RoomTypeEntity roomTypeEntity,
                                 @RequestParam("image") MultipartFile image,
                                 @RequestParam("des")String imageDescription) throws IOException {
        roomService.createRoomType(roomTypeEntity, image, imageDescription);
        return null;
    }
    @GetMapping("/room-type-detail/update/{room-type-id}")
    public String showUpdateRoomTypeDetailPage(Model model,
                                               @PathVariable(name = "room-type-id")Long roomTypeId) {
        RoomTypeEntity roomType = roomService.findRoomTypeById(roomTypeId);
        RoomTypeDetailEntity roomTypeDetailEntity = roomType.getRoomTypeDetail();
        if(roomTypeDetailEntity == null){
            roomTypeDetailEntity = new RoomTypeDetailEntity();
        }
        model.addAttribute("bedList", roomService.getTypeBed());
        model.addAttribute("roomType", roomType);
        model.addAttribute("roomTypeDetail", roomTypeDetailEntity);
        return "manager/room-type-detail-update";
    }
    @PostMapping("/room-type-detail/update")
    public String createRoomTypeDetail(@RequestParam(name = "bedPic")MultipartFile bedPic,
                                       @RequestParam(name = "bedDes")String bedDes,
                                       @RequestParam(name = "frontPic")MultipartFile frontPic,
                                       @RequestParam(name = "frontDes")String frontDes,
                                       @RequestParam(name = "backPic")MultipartFile backPic,
                                       @RequestParam(name = "backDes")String backDes,
                                       @RequestParam(name = "bathPic")MultipartFile bathPic,
                                       @RequestParam(name = "bathDes")String bathDes,
                                       @ModelAttribute(name = "roomTypeDetail")RoomTypeDetailEntity roomTypeDetailEntity) throws IOException {
        RoomTypeEntity roomTypeEntity = roomTypeDetailEntity.getRoomType();
        RoomTypeEntity roomTypeSaved = roomService.updateRoomTypeDetail(roomTypeEntity, roomTypeDetailEntity);
        roomService.updateRoomTypeImage(roomTypeSaved, bedPic, RoomTypeImageEntity.photoName.bed, bedDes);
        roomService.updateRoomTypeImage(roomTypeSaved, frontPic, RoomTypeImageEntity.photoName.front, frontDes);
        roomService.updateRoomTypeImage(roomTypeSaved, backPic, RoomTypeImageEntity.photoName.back, backDes);
        roomService.updateRoomTypeImage(roomTypeSaved, bathPic, RoomTypeImageEntity.photoName.bath, bathDes);
        return "redirect:/manager/room-type";
    }
    @GetMapping("/room-type/disable")
    public String disableRoomTypeDetail(@RequestParam(name = "room-type-id")Long roomTypeId) {
        roomService.disableRoomType(roomTypeId);
        return "redirect:/manager/room-type";
    }
    @GetMapping("/room-type/enable")
    public String enableRoomTypeDetail(@RequestParam(name = "room-type-id")Long roomTypeId) {
        roomService.enableRoomType(roomTypeId);
        return "redirect:/manager/room-type";
    }

    @GetMapping("/orders")
    public String showOrdersPage(Model model) {
        model.addAttribute("orderList", orderService.findAllOrderForManager());
        model.addAttribute("formatter", DateTimeConvertor.singleFormatter());
        return "manager/orders-manage";
    }

    @GetMapping("/order/detail/{orderId}")
    public String showOrderDetail(Model model, @PathVariable(name = "orderId")Long orderId) {
        OrderEntity order = orderService.findById(orderId);
        model.addAttribute("order", order);
        model.addAttribute("formatter", DateTimeConvertor.singleFormatter());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy HH:mm:ss");
        model.addAttribute("dateTimeFormatter", dateTimeFormatter);
        return "manager/order-detail";
    }

    @GetMapping("/order/delete")
    public String deleteOrder(@RequestParam("orderId")Long orderId){
        orderService.deleteOrder(orderId);
        return "redirect:/orders-manage";
    }

    @GetMapping("/order/billing")
    public String billing(@RequestParam("bookingId") Long bookingId, Model model){
        RoomBookingEntity roomBookingEntity = bookingService.findById(bookingId);
        model.addAttribute("roomBooking", roomBookingEntity);
        model.addAttribute("formatter", DateTimeConvertor.singleFormatter());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy HH:mm:ss");
        model.addAttribute("dateTimeFormatter", dateTimeFormatter);
        return "receptionist/booking-invoice";
    }

    @GetMapping("/customers")
    public String showCustomersPage(Model model){
        List<CustomerEntity> customerList = customerService.getAllCustomer();
        model.addAttribute("customerList", customerList);
        return "manager/customers-manage";
    }

    @GetMapping("/customer/{customerId}")
    public String showCustomer(Model model, @PathVariable(name = "customerId")Long customerId){
        CustomerEntity customer = customerService.findById(customerId);
        model.addAttribute("customer", customer);
        return "manager/customer-detail";
    }

    @GetMapping("/account")
    public String showAccountPage(Model model){
        List<AccountEntity> accountList = accountService.findAll();
        model.addAttribute("accountList", accountList);
        return "manager/account-manage";
    }

    @PostMapping("/account/ban")
    public String banAccount(Model model, @RequestParam("accountId")Long accountId,
                             @RequestParam("banTime")int banTime){
        accountService.banAccount(accountId, banTime);
        return "redirect:/manager/account";
    }

    @GetMapping("/account/unban/{accountId}")
    public String unbanAccount(Model model, @PathVariable(name = "accountId")Long accountId){
        accountService.unbanAccount(accountId);
        return "redirect:/manager/account";
    }

    @GetMapping("/coupon")
    public String showCouponPage(Model model){
        model.addAttribute("couponTypeList", couponService.getAllCouponType());
        model.addAttribute("couponList", couponService.getAllCoupon());
        model.addAttribute("newCoupon", new CouponEntity());
        return "manager/coupon-manage";
    }

    @PostMapping("/coupon/create")
    public String createCoupon(@ModelAttribute("newCoupon") CouponEntity couponEntity){
        couponService.saveCoupon(couponEntity);
        return "redirect:/manager/coupon";
    }

    @GetMapping("/coupon/delete/{couponId}")
    public String deleteCoupon(@PathVariable(name = "couponId")Long couponId){
        couponService.deleteCoupon(couponId);
        return "redirect:/manager/coupon";
    }

    @GetMapping("/coupon-type")
    public String showCouponTypePage(Model model){
        model.addAttribute("couponTypeList", couponService.getAllCouponType());
        model.addAttribute("newCouponType", new CouponTypeEntity());
        model.addAttribute("roomTypeList", roomService.getAllRoomType());
        return "manager/coupon-type-manage";
    }

    @PostMapping("/coupon-type/create")
    public String createCouponType(@ModelAttribute("newCouponType") CouponTypeEntity couponTypeEntity){
        couponService.saveCouponType(couponTypeEntity);
        return "redirect:/manager/coupon-type";
    }

    @GetMapping("/coupon-type/delete/{couponTypeId}")
    public String deleteCouponType(@PathVariable(name = "couponTypeId") Long couponTypeId){
        couponService.deleteCouponType(couponTypeId);
        return "redirect:/manager/coupon-type";
    }
}
