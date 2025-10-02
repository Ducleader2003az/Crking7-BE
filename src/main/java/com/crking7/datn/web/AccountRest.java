package com.crking7.datn.web;

import com.crking7.datn.helper.ApiResponse;
import com.crking7.datn.models.User;
import com.crking7.datn.web.dto.request.*;
import com.crking7.datn.web.dto.response.LoginResponse;
import com.crking7.datn.web.dto.response.OrdersResponse;
import com.crking7.datn.web.dto.response.UserResponse;
import com.crking7.datn.securities.JwtConfig;
import com.crking7.datn.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AccountRest {

    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    private final UserService userService;

    @Autowired
    public AccountRest(AuthenticationManager authenticationManager, JwtConfig jwtConfig, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            UserResponse userResponse = userService.findByUserName(loginRequest.getUsername());
            if (userResponse != null) {
                if (userResponse.getStatus() != 0) {
                    try {
                        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                                loginRequest.getUsername(), loginRequest.getPassword()));

                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        //get token form tokenProvider
                        String token = jwtConfig.generateToken(authentication);

                        LoginResponse loginResponse = new LoginResponse();
                        loginResponse.setToken(token);
                        loginResponse.setUser(userResponse);

                        return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", loginResponse), HttpStatus.OK);
                    } catch (Exception e) {
                        return new ResponseEntity<>(ApiResponse.build(400, false, "Thành công", "Mật khẩu không chính xác"), HttpStatus.OK);
                    }
                } else {
                    return new ResponseEntity<>(ApiResponse.build(400, false, "Thành công", "Tài khoản của bạn đã bị khóa!"), HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<>(ApiResponse.build(400, false, "Thành công", "Tài khoản không tồn tại"), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi ! " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
//        try {
//            Object userResponse = userService.login(loginRequest);
//            if (userResponse != null) {
//                return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", userResponse), HttpStatus.OK);
//            } else {
//                return new ResponseEntity<>(ApiResponse.build(400, false, "Thành công", null), HttpStatus.BAD_REQUEST);
//            }
//        }catch (Exception e){
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        // Invalidate the current user's authentication and clear the SecurityContextHolder
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
            Object userResponse = userService.registerUser(registerRequest);
            if (userResponse instanceof String) {
                String errorMessage = (String) userResponse;
                return new ResponseEntity<>(ApiResponse.build(201, false, "Thất bại", errorMessage), HttpStatus.OK);
            } else {
                UserResponse userResponses = (UserResponse) userResponse;
                return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", userResponses), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/user/changePassword/{id}")
    public ResponseEntity<?> changePassword(@PathVariable(name = "id") Long userId,
                                            @RequestBody PasswordRequest passwordRequest) {
        try {
            String changePasswordResult = userService.changePassword(userId, passwordRequest);
            if (changePasswordResult.equals("Thay đổi mật khẩu thành công")) {
                return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", changePasswordResult), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.build(200, false, "Thất bại", changePasswordResult), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/generate-otp")
    public ResponseEntity<?> regenerateOtp(@RequestBody RegisterRequest registerRequest) {
        try {
            String s = userService.generateOtp(registerRequest);
            if(s == null ){
                return new ResponseEntity<>(ApiResponse.build(201, false, "Thất bại", "Email đã được sử dụng"), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", s), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("user/forgotPass")
    public ResponseEntity<?> forgotPassword(@RequestParam String username) {
        try {
            String s = userService.forgotPassword(username);
            UserResponse user = userService.findByUserName(username);
            if(user == null){
                return new ResponseEntity<>(ApiResponse.build(201, false, "Thất bại", s), HttpStatus.OK);
            }else {
                return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", s), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("user/update/{id}")
    public ResponseEntity<?> updateProfile(@PathVariable(name = "id") Long userId,
                                        @RequestBody UserRequest userRequest) {
        try {
            String s = userService.updateProfile(userId, userRequest);
            if (s != null) {
                return new ResponseEntity<>(ApiResponse.build(200, true, "thành công", s), HttpStatus.OK);
            }else {
                return new ResponseEntity<>(ApiResponse.build(201, false, "Thất bại", "Không thành công"), HttpStatus.OK);
            }
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
