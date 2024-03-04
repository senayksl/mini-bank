package com.bankapplication.repository;

import com.bankapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail (String email);

    Boolean existsByEmail (String email);

    User findById(UUID id);
}
