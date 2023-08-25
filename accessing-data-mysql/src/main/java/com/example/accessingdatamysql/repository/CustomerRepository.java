package com.example.accessingdatamysql.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.accessingdatamysql.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, String> {
}