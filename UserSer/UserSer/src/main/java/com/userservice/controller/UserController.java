package com.userservice.controller;

import com.userservice.dto.*;
import com.userservice.entity.Role;
import com.userservice.entity.User;
import com.userservice.repo.UserRepository;
import com.userservice.service.PasswordResetService;
import com.userservice.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final  UserRepository userRepository;

    private final PasswordResetService passwordResetService;



    @PostMapping("/register")
    public ResponseEntity<MessageResponse> registerUser(@RequestBody RegisterReq req) {

        if (userRepository.existsByEmail(req.getEmail())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new MessageResponse("Email already registered"));
        }

        MessageResponse response = userService.register(req);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/profile/image")
    public String uploadProfileImage(
            @RequestParam("image") MultipartFile file,
            HttpServletRequest request) {

        String username = (String) request.getAttribute("username");

        return userService.uploadProfileImage(username, file);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginReq loginReq, HttpServletResponse response){
        return this.userService.login(loginReq,response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        System.out.println("Enter into method....");
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok(new MessageResponse("Logged out successfully"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<MessageResponse> forgotPassword(
            @RequestBody ForgotPasswordRequest request
    ) {
        return ResponseEntity.ok(passwordResetService.forgotPassword(request));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponse> resetPassword(
            @RequestBody ResetPasswordRequest request
    ) {
        return ResponseEntity.ok(passwordResetService.resetPassword(request));
    }


    @GetMapping("/check-email")
    public Map<String, Boolean> checkEmail(@RequestParam String email) {

        boolean exists = userRepository.existsByEmail(email);

        return Map.of("exists", exists);
    }


    @GetMapping
    public List<String> getUsers() {
        return List.of("Amit", "Rahul", "Priya");
    }

    @GetMapping("/profile")
    public UserProfileResponse getProfile(HttpServletRequest request) {

        String email = (String) request.getAttribute("username"); // ✅ FIXED

        return userService.getProfile(email);
    }


    @GetMapping("/nagarsevak")
    public UserProfileResponse getNagarsevak(HttpServletRequest request) {

        Integer wardNo = (Integer) request.getAttribute("wardNo");
        String taluka = (String) request.getAttribute("taluka");
        String district = (String) request.getAttribute("district");

        return userService.getNagarsevak(wardNo, taluka, district);
    }

    @PutMapping("/update-profile")
    public UserProfileResponse updateProfile(
            @RequestBody UpdateProfileRequest req,
            HttpServletRequest request) {

        String username = (String) request.getAttribute("username");

        return userService.updateProfile(username, req);
    }

    @GetMapping("/users")
    public List<User> getUsers(HttpServletRequest request) {

        Integer wardNo = (Integer) request.getAttribute("wardNo");
        String taluka = (String) request.getAttribute("taluka");
        String district = (String) request.getAttribute("district");

        return userRepository.findByWardNoAndTalukaAndDistrict(
                wardNo, taluka, district
        );
    }

    @GetMapping("/nagarsevak/{wardNo}")
    public List<String> getNagarsevakEmails(@PathVariable Integer wardNo) {

        System.out.println("Enter into email.....");
        return userRepository.findByRoleAndWardNo(Role.NAGARSEVAK, wardNo)
                .stream()
                .map(User::getEmail)
                .toList();
    }




}
