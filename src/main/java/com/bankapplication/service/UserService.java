package com.bankapplication.service;

import com.bankapplication.exception.ErrorException;
import com.bankapplication.model.User;
import com.bankapplication.model.jwt.JwtModel;
import com.bankapplication.model.response.user.UserDto;
import com.bankapplication.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public ResponseEntity addUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new ErrorException("Bu Mail ile kayıtlı kullanıcı bulunmaktadır.");
        }
        User user = new User();
        user.setUserName(userDto.getUserName());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        User savedUser = userRepository.save(user);
        UserDto savedUserDto = UserDto.builder()
                .id(savedUser.getId())
                .userName(savedUser.getUserName())
                .email(savedUser.getEmail())
                .password(savedUser.getPassword())
                .createdAt(savedUser.getCreatedAt())
                .updatedAt(savedUser.getUpdatedAt())
                .build();
        return ResponseEntity.ok(savedUserDto);
    }

    public User getUserIdFromToken(HttpHeaders headers) {
        String token = headers.get("Authorization").get(0);
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String jsonStringText = new String(decoder.decode(chunks[1]));
        JwtModel jsonData = new JwtModel();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            jsonData = objectMapper.readValue(jsonStringText, JwtModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Java nesnesinden ID'yi al
        String id = jsonData.getId();
        UUID uuid = UUID.fromString(id);
        User user = userRepository.findById(uuid);
        return user;
    }

}
