package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.User;
import org.springframework.http.HttpMethod;

@Configuration
public class SecurityConfig {

    // 🔐 STEP 3: Insert users here
    @Bean
    public UserDetailsService userDetailsService() {

        UserDetails admin = User
                .withDefaultPasswordEncoder()
                .username("admin")
                .password("1234")
                .roles("ADMIN")
                .build();

        UserDetails user = User
                .withDefaultPasswordEncoder()
                .username("user")
                .password("1234")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth

                // Login page public
                .requestMatchers("/login.html").permitAll()

                // USER can only POST
                .requestMatchers(HttpMethod.POST, "/employees/**")
                    .hasAnyRole("ADMIN", "USER")

                // Only ADMIN can GET
                .requestMatchers(HttpMethod.GET, "/employees/**")
                    .hasRole("ADMIN")

                // Only ADMIN can UPDATE
                .requestMatchers(HttpMethod.PUT, "/employees/**")
                    .hasRole("ADMIN")

                // Only ADMIN can DELETE
                .requestMatchers(HttpMethod.DELETE, "/employees/**")
                    .hasRole("ADMIN")

                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login.html")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/employee.html", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login.html")
                .permitAll()
            );

        return http.build();
    }
}