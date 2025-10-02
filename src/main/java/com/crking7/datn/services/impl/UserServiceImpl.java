package com.crking7.datn.services.impl;

import com.crking7.datn.exceptions.ResourceNotFoundException;
import com.crking7.datn.models.Product;
import com.crking7.datn.models.dtos.TopUserDto;
import com.crking7.datn.repositories.RoleRepository;
import com.crking7.datn.repositories.UserRepository;
import com.crking7.datn.securities.JwtConfig;
import com.crking7.datn.services.UserService;
import com.crking7.datn.utils.EmailUtils;
import com.crking7.datn.utils.OtpService;
import com.crking7.datn.utils.OtpUtil;
import com.crking7.datn.utils.Utils;
import com.crking7.datn.web.dto.request.*;
import com.crking7.datn.web.dto.response.LoginResponse;
import com.crking7.datn.web.dto.response.OrdersResponse;
import com.crking7.datn.web.dto.response.SaleResponse;
import com.crking7.datn.web.dto.response.UserResponse;
import com.crking7.datn.mapper.UserMapper;
import com.crking7.datn.models.Role;
import com.crking7.datn.models.User;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    @Autowired
    private OtpUtil otpUtil;
    @Autowired
    private OtpService otpService;
    @Autowired
    private EmailUtils emailUtils;
    @Autowired
    private Utils utils;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserMapper userMapper, OtpUtil otpUtil,
                           EmailUtils emailUtils,
                           OtpService otpService,
                           Utils utils,
                           AuthenticationManager authenticationManager,
                           JwtConfig jwtConfig) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.otpUtil = otpUtil;
        this.emailUtils = emailUtils;
        this.otpService = otpService;
        this.utils = utils;
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;
    }

    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public Pair<List<UserResponse>, Integer> getAllUsers(String keyword,Integer status, int pageNo, int pageSize, String sortBy, boolean asc) {
        if (pageNo < 1) {
            pageNo = 1;
        }
        Sort.Direction sortDirection = asc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sortDirection, sortBy);
        Page<User> users = userRepository.findAllUser(keyword,status, pageable);
        int total = (int) users.getTotalElements();
        List<UserResponse> userResponses = users.getContent().stream()
                .map(userMapper::mapModelToResponse)
                .toList();
        return Pair.of(userResponses, total);
    }

    @Override
    public UserResponse findByUserName(String username) {
        return userMapper.mapModelToResponse(userRepository.findByUsername(username));
    }
    @Override
    public UserResponse findByEmail(String email) {
        return userMapper.mapModelToResponse(userRepository.findByEmail(email));
    }

    @Override
    public List<UserResponse> findAllUser() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::mapModelToResponse)
                .toList();
    }

    @Override
    public String hideUser(Long userId, Long id) {
        User user = userRepository.findById(userId).orElseThrow();
        User adminOrEmp = userRepository.findById(id).orElseThrow();
        boolean userHideA = user.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
        boolean userHideE = user.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ROLE_EMPLOYEE"));
        boolean isAdmin = adminOrEmp.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
        boolean isEmployee = adminOrEmp.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ROLE_EMPLOYEE"));
        if (userHideA) {
            return "Không thể ẩn tài khoản admin";
        } else if (userHideE) {
            if (isAdmin) {
                Date date = new Date();
                user.setStatus(0);
                user.setModifiedDate(date);
                userRepository.save(user);
                return "Tài khoản đã được khóa";
            } else {
                return "Bạn không phải là admin";
            }
        } else {
            Date date = new Date();
            user.setStatus(0);
            user.setModifiedDate(date);
            userRepository.save(user);
            return "Tài khoản đã được khóa";
        }
    }


    @Override
    public String showUser(Long userId, Long id) {
        User userToShow = userRepository.findById(userId).orElseThrow();
        User adminOrEmp = userRepository.findById(id).orElseThrow();

        boolean isAdmin = userToShow.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));

        if (isAdmin) {
            boolean isEmployee = adminOrEmp.getRoles().stream()
                    .anyMatch(role -> role.getName().equals("ROLE_EMPLOYEE"));

            if (isEmployee) {
                Date date = new Date();
                userToShow.setStatus(1);
                userToShow.setModifiedDate(date);
                userRepository.save(userToShow);
                return "Người dùng đã được mở khóa";
            } else {
                return "Bạn phải liên hệ với admin để mở khóa tài khoản này";
            }
        } else {
            Date date = new Date();
            userToShow.setStatus(1);
            userToShow.setModifiedDate(date);
            userRepository.save(userToShow);
            return "Người dùng đã được mở khóa";
        }
    }

    @Override
    @Transactional
    public String addEmP(AddEmpRequest addEmpRequest) {
        User user = userRepository.findByUsername(addEmpRequest.getUsername());
        User user1 = userRepository.findByEmail(addEmpRequest.getEmail());
        if (user == null) {
            if (user1 == null) {
                addEmpRequest.setPassword(passwordEncoder().encode(addEmpRequest.getPassword()));
                User user2 = userMapper.mapSignupToModel2(addEmpRequest);
                Date date = new Date();
                user2.setModifiedDate(date);
                user2.setCreatedDate(date);
                user2.setStatus(1);
                // Set default role_id = 3 (ROLE_EMPLOYEE)
                long defaultRoleId = 3;
                Role role = roleRepository.findById(defaultRoleId);
                user2.getRoles().add(role);
                // Lưu thông tin tài khoản mới
                userRepository.save(user2);
                return "Tạo tài khoản nhân viên thành công";
            } else {
                return "Email đã tồn tại";
            }
        } else {
            return "Tên tài khoản này đã tồn tại";
        }
    }

    @Override
    public UserResponse getUser(long userId) {
        return userMapper.mapModelToResponse(userRepository.findById(userId).orElseThrow());
    }

    @Override
    public String generateOtp(RegisterRequest registerRequest) {
        String email = registerRequest.getEmail();
        User user = userRepository.findByEmail(email);
        if(user == null){
            try {
                emailUtils.sendOtpEmail(email);
            } catch (MessagingException e) {
                throw new RuntimeException("Không thể gửi otp vui lòng thử lại", e);
            }
            return "Đã lấy mã OTP... Vui lòng xác minh tài khoản trong vòng 5 phút";
        }else{
            return null;
        }
    }

    @Override
    @Transactional
    public Object registerUser(RegisterRequest registerRequest) {
        // Kiểm tra trong csdl đã tồn tại người sử dụng có username tương tự user của yêu cầu đăng kí chưa
        User checkUser = userRepository.findByUsername(registerRequest.getUsername());
        User checkUser2 = userRepository.findByEmail(registerRequest.getEmail());
        String otp = otpService.getOtp(registerRequest.getEmail());
        // Kiểm tra mã OTP trong yêu cầu với mã OTP được gửi qua email
        if (registerRequest.getOtp().equals(otp)) {
            if (checkUser == null) {
                if (checkUser2 == null) {
                    registerRequest.setPassword(passwordEncoder().encode(registerRequest.getPassword()));
                    User user = userMapper.mapSignupToModel(registerRequest);
                    user.setOtpGeneratedTime(LocalDateTime.now());
                    if (Duration.between(user.getOtpGeneratedTime(),
                            LocalDateTime.now()).getSeconds() < (1 * 300)) {
                        Date currentDate = new Date();
                        user.setCreatedDate(currentDate);
                        user.setModifiedDate(currentDate);
                        user.setStatus(1);
                        user.setOtp(otp);
                        // Set default role_id = 2 (ROLE_USER)
                        long defaultRoleId = 2;
                        Role role = roleRepository.findById(defaultRoleId);
                        user.getRoles().add(role);

                        // Lưu thông tin tài khoản mới
                        User newUser = userRepository.save(user);
                        // Xóa mã OTP sau khi đăng ký thành công
                        otpService.removeOtp(registerRequest.getEmail());
                        return userMapper.mapModelToResponse(newUser);
                    } else {
                        return "Mã OTP đã hết hạn. Vui lòng lấy lại mã OTP!";
                    }
                } else {
                    return "Email đã tồn tại .";
                }
            } else {
                return "Tài khoản đã tồn tại.";
            }

        } else {
            return "Mã OTP Không hợp lệ!";
        }
    }

    @Override
    public String updateUser(Long userId, UserRequest userRequest) {
        if (userId != null && userRequest != null) {
            //thực hiện lấy thông tin cũ
            User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
            boolean isAdminOrEmployee = user.getRoles().stream()
                    .anyMatch(role -> role.getName().equals("ROLE_ADMIN") || role.getName().equals("ROLE_EMPLOYEE"));
            if (isAdminOrEmployee) {
                return "Không thể sửa tài khoản admin và emp";
            } else {
                //update thông tin cho user
                user.setModifiedDate(new Date());
                userMapper.updateModel(user, userRequest);
                userRepository.save(user);
                //trả về thông tin người sử dụng vừa cập nhật
                return "Cập nhật thành công";
            }
        }
        return null;
    }

    @Override
    public String updateProfile(Long userId, UserRequest userRequest) {
        if (userId != null && userRequest != null) {
            //thực hiện lấy thông tin cũ
            User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
            //update thông tin cho user
            user.setModifiedDate(new Date());
            userMapper.updateModel(user, userRequest);
            userRepository.save(user);
            //trả về thông tin người sử dụng vừa cập nhật
            return "Cập nhật thành công";
        }
        return null;
    }

    @Override
    public String changePassword(Long userId, PasswordRequest passwordRequest) {
        User user = userRepository.findById(userId).orElseThrow();
        //Kiểm tra thông tin của passrequest có hợp lệ hay không
        if (passwordRequest.getNewPassword() != null && passwordRequest.getCfNewPassword() != null) {
            //kiểm tra 2 pass có trùng nhau không
            if (Objects.equals(passwordRequest.getNewPassword(), passwordRequest.getCfNewPassword())) {
                if (passwordEncoder().matches(passwordRequest.getOldPassword(), user.getPassword())) {
                    if (!passwordEncoder().matches(passwordRequest.getNewPassword(), user.getPassword())) {
                        //cập nhật pass mới
                        user.setPassword(passwordEncoder().encode(passwordRequest.getNewPassword()));
                        //lưu và trả về kết quả
                        userRepository.save(user);
                        return "Thay đổi mật khẩu thành công";
                    } else {
                        return "Mật khẩu mới phải khác mật khẩu cũ";
                    }
                } else {
                    return "Mật khẩu cũ không đúng";
                }
            } else {
                return "Mật khẩu xác nhận không chính xác";
            }
        } else {
            return null;
        }
    }

    @Override
    public String forgotPassword(String username) {
        User user = userRepository.findByUsername(username.toString());
        if (user != null) {
            String newPassword = utils.generateRandomPassword();
            user.setPassword(passwordEncoder().encode(newPassword));
            userRepository.save(user);
            try {
                emailUtils.sendPassword(user.getEmail(), newPassword);
            } catch (MessagingException e) {
                throw new RuntimeException("Không thể gửi otp vui lòng thử lại", e);
            }
            return "Mật khẩu đã được gửi về email đã đăng kí tài khoản này của bạn. Vui lòng kiểm tra email của bạn !!";
        } else {
            return ("Tài khoản không tồn tại");
        }
    }

    @Override
    public List<TopUserDto> findTopUser() {
        List<Object[]> results = userRepository.findTopUser();
        List<TopUserDto> userSummaries = new ArrayList<>();
        for (Object[] result : results) {
            Long id = (Long) result[0];
            String username = (String) result[1];
            Long totalOrder = (Long) result[2];
            Long totalIncome = (Long) result[3];

            TopUserDto topUserDto = new TopUserDto(id, username, totalOrder, totalIncome);
            userSummaries.add(topUserDto);
        }

        return userSummaries;
    }

}
