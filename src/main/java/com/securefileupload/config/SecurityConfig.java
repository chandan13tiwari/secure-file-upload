package com.securefileupload.config;

import com.securefileupload.service.AccountDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Order(1)
public class SecurityConfig {

    @Autowired
    private final AccountDetailsServiceImpl accountDetailsService;

    public SecurityConfig(AccountDetailsServiceImpl accountDetailsService) {
        this.accountDetailsService = accountDetailsService;
    }

    @Bean
    public UserDetailsService getUserDetailService(){
        return this.accountDetailsService;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }



    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(this.getUserDetailService());
        daoAuthenticationProvider.setPasswordEncoder(this.passwordEncoder());

        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationProvider userDetailsService() throws Exception {
       return this.authenticationProvider();
    }

   @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //.antMatchers("**/admin/**").hasRole("ADMIN")
                .antMatchers("/api/v1/secure/user/**").hasRole("USER")
                .antMatchers("/api/v1/secure/dashboard/index")
                .permitAll()
                .antMatchers("/**")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and().csrf().disable()
                .authenticationProvider(this.userDetailsService());

        return http.build();
    }
}