package com.bankapplication.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorRes {
    HttpStatus httpStatus;
    String message;
}
