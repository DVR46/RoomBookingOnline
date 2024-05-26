package com.example.roombookingonline.service.impl;

import com.example.roombookingonline.convertor.AccountConvertor;
import com.example.roombookingonline.domain.AccountModel;
import com.example.roombookingonline.entity.AccountEntity;
import com.example.roombookingonline.exception.FieldMissMatchException;
import com.example.roombookingonline.repository.AccountRepository;
import com.example.roombookingonline.service.AccountService;
import com.example.roombookingonline.validator.AccountRegisterValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountRegisterValidator validator;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void register(AccountModel accountModel) throws FieldMissMatchException {
        validator.registerUser(accountModel);
        AccountEntity accountEntity = AccountConvertor.toEntity(accountModel);
        accountEntity.setPassword(passwordEncoder.encode(accountEntity.getPassword()));
        accountRepository.save(accountEntity);
    }

    @Override
    public AccountEntity findById(Long id) {
        return accountRepository.findById(id).orElseThrow();
    }

    @Override
    public List<AccountEntity> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public void banAccount(Long accountId, int banTime) {
        LocalDateTime ban = LocalDateTime.now().plusDays(banTime);
        AccountEntity accountEntity = accountRepository.findById(accountId).orElseThrow();
        accountEntity.setBaned(true);
        accountEntity.setBanedTime(ban);
        accountRepository.save(accountEntity);
    }

    @Override
    public void unbanAccount(Long accountId) {
        AccountEntity accountEntity = accountRepository.findById(accountId).orElseThrow();
        accountEntity.setBaned(false);
        accountRepository.save(accountEntity);
    }

}
