package com.bankapplication.controller;

import com.bankapplication.exception.ErrorException;
import com.bankapplication.model.response.account.AccountDto;
import com.bankapplication.model.request.account.AddAccountRequestDto;
import com.bankapplication.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    @Autowired
    AccountService accountService;

    @PostMapping
    @Operation(summary = "Account oluşturur",
            description = "Account oluşturur"
    )
    public AccountDto createAccount(@RequestHeader HttpHeaders headers,
                                    @RequestBody AddAccountRequestDto request) {
        AccountDto account = accountService.createAccount(headers, request);
        return account;
    }

    @GetMapping
    @Operation(summary = "Accountları filtreler",
            description = "Accountları name ve number paramatleri ile filtreler"
    )
    public ResponseEntity<List<AccountDto>> filterAccountsByNameAndNumber(@RequestHeader HttpHeaders headers,
                                                                          @RequestParam(required = false) String name,
                                                                          @RequestParam(required = false) String number) {
        if (name != null && name.isBlank()) {
            name = null;
        }
        if (number != null && number.isBlank()) {
            number = null;
        }
        return ResponseEntity.ok(accountService.filterAccountsByNameAndNumber(headers, name, number));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Account günceller",
            description = "Account günceller"
    )
    public AccountDto updateAccount(@RequestHeader HttpHeaders headers,
                                    @RequestBody AddAccountRequestDto request,
                                    @PathVariable String id) {
        AccountDto account = accountService.updateAccount(headers, request, id);
        return account;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Account siler",
            description = "Account siler"
    )
    public String deleteAccount(@RequestHeader HttpHeaders headers, @PathVariable String id) {
        return accountService.deleteAccount(headers, id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Account getirir",
            description = "Id'ye göre account getirir"
    )
    public AccountDto getAccount(@RequestHeader HttpHeaders headers, @PathVariable String id) {
        return accountService.getAccount(headers, id);
    }

}
