package com.bankapplication.model.response.account;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
    private UUID id;
    private String name;
    private String number;
    private String balance;
    @JsonFormat(pattern = "yyyy-dd-MM HH:mm:ss", timezone = "Europe/Istanbul")
    private Date createdAt;
    @JsonFormat(pattern = "yyyy-dd-MM HH:mm:ss", timezone = "Europe/Istanbul")
    private Date updatedAt;

}
