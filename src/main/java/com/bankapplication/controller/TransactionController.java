package com.bankapplication.controller;

import com.bankapplication.model.Transaction;
import com.bankapplication.model.request.transaction.TransactionRequestDto;
import com.bankapplication.model.response.TransactionDto;
import com.bankapplication.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    @Autowired
    TransactionService transactionService;


    @PostMapping("/transfer")
    @Operation(summary = "Para transferi yapar",
            description = "Para transferi yapar"
    )
    public TransactionDto transfer(@RequestHeader HttpHeaders headers, @RequestBody TransactionRequestDto request) {
        TransactionDto transaction = transactionService.transfer(headers,request);
        return transaction;
    }


    @GetMapping("/account/{accountId}")
    @Operation(summary = "Account Id'ye göre transactionları getirir",
            description = "Account Id'ye göre transactionları getirir"
    )
    public List<TransactionDto> getTransactionsByAccountId(@RequestHeader HttpHeaders headers, @PathVariable String accountId) {
        List<TransactionDto> transactionDtos = transactionService.getTransactionsByAccountId(headers,accountId);
        return transactionDtos;
    }
}
