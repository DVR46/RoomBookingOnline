package com.example.roombookingonline.controller;

import com.example.roombookingonline.service.BookingCartService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Duration;

@Controller
@RequestMapping("/booking-cart")
public class BookingCartController {
    @Autowired
    private BookingCartService bookingCartService;

    @PostMapping("/add")
    public String addRoom(@RequestParam("roomTypeId") Long roomTypeId,
                          @RequestParam("adults")int adults,
                          @RequestParam("childs")int childs) {
        bookingCartService.addToCart(roomTypeId, adults, childs);
        return "redirect:/search?adults="+adults+"&childs="+childs;
    }
    @PostMapping("/update")
    public String updateRoom(@RequestParam("roomTypeId") Long roomTypeId,
                             @RequestParam("capacityRoom")int capacityRoom,
                             @RequestParam("capacity")Integer capacity,
                             @RequestParam("adults")int adults,
                             @RequestParam("childs")int childs){
        bookingCartService.updateRoom(roomTypeId, capacityRoom, capacity, adults, childs);
        return "redirect:/search";
    }
    @PostMapping("/remove")
    public String removeRoom(@RequestParam("roomTypeId") Long roomTypeId,
                             @RequestParam("capacity")Integer capacity) {
        bookingCartService.removeRoomType(roomTypeId, capacity);
        return "redirect:/search";
    }
    @PostMapping("/delete")
    public String deleteRoom() {
        bookingCartService.deleteCart();
        return "redirect:/search";
    }
}
