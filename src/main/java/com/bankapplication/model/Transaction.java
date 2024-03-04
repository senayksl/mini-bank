package com.bankapplication.model;

import com.bankapplication.enums.TransactionStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Builder
@Table(name = "transaction")
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "from_account_id", referencedColumnName = "id")
    private Account from;
    @ManyToOne
    @JoinColumn(name = "to_account_id", referencedColumnName = "id")
    private Account to;
    private BigDecimal amount;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "transcation_date", nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-dd-MM HH:mm:ss", timezone = "Europe/Istanbul")
    private LocalDateTime transactionDate;
    @Enumerated(EnumType.STRING)
    private TransactionStatusEnum status;
}
