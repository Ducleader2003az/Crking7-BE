package com.crking7.datn.securities;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    public JwtAuthenticationFilter(JwtConfig jwtConfig, CustomUserDetailsService customUserDetailsService) {
        this.jwtConfig = jwtConfig;
        this.customUserDetailsService = customUserDetailsService;
    }

    public JwtAuthenticationFilter() {}

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // get JWT(token) from http request
        String token = this.getJWTFromRequest(request);

        // Check if logout endpoint is called
        if (request.getRequestURI().equals("/api/logout")) {
            if (StringUtils.hasText(token)) {
                jwtConfig.addToBlacklist(token); // Add token to the blacklist
            }
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        // validate token
        if (StringUtils.hasText(token) && jwtConfig.validateToken(token)){
            // get username from token
            String username = jwtConfig.getUsernameFromJWT(token);

            // load user associated with token
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // set spring security
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request,response);

    }

    //Bearer<accessToken>
    private String getJWTFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")){
            return bearerToken.substring(7);
        }
        return null;
    }
}
