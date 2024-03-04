package com.bankapplication.model.response.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class UserDto {
    private UUID id;
    private String email;
    private String password;
    private String userName;
    @JsonFormat(pattern = "yyyy-dd-MM HH:mm:ss", timezone = "Europe/Istanbul")
    private Date createdAt;
    @JsonFormat(pattern = "yyyy-dd-MM HH:mm:ss", timezone = "Europe/Istanbul")
    private Date updatedAt;
}
