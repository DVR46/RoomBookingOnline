package com.example.roombookingonline.controller;

import com.example.roombookingonline.entity.RoomTypeEntity;
import com.example.roombookingonline.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @GetMapping
    public String showAllType(Model model){
        model.addAttribute("roomTypeList", roomService.getAllRoomTypeActive());
        model.addAttribute("nameList", roomService.getRoomTypeNameExits());
        return "rooms/list";
    }

    @GetMapping("/type/{typeName}")
    public String showRoomName(@PathVariable String typeName, Model model) {
        model.addAttribute("roomTypeList", roomService.getAllTypeByName(typeName));
        model.addAttribute("nameList", roomService.getRoomTypeNameExits());
        return "rooms/list-type-name";
    }

    @GetMapping("{roomTypeId}")
    public String roomTypeDetail(@PathVariable Long roomTypeId, Model model) {
        RoomTypeEntity roomType = roomService.findRoomTypeById(roomTypeId);
        model.addAttribute("roomType", roomType);
        model.addAttribute("roomTypeList", roomService.getAllTypeByName(String.valueOf(roomType.getName())));
        model.addAttribute("nameList", roomService.getRoomTypeNameExits());
        return "rooms/detail";
    }
}
