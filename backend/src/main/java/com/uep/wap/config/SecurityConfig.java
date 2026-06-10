package com.uep.wap.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/players").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/players/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/players/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/tournaments/**").hasAnyRole("ARBITER", "ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/tournaments/**").hasAnyRole("ARBITER", "ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/tournaments/**").hasAnyRole("ARBITER", "ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/pairings/*/result").hasAnyRole("ARBITER", "ADMIN")
                .anyRequest().permitAll()
                .and()
                .httpBasic();

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        return new InMemoryUserDetailsManager(
                User.withUsername("admin")
                        .password(passwordEncoder.encode("admin123"))
                        .roles("ADMIN")
                        .build(),
                User.withUsername("arbiter")
                        .password(passwordEncoder.encode("arbiter123"))
                        .roles("ARBITER")
                        .build(),
                User.withUsername("viewer")
                        .password(passwordEncoder.encode("viewer123"))
                        .roles("VIEWER")
                        .build()
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}