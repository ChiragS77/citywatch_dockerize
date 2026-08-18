package com.userservice.service;

import com.userservice.dto.*;
import com.userservice.entity.Role;
import com.userservice.entity.User;
import com.userservice.entity.WorkType;
import com.userservice.entity.Worker;
import com.userservice.repo.UserRepository;
import com.userservice.repo.WorkerRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtService jwtService;
    private final WorkerRepository workerRepository;


    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;

    public MessageResponse register(RegisterReq req){

        try{
            User user = new User();

            user.setName(req.getName());
            user.setEmail(req.getEmail());

            user.setRole(req.getRole() != null ? req.getRole() : Role.USER);

            user.setPassword(passwordEncoder.encode(req.getPassword()));

            user.setState(req.getState());
            user.setDistrict(req.getDistrict());
            user.setTaluka(req.getTaluka());
            user.setWardNo(req.getWardNo());
            user.setDob(req.getDob());

            userRepository.save(user);

            // 🔥 IMPORTANT PART
            if (user.getRole() == Role.WORKER) {

                Worker worker = new Worker();
                worker.setUser(user);

                // default values
                worker.setAvailable(true);
                worker.setRating(0.0);

                // ⚠️ You don't have workType here → so set later OR pass in request
//                worker.setWorkType(WorkType.ELECTRICIAN); // temporary

                workerRepository.save(worker);
            }

            return new MessageResponse("Registered successfully");

        } catch (Exception e){
            return new MessageResponse("Registration failed");
        }
    }


    public ResponseEntity<?> login(LoginReq loginReq, HttpServletResponse response) {

        // 1️⃣ Validate input
        if (loginReq.getEmail() == null || loginReq.getPassword() == null) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", "Email and password are required"
            ));
        }

        // 2️⃣ Find user
        Optional<User> userOptional = userRepository.findByEmail(loginReq.getEmail());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "message", "Invalid email or password"
            ));
        }

        User user = userOptional.get();

        // 3️⃣ Check password
        if (!passwordEncoder.matches(loginReq.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "message", "Invalid email or password"
            ));
        }

        // 4️⃣ Set authentication
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                null,
                user.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 5️⃣ Generate JWT
        String jwt = jwtService.generateToken(user);

        // 6️⃣ Cookie
        Cookie cookie = new Cookie("jwt", jwt);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // HTTPS only
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);

        response.addCookie(cookie);
        LoginResponse res = new LoginResponse();

        res.setMessage("Login successful");

        res.setId(user.getId());
        res.setName(user.getName());
        res.setEmail(user.getEmail());
        res.setRole(user.getRole());

        res.setProfileImage(user.getProfileImage());

        res.setState(user.getState());
        res.setDistrict(user.getDistrict());
        res.setTaluka(user.getTaluka());
        res.setWardNo(user.getWardNo());

        res.setDob(user.getDob());
        res.setAge(user.getAge());


        // 7️⃣ Response
        return ResponseEntity.ok(res);
    }


    public String uploadProfileImage(String username, MultipartFile file) {

        User user = this.userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String imageUrl = this.cloudinaryService.uploadImage(file); // reuse same service

        user.setProfileImage(imageUrl);

        this.userRepository.save(user);

        return imageUrl;
    }


    public UserProfileResponse getProfile(String email) {

        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserProfileResponse res = new UserProfileResponse();

        res.setName(user.getName()); // 🔥 ADD THIS
        res.setEmail(user.getEmail());
        res.setRole(user.getRole());

        res.setWardNo(user.getWardNo());
        res.setTaluka(user.getTaluka());
        res.setDistrict(user.getDistrict());

        res.setProfileImage(user.getProfileImage()); // 🔥 IMPORTANT

        return res;
    }

    public UserProfileResponse getNagarsevak(Integer wardNo,
                                             String taluka,
                                             String district) {

        if (wardNo == null || taluka == null || district == null) {
            return null;
        }

        Optional<User> optionalUser =
                userRepository.findByRoleAndWardNoAndTalukaAndDistrict(
                        Role.NAGARSEVAK,
                        wardNo,
                        taluka,
                        district
                );

        if (optionalUser.isEmpty()) {
            return null; // ✅ safe
        }

        User user = optionalUser.get();

        UserProfileResponse res = new UserProfileResponse();
        res.setName(user.getName());
        res.setEmail(user.getEmail());
        res.setRole(user.getRole());
        res.setWardNo(user.getWardNo());
        res.setTaluka(user.getTaluka());
        res.setDistrict(user.getDistrict());
        res.setProfileImage(user.getProfileImage());

        return res;
    }

    public UserProfileResponse updateProfile(String email, UpdateProfileRequest req) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ update only if not null (important)
        if (req.getName() != null) user.setName(req.getName());
        if (req.getState() != null) user.setState(req.getState());
        if (req.getDistrict() != null) user.setDistrict(req.getDistrict());
        if (req.getTaluka() != null) user.setTaluka(req.getTaluka());
        if (req.getWardNo() != null) user.setWardNo(req.getWardNo());

        userRepository.save(user);

        // ✅ return updated response
        return getProfile(email);
    }


}
