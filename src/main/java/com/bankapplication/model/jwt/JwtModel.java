package com.bankapplication.model.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtModel {

    private String sub;
    private String id;
    private String userName;
    private String email;
    private String exp;

}
