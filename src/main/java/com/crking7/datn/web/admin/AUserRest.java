package com.crking7.datn.web.admin;

import com.crking7.datn.helper.ApiResponse;
import com.crking7.datn.helper.ApiResponsePage;
import com.crking7.datn.models.dtos.TopUserDto;
import com.crking7.datn.web.dto.request.AddEmpRequest;
import com.crking7.datn.web.dto.response.BannerResponse;
import com.crking7.datn.web.dto.response.ProductResponse;
import com.crking7.datn.web.dto.response.UserResponse;
import com.crking7.datn.services.UserService;
import com.crking7.datn.web.dto.request.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin/user")
public class AUserRest {
    @Autowired
    private final UserService userService;

    public AUserRest(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection) {
        try {
            Pair<List<UserResponse>, Integer> result = userService.getAllUsers(keyword,status, pageNo, pageSize, sortBy, sortDirection.equals("asc"));
            List<UserResponse> userResponses = result.getFirst();
            int total = result.getSecond();
            if (!userResponses.isEmpty()) {
                List<Object> data = new ArrayList<>(userResponses);
                return new ResponseEntity<>(ApiResponsePage.build(200, true, pageNo, pageSize, total, "Lấy danh sách thành công", data), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponsePage.build(201, false, pageNo, pageSize, total, "thất bại", null), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/hide")
    public ResponseEntity<?> hideUser(@RequestParam(name = "userId") Long userId,
                                      @RequestParam(name = "id") Long id) {
        try {
            String s = userService.hideUser(userId, id);
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", s), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/show")
    public ResponseEntity<?> showUser(@RequestParam(name = "userId") Long userId,
                                      @RequestParam(name = "id") Long id) {
        try {
            String s = userService.showUser(userId, id);
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", s), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/addEmp")
    public ResponseEntity<?> addEmP(@RequestBody AddEmpRequest addEmpRequest) {
        try {
            UserResponse userResponse = userService.findByUserName(addEmpRequest.getUsername());
            UserResponse userResponse1 = userService.findByEmail(addEmpRequest.getEmail());
            if (userResponse == null) {
                if (userResponse1 == null){
                    String s = userService.addEmP(addEmpRequest);
                    return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", s), HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(ApiResponse.build(200, false, "Thành công", "Email đã tồn tại"), HttpStatus.OK);
                }
            }else {
                return new ResponseEntity<>(ApiResponse.build(200, false, "Thành công", "Tên tài khoản đã tồn tại"), HttpStatus.OK);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("user/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable(name = "id") Long userId,
                                        @RequestBody UserRequest userRequest) {
        try {
            String s = userService.updateUser(userId, userRequest);
            if (s != null) {
                return new ResponseEntity<>(ApiResponse.build(200, true, "thành công", s), HttpStatus.OK);
            }else {
                return new ResponseEntity<>(ApiResponse.build(201, false, "thành công", "Không thành công"), HttpStatus.OK);
            }
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/findTopUser")
    public ResponseEntity<?> findTopUser() {
        try {
            List<TopUserDto> objects = userService.findTopUser();
            if (objects == null) {
                return new ResponseEntity<>(ApiResponse.build(201, false, "thành công", null), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.build(200, true, "thành công", objects), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(ApiResponse.build(404, true, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/allUser")
    public ResponseEntity<?> findAllUser() {
        try {
            List<UserResponse> userResponses = userService.findAllUser();
            if (userResponses == null) {
                return new ResponseEntity<>(ApiResponse.build(201, false, "thành công", null), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.build(200, true, "thành công", userResponses), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(ApiResponse.build(404, true, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getUserAnonymous")
    public ResponseEntity<?> getUserAnonymous() {
        UserResponse userResponse = new UserResponse();
        return new ResponseEntity<>(ApiResponse.build(200, true, "thành công", userResponse), HttpStatus.OK);
    }
}
