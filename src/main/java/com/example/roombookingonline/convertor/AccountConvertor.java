package com.example.roombookingonline.convertor;

import com.example.roombookingonline.domain.AccountModel;
import com.example.roombookingonline.entity.AccountEntity;

public class AccountConvertor {
    public static AccountEntity toEntity(AccountModel accountModel){
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setUsername(accountModel.getUsername());
        accountEntity.setPassword(accountModel.getPassword());
        accountEntity.setRole(accountModel.getRole());
        accountEntity.setEmail(accountModel.getEmail());
        accountEntity.setPhone(accountModel.getPhone());
        return accountEntity;
    }
}
