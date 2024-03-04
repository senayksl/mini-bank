package com.bankapplication.repository;

import com.bankapplication.model.Account;
import com.bankapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, Long> {


    List<Account> findAllByNameAndUserOrNumberAndUser(String name, User user1, String number, User user2);

    List<Account> findAllByUser(User user);

    Account findByNumber(String number);


    boolean existsByUserAndName(User user, String name);

    Account findByUserAndNumber(User user, String number);
    Account findByUserAndId(User user, UUID id);

    Account findById(UUID id);
}
