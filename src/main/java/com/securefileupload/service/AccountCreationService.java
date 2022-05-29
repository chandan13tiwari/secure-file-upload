package com.securefileupload.service;

import com.securefileupload.entity.AccountDetailsEntity;
import com.securefileupload.exception.UserAlreadyExistException;
import com.securefileupload.repository.AccountDetailsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountCreationService {

    private AccountDetailsRepository accountDetailsRepository;

    public void createAccount(AccountDetailsEntity accountDetails) throws UserAlreadyExistException {
        try {
            accountDetailsRepository.save(accountDetails);
        } catch(Exception e){
            throw new UserAlreadyExistException("User Already Exist!! Please try to login");
        }
    }
}
