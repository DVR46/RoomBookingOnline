package com.example.roombookingonline.controller;

import jakarta.persistence.Column;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HTMLTest {
    @GetMapping("bs")
    public String bookingSuccess() {
        return "message/booking-success";
    }
}
