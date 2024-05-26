package com.example.roombookingonline.controller;

import com.example.roombookingonline.convertor.DateTimeConvertor;
import com.example.roombookingonline.domain.AccountModel;
import com.example.roombookingonline.entity.RoomTypeEntity;
import com.example.roombookingonline.exception.FieldMissMatchException;
import com.example.roombookingonline.service.AccountService;
import com.example.roombookingonline.service.BookingCartService;
import com.example.roombookingonline.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;

@Controller
public class GuestController {
    @Autowired
    private RoomService roomService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private BookingCartService bookingCartService;

    @GetMapping("/home")
    private String home(Model model) {
        model.addAttribute("nameList", roomService.getRoomTypeNameExits());
        model.addAttribute("roomTypeList", roomService.getRoomType());
        return "guest/home";
    }

    @GetMapping("/register")
    public String showFormRegister(Model model) {
        model.addAttribute("account", new AccountModel());
        model.addAttribute("nameList", roomService.getRoomTypeNameExits());
        return "guest/register";
    }
    @PostMapping("/register")
    public String processRegister(@ModelAttribute("account") AccountModel account) throws FieldMissMatchException {
        account.setRole("ROLE_CUSTOMER");
        accountService.register(account);
        return "redirect:/home";
    }

    @GetMapping("/search")
    public String showResult(Model model, @RequestParam(name = "startDatetime", defaultValue = "none") String start,
                             @RequestParam(name = "endDatetime", defaultValue = "none")String end,
                             @RequestParam(name = "adults", defaultValue = "0")int adults,
                             @RequestParam(name = "childs", defaultValue = "0")int childs) {
        DateTimeFormatter formatter = DateTimeConvertor.singleFormatter();
        model.addAttribute("formatter", formatter);
        LocalDateTime startDateTime = bookingCartService.getStart();
        LocalDateTime endDateTime = bookingCartService.getEnd();
        if(!start.equals("none")){
            startDateTime = LocalDateTime.parse(start, formatter);
            endDateTime = LocalDateTime.parse(end, formatter);
        }
        if(bookingCartService.getBookingCart().isEmpty()){
            bookingCartService.setDatime(startDateTime, endDateTime);
        }
        if(adults == 0){
            adults = 2;
        }
        Map<RoomTypeEntity, Integer> roomTypeMap = roomService.findByDateAndCapacity(startDateTime, endDateTime, adults+childs);
        model.addAttribute("duration", bookingCartService.getDuration());
        model.addAttribute("bookingCart", bookingCartService.getBookingCart());
        model.addAttribute("roomTypeMap", roomTypeMap);
        model.addAttribute("nameList", roomService.getRoomTypeNameExits());
        model.addAttribute("startDatetime", startDateTime);
        model.addAttribute("endDatetime", endDateTime);
        model.addAttribute("checkIn", bookingCartService.getStart());
        model.addAttribute("checkOut", bookingCartService.getEnd());
        model.addAttribute("adults", adults);
        model.addAttribute("childs", childs);
        model.addAttribute("adultsBook",bookingCartService.getTotalAdults());
        model.addAttribute("childsBook", bookingCartService.getTotalChilds());
        model.addAttribute("originBookingCart", bookingCartService.getOriginBookingCart());
        model.addAttribute("roomAmount", bookingCartService.getRoomAmount());
        return "guest/search";
    }

}
