package com.bankapplication.service;

import com.bankapplication.model.Account;
import com.bankapplication.model.Transaction;
import com.bankapplication.model.User;
import com.bankapplication.model.response.account.AccountDto;
import com.bankapplication.model.request.account.AddAccountRequestDto;
import com.bankapplication.repository.AccountRepository;
import com.bankapplication.exception.ErrorException;
import com.bankapplication.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    @Autowired
    UserService userService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    public AccountDto createAccount(HttpHeaders headers, AddAccountRequestDto request) {
        User user = userService.getUserIdFromToken(headers);
        UUID uuid = UUID.randomUUID();
        // UUID'yi alfanümerik bir dize olarak döndür
        String number = uuid.toString().replace("-", "");
        if (accountRepository.existsByUserAndName(user, request.getName())) {
            throw new ErrorException("Account with this name already exists");
        }
        Account account = Account.builder()
                .name(request.getName())
                .user(user)
                .balance(BigDecimal.valueOf(1000.0))
                .number(number)
                .build();
        Account savedAccount = accountRepository.save(account);
        AccountDto accountDto = AccountDto.builder()
                .id(savedAccount.getId())
                .name(savedAccount.getName())
                .number(savedAccount.getNumber())
                .balance(savedAccount.getBalance().toString())
                .createdAt(savedAccount.getCreatedAt())
                .updatedAt(savedAccount.getUpdatedAt())
                .build();
        return accountDto;
    }

    public List<AccountDto> filterAccountsByNameAndNumber(HttpHeaders headers, String name, String number) {
        User user = userService.getUserIdFromToken(headers);
        List<Account> accounts = new ArrayList<>();
        if (name != null || number != null) {
            accounts = accountRepository.findAllByNameAndUserOrNumberAndUser(name, user, number, user);
        }else{
            accounts = accountRepository.findAllByUser(user);
        }

        if (accounts.size() == 0) {
            throw new ErrorException("Account not found");
        }
        List<AccountDto> accountDtos = accounts.stream().map(account -> AccountDto.builder()
                .id(account.getId())
                .name(account.getName())
                .number(account.getNumber())
                .balance(account.getBalance().toString())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build()).collect(Collectors.toList());
        return accountDtos;
    }

    public AccountDto updateAccount(HttpHeaders headers, AddAccountRequestDto request, String id) {
        UUID idUuid = UUID.fromString(id);
        User user = userService.getUserIdFromToken(headers);
        Account account = accountRepository.findByUserAndId(user,idUuid);
        if (account == null) {
            throw new ErrorException("Account not found");
        }
        if (!account.getUser().getId().equals(user.getId())) {
            throw new ErrorException("You are not authorized to update this account");
        }if (accountRepository.existsByUserAndName(user, request.getName())) {
            throw new ErrorException("Account with this name already exists");
        }
        account.setName(request.getName());
        Account savedAccount = accountRepository.save(account);
        AccountDto accountDto = AccountDto.builder()
                .id(savedAccount.getId())
                .name(savedAccount.getName())
                .number(savedAccount.getNumber())
                .balance(savedAccount.getBalance().toString())
                .createdAt(savedAccount.getCreatedAt())
                .updatedAt(savedAccount.getUpdatedAt())
                .build();
        return accountDto;
    }

    public Account getAccountByNumber(String number) {
        Account account = accountRepository.findByNumber(number);
        return account;
    }

    public String deleteAccount(HttpHeaders headers, String id) {
        User user = userService.getUserIdFromToken(headers);
        UUID idUuid = UUID.fromString(id);
        Account account = accountRepository.findByUserAndId(user,idUuid);
        if (account == null) {
            throw new ErrorException("Account not found");
        }
        if (!account.getUser().getId().equals(user.getId())) {
            throw new ErrorException("You are not authorized to delete this account");
        }
        List<Transaction> transactions = transactionRepository.findAllByFromOrTo(account, account);
        if (transactions.size() > 0) {
            transactionRepository.deleteAll(transactions);
        }
        accountRepository.delete(account);
        return "Account deleted successfully";
    }

    public AccountDto getAccount(HttpHeaders headers, String id) {
        User user = userService.getUserIdFromToken(headers);
        UUID idUuid = UUID.fromString(id);
        Account account = getAccountById(idUuid);
        if (account == null) {
            throw new ErrorException("Account not found");
        }if (!account.getUser().getId().equals(user.getId())) {
            throw new ErrorException("You are not authorized to view this account");
        }
        AccountDto accountDto = AccountDto.builder()
                .id(account.getId())
                .name(account.getName())
                .number(account.getNumber())
                .balance(account.getBalance().toString())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
        return accountDto;
    }

    public Account getAccountById(UUID idUuid) {
        Account account = accountRepository.findById(idUuid);
        return account;
    }


}
