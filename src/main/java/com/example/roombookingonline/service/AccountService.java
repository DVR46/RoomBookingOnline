package com.example.roombookingonline.service;

import com.example.roombookingonline.domain.AccountModel;
import com.example.roombookingonline.entity.AccountEntity;
import com.example.roombookingonline.exception.FieldMissMatchException;

import java.util.List;

public interface AccountService {
    void register(AccountModel accountModel) throws FieldMissMatchException;

    AccountEntity findById(Long i);

    List<AccountEntity> findAll();

    void banAccount(Long accountId, int banTime);

    void unbanAccount(Long accountId);
}
