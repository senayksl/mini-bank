package com.bankapplication.service;

import com.bankapplication.enums.TransactionStatusEnum;
import com.bankapplication.exception.ErrorException;
import com.bankapplication.model.Account;
import com.bankapplication.model.Transaction;
import com.bankapplication.model.User;
import com.bankapplication.model.request.transaction.TransactionRequestDto;
import com.bankapplication.model.response.TransactionDto;
import com.bankapplication.repository.AccountRepository;
import com.bankapplication.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    @Autowired
    UserService userService;

    @Autowired
    AccountService accountService;
    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    AccountRepository accountRepository;

    public List<TransactionDto> getTransactionsByAccountId(HttpHeaders headers, String accountId) {
        UUID idUuid = UUID.fromString(accountId);
        Account account = accountService.getAccountById(idUuid);
        User user = userService.getUserIdFromToken(headers);
        if (account == null) {
            throw new ErrorException("Account not found");
        } else if (account.getUser().getId() != user.getId()) {
            throw new ErrorException("You are not authorized to view this account");
        }
        List<Transaction> transactions = transactionRepository.findAllByFromOrTo(account, account);
        List<TransactionDto> transactionDtos = transactions.stream().map(transaction -> {
            Boolean from = false;
            if (transaction.getFrom().getId().equals(account.getId())) {
                from = true;
            }
            TransactionDto transactionDto = TransactionDto.builder()
                    .id(transaction.getId())
                    .fromNumber(transaction.getFrom().getNumber())
                    .toNumber(transaction.getTo().getNumber())
                    .amount(from ? transaction.getAmount().negate() : transaction.getAmount())
                    .status(transaction.getStatus())
                    .transactionDate(transaction.getTransactionDate())
                    .build();
            return transactionDto;
        }).collect(Collectors.toList());
        return transactionDtos;
    }

    public TransactionDto transfer(HttpHeaders headers, TransactionRequestDto request) {
        Transaction transaction = new Transaction();
        User user = userService.getUserIdFromToken(headers);
        Account accountFrom = accountService.getAccountByNumber(request.getFromAccountNumber());
        if (accountFrom == null) {
            throw new ErrorException("From Account not found");
        } else if (accountFrom.getUser().getId() != user.getId()) {
            throw new ErrorException("You are not authorized to view this account");
        }
        Account accountTo = accountService.getAccountByNumber(request.getToAccountNumber());
        if (accountTo == null) {
            throw new ErrorException("To Account not found");
        }
        if (accountFrom.getBalance().compareTo(request.getAmount()) < 0) {
            saveTransaction(request, accountFrom, accountTo, TransactionStatusEnum.FAILED);
            throw new ErrorException("Insufficient balance");
        }
        if (accountFrom.getNumber().equals(accountTo.getNumber())) {
            saveTransaction(request, accountFrom, accountTo, TransactionStatusEnum.FAILED);
            throw new ErrorException("You cannot transfer to the same account");
        }
        if (request.getAmount().compareTo(BigDecimal.valueOf(0.0)) <= 0) {
            saveTransaction(request, accountFrom, accountTo, TransactionStatusEnum.FAILED);
            throw new ErrorException("Amount must be greater than 0");
        } else {
            accountFrom.setBalance(accountFrom.getBalance().subtract(request.getAmount()));
            accountTo.setBalance(accountTo.getBalance().add(request.getAmount()));
            Account updatedAccountFrom = accountRepository.save(accountFrom);
            Account updatedAccountTo = accountRepository.save(accountTo);
            transaction = saveTransaction(request, accountFrom, accountTo, TransactionStatusEnum.SUCCESS);
            transaction.setFrom(updatedAccountFrom);
            transaction.setTo(updatedAccountTo);
        }
        TransactionDto transactionDto = TransactionDto.builder()
                .id(transaction.getId())
                .fromNumber(transaction.getFrom().getNumber())
                .toNumber(transaction.getTo().getNumber())
                .amount(transaction.getAmount())
                .status(transaction.getStatus())
                .transactionDate(transaction.getTransactionDate())
                .build();
        return transactionDto;
    }

    private Transaction saveTransaction(TransactionRequestDto request, Account accountFrom, Account accountTo, TransactionStatusEnum status) {
        Transaction transaction = Transaction.builder()
                .from(accountFrom)
                .to(accountTo)
                .amount(request.getAmount())
                .status(status)
                .build();
        Transaction savedTransaction = transactionRepository.save(transaction);
        return savedTransaction;
    }

}
