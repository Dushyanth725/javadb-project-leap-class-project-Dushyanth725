package com.lostfound.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lostfound.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}