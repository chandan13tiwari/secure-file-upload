package com.securefileupload.controller;

import com.securefileupload.dto.CreateAccountDto;
import com.securefileupload.entity.AccountDetailsEntity;
import com.securefileupload.exception.UserAlreadyExistException;
import com.securefileupload.service.AccountCreationService;
import com.securefileupload.service.AccountDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/api/v1/secure/dashboard")
@CrossOrigin("*")
public class LoginDashboardController {

    private final BCryptPasswordEncoder passwordEncoder;
    private final AccountCreationService accountDetailsService;

    @Autowired
    public LoginDashboardController(BCryptPasswordEncoder passwordEncoder, AccountCreationService accountDetailsService) {
        this.passwordEncoder = passwordEncoder;
        this.accountDetailsService = accountDetailsService;
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }




    @GetMapping("/signup")
    public String create(Model model) {
        model.addAttribute("account", new CreateAccountDto());
        return "create_account";
    }

    @PostMapping("/create")
    public String createAccount(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("acc_type") String role) throws UserAlreadyExistException {
        AccountDetailsEntity accountDetails = AccountDetailsEntity.builder()
                .accountId(UUID.randomUUID())
                .username(username)
                .password(passwordEncoder.encode(password))
                .accountType(role)
                .build();
        accountDetailsService.createAccount(accountDetails);
        return "create_account";
    }
}
