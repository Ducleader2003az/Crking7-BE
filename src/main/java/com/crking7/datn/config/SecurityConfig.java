package com.crking7.datn.config;

import com.crking7.datn.securities.CustomUserDetailsService;
import com.crking7.datn.securities.JwtAuthenticationEntryPoint;
import com.crking7.datn.securities.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(proxyTargetClass = true)
public class SecurityConfig {

    private CustomUserDetailsService userDetailsService;
    private JwtAuthenticationEntryPoint authenticationEntryPoint;
    private CorsConfig corsConfig;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        return new JwtAuthenticationFilter();
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/api/register").permitAll()
                .requestMatchers("/api/generate-otp").permitAll()
                .requestMatchers("/api/login").permitAll()
                .requestMatchers("/api/logout").permitAll()
                .requestMatchers("/api/forgotPass").permitAll()
                .requestMatchers("/api/admin/user/addEmp").hasRole("ADMIN")
                    .requestMatchers("/api/admin/**").hasAnyRole("ADMIN", "EMPLOYEE")
                .requestMatchers("/api/category/**").permitAll()
                .requestMatchers("/api/product/**").permitAll()
                .requestMatchers("/api/banner/**").permitAll()
                .requestMatchers("/api/article/**").permitAll()
                .requestMatchers("/api/company/**").permitAll()
                .requestMatchers("/api/user/**").permitAll()
                .requestMatchers("/api/size/**").permitAll()
                .requestMatchers("/api/color/**").permitAll()
                .requestMatchers("/api/sale/**").permitAll()
                .requestMatchers("/api/cart/**").permitAll()
                .requestMatchers("/error").permitAll()
                .anyRequest().authenticated();
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    protected void filterChain(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService((userDetailsService)).passwordEncoder((passwordEncoder()));
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}