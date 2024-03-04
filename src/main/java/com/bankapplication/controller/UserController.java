package com.bankapplication.controller;

import com.bankapplication.model.User;
import com.bankapplication.model.request.user.LoginReq;
import com.bankapplication.model.response.user.UserDto;
import com.bankapplication.model.response.ErrorRes;
import com.bankapplication.model.response.LoginRes;
import com.bankapplication.repository.UserRepository;
import com.bankapplication.security.JwtUtil;
import com.bankapplication.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    UserService userService;

    private final AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;


    private final JwtUtil jwtUtil;

    @PostMapping("register")
    @Operation(summary = "Register servisi",
            description = "Kullanıcı kaydı yapar"
    )
    public ResponseEntity<?> addUser (@RequestBody UserDto request)throws Exception{
        ResponseEntity<UserDto> userDto = userService.addUser(request);
        return userDto;
    }

    @ResponseBody
    @PostMapping("/login")
    @Operation(summary = "Login servisi",
            description = "Kullanıcı girişi yapar ve token döner"
    )
    public ResponseEntity login(@RequestBody LoginReq loginReq) {

        try {
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginReq.getEmail(), loginReq.getPassword()));
            String email = authentication.getName();
            User user = userRepository.findByEmail(email);
            String token = jwtUtil.createToken(user);
            LoginRes loginRes = LoginRes.builder()
                    .token(token)
                    .email(email)
                    .userName(user.getUserName())
                    .id(user.getId())
                    .build();

            return ResponseEntity.ok(loginRes);

        } catch (BadCredentialsException e) {
            ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST, "Invalid username or password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping
    @Operation(summary = "User'ı döndürür",
            description = "Login olan kullanıcının bilgilerini döndürür"
    )
    public User getUser(@RequestHeader HttpHeaders headers) {
        User user = userService.getUserIdFromToken(headers);
        return user;
    }

}
