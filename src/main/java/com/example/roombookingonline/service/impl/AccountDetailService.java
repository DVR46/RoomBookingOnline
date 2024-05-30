package com.example.roombookingonline.service.impl;

import com.example.roombookingonline.entity.AccountEntity;
import com.example.roombookingonline.repository.AccountRepository;
import com.example.roombookingonline.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountDetailService implements UserDetailsService {
    @Autowired
    private AccountRepository accountRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AccountEntity userEntity = accountRepository.findByEmail(email)
                .orElseThrow(()-> new BadCredentialsException("Email not found!"));
        UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setId(userEntity.getId());
        userPrincipal.setUsername(userEntity.getUsername());
        userPrincipal.setPassword(userEntity.getPassword());
        userPrincipal.setBaned(userEntity.isBaned());
        userPrincipal.setActive(userEntity.isActive());
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(userEntity.getRole()));
        if(userEntity.getRole().equals("ROLE_MANAGER")){
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_RECEPTIONIST"));
        }
        userPrincipal.setAuthorities(grantedAuthorities);
        return userPrincipal;
    }
}
