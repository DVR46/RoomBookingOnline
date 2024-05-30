package com.example.roombookingonline.controller;

import com.example.roombookingonline.convertor.DateTimeConvertor;
import com.example.roombookingonline.entity.*;
import com.example.roombookingonline.service.BookingService;
import com.example.roombookingonline.service.CustomerService;
import com.example.roombookingonline.service.OrderService;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/receptionist")
public class ReceptionistController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private CustomerService customerService;

    @GetMapping("/scan")
    public String scan(Model model) {
        return "receptionist/scan-qrcode";
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        return "receptionist/login";
    }

    @GetMapping
    public String showBookingListPage(Model model){
        List<RoomBookingEntity> bookingList = bookingService.getCheckInList();
        model.addAttribute("bookingList", bookingList);
        return "receptionist/booking-list";
    }

    @GetMapping("/booking/{code}")
    public String showBookingPage(Model model,@PathVariable String code) {
        Base64 base64 = new Base64();
        String decode = new String(base64.decode(code));
        OrderEntity order = orderService.findByCode(decode);
        model.addAttribute("order", order);
        RoomBookingEntity roomBookingEntity = bookingService.booking(order);
        if(roomBookingEntity == null) {
            return "redirect:/receptionist";
        }
        model.addAttribute("roomBooking", roomBookingEntity);
        model.addAttribute("formatter", DateTimeConvertor.singleFormatter());
        return "receptionist/make-booking";
    }
    @PostMapping("/booking")
    public String booking(@ModelAttribute("roomBooking") RoomBookingEntity roomBooking,
                          @RequestParam("orderId")Long orderId,
                          @RequestParam(value = "roomService", defaultValue = "false")boolean roomService,
                          @RequestParam(value = "massageService", defaultValue = "false")boolean massageService,
                          Model model){
        OrderEntity order = orderService.findById(orderId);
        roomBooking.setOrderEntity(order);
        double serviceAmount = 0;
        if(roomService){
            order.setRoomsService(true);
            serviceAmount += orderService.createRoomService(order.getRoomServiceAmount(), order);
        }
        if(massageService){
            order.setMassageServices(true);
            serviceAmount += orderService.createMassageService(order.getMassageServiceAmount(), order);
        }
        if(serviceAmount >0){
            orderService.updateServiceInvoice(serviceAmount, order);
        }
        RoomBookingEntity bookingSaved = bookingService.bookingSave(roomBooking);
        model.addAttribute("bookingSaved", bookingSaved);
        return "redirect:/receptionist";
    }
    @GetMapping("/booking/detail")
    public String showBookingDetailPage(@RequestParam("bookingId") Long bookingId,Model model){
        RoomBookingEntity roomBookingEntity = bookingService.findById(bookingId);
        model.addAttribute("roomBooking", roomBookingEntity);
        model.addAttribute("formatter", DateTimeConvertor.singleFormatter());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy HH:mm:ss");
        model.addAttribute("dateTimeFormatter", dateTimeFormatter);
        return "receptionist/booking-detail";
    }

    @PostMapping("/booking/detail/update")
    public String updateBookingDetail(@ModelAttribute("roomBooking") RoomBookingEntity roomBooking){
        bookingService.updateCustomer(roomBooking);
        return "redirect:/receptionist/booking/detail?bookingId="+roomBooking.getReservationNumber();
    }

    @GetMapping("/check-in")
    public String checkIn(@RequestParam("bookingId") Long bookingId, Model model){
        bookingService.checkIn(bookingId);
        return "redirect:/receptionist";
    }
    @GetMapping("/checked-in")
    public String checkedIn(@RequestParam("bookingId") Long bookingId, Model model){
        bookingService.checkedIn(bookingId);
        return "redirect:/receptionist";
    }
    @GetMapping("/check-out")
    public String checkOut(@RequestParam("bookingId") Long bookingId, Model model){
        RoomBookingEntity roomBookingEntity = bookingService.checkOut(bookingId);
        model.addAttribute("formatter", DateTimeConvertor.singleFormatter());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy HH:mm:ss");
        model.addAttribute("dateTimeFormatter", dateTimeFormatter);
        model.addAttribute("roomBooking", roomBookingEntity);
        return "receptionist/booking-invoice";
    }
    @GetMapping("/billing")
    public String billing(@RequestParam("bookingId") Long bookingId, Model model){
        RoomBookingEntity roomBookingEntity = bookingService.findById(bookingId);
        model.addAttribute("roomBooking", roomBookingEntity);
        model.addAttribute("formatter", DateTimeConvertor.singleFormatter());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy HH:mm:ss");
        model.addAttribute("dateTimeFormatter", dateTimeFormatter);
        return "receptionist/booking-invoice";
    }

    @GetMapping("/booking-history")
    public String bookingHistory(Model model){
        List<RoomBookingEntity> bookingList = bookingService.getBookingHistory();
        model.addAttribute("bookingList", bookingList);
        return "receptionist/booking-history";
    }

    @GetMapping("/customers")
    public String showCustomersPage(Model model){
        List<CustomerEntity> customerList = customerService.getAllCustomer();
        model.addAttribute("customerList", customerList);
        model.addAttribute("customer", new CustomerEntity());
        return "receptionist/customers-manage";
    }

    @PostMapping("/customer/update")
    public String updateCustomer(@ModelAttribute("customer")CustomerEntity customerEntity){
        customerService.update(customerEntity);
        return "redirect:/receptionist/customers";
    }
}
