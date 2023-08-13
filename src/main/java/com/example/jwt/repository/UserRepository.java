package com.example.jwt.repository;

import com.example.jwt.entitiy.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    public User findByUsername(String username);
}
