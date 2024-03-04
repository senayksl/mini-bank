package com.bankapplication.model.response;

import com.bankapplication.enums.TransactionStatusEnum;
import com.bankapplication.model.Account;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {

    private Long id;
    private String fromNumber;
    private String toNumber;
    private BigDecimal amount;
    @JsonFormat(pattern = "yyyy-dd-MM HH:mm:ss", timezone = "Europe/Istanbul")
    private LocalDateTime transactionDate;
    private TransactionStatusEnum status;

}
