package com.example.roombookingonline.exception;

public class CouponUsedUpException extends Throwable{
    public CouponUsedUpException(String msg) {
        super(msg);
    }
}
