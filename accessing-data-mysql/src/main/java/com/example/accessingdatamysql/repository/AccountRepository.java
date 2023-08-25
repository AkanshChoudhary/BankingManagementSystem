package com.example.accessingdatamysql.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.accessingdatamysql.model.Account;

public interface AccountRepository extends JpaRepository<Account, String> {
}