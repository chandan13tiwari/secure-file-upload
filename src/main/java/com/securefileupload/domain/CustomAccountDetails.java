package com.securefileupload.domain;

import com.securefileupload.entity.AccountDetailsEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class CustomAccountDetails implements UserDetails {

    private AccountDetailsEntity accountDetailsEntity;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(accountDetailsEntity.getAccountType());
        return List.of(simpleGrantedAuthority);
    }

    @Override
    public String getPassword() {
        return accountDetailsEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return accountDetailsEntity.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
