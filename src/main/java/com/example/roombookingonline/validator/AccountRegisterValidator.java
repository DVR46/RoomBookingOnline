package com.example.roombookingonline.validator;

import com.example.roombookingonline.domain.AccountModel;
import com.example.roombookingonline.exception.FieldMissMatchException;
import org.springframework.stereotype.Component;

import java.util.Objects;
@Component
public class AccountRegisterValidator {
    public void rePasswordCheck(AccountModel accountModel) throws FieldMissMatchException {

        if(!Objects.equals(accountModel.getPassword(), accountModel.getRePassword())){
            throw new FieldMissMatchException("Password miss match.");
        }

    }
}
