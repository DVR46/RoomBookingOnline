package com.example.roombookingonline.convertor;

import java.time.format.DateTimeFormatter;

public class DateTimeConvertor {
    public static DateTimeFormatter singleFormatter() {
        return DateTimeFormatter.ofPattern("dd/MMM/yyyy HH:00");
    }
}
