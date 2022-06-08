package com.securefileupload.service;

import com.securefileupload.domain.CustomAccountDetails;
import com.securefileupload.entity.AccountDetailsEntity;
import com.securefileupload.repository.AccountDetailsRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountDetailsServiceImpl implements UserDetailsService {

    private final AccountDetailsRepository accountDetailsRepository;

    @Autowired
    public AccountDetailsServiceImpl(AccountDetailsRepository accountDetailsRepository) {
        this.accountDetailsRepository = accountDetailsRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AccountDetailsEntity accountDetailsEntity = accountDetailsRepository.getUserByUsername(username);

        if(accountDetailsEntity == null){
            throw new UsernameNotFoundException("Username not found");
        }

        return new CustomAccountDetails(accountDetailsEntity);
    }
}
