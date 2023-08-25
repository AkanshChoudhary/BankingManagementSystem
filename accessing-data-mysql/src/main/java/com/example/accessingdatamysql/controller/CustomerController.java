package com.example.accessingdatamysql.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.*;

import com.example.accessingdatamysql.model.Account;
import com.example.accessingdatamysql.model.Customer;
import com.example.accessingdatamysql.service.AccountService;
import com.example.accessingdatamysql.service.CustomerService;

import java.util.Map;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
	
	 @Autowired
	    private CustomerService customerService;

	    @PostMapping("/create")
	    public Customer createCustomer(@RequestBody Map<String, Object>  customer) {
	    	Customer c =  new Customer();
	    	String id = (String)customer.get("id");
	    	int age = (Integer)customer.get("age");
	        String location = (String)customer.get("location");
	        String name = (String)customer.get("name");
	        String password = (String)customer.get("password");
	        if(age>18 && location.length()>0 && name.length()>0&&password.length()>8&&id.length()>0) {
	        	c.setAge(age);
		    	c.setIsLoggedIn(false);
		    	c.setLocation(location);
		    	c.setPassword(password);
		    	c.setId(id);
		    	System.out.println("customer created");
		        return customerService.createCustomer(c);
	        }else {
	        	throw new RuntimeException("Age>18 is needed and Name, Location cannot be empty\n Password minimum 8 characters");
	        }
	    	
	    }

	    @GetMapping("/{id}")
	    public Customer getCustomer(@PathVariable String id) {
	        return customerService.getCustomer(id).orElseThrow(() -> new RuntimeException("Customer not found"));
	    
	    }
	    @PostMapping("/{id}")
	    public void deleteCustomer(@PathVariable String id) {
	        customerService.deleteCustomer(id);
	    }

	    @PostMapping("/{id}/update")
	    public void updateCustomer(@PathVariable String id, @RequestBody Map<String, Object> customer) {
	        int age = (Integer)customer.get("age");
	        boolean isLoggedIn = (Boolean)customer.get("isLoggedIn");
	        String location = (String)customer.get("location");
	        String name = (String)customer.get("name");
	    	Customer c =  customerService.getCustomer(id).orElseThrow(() -> new RuntimeException("Customer not found"));
	    	if(age>18 && location.length()>0 && name.length()>0) {
		        c.setAge(age);
		        c.setIsLoggedIn(isLoggedIn);
		        c.setLocation(location);
		        c.setName(name);
		        customerService.updateCustomer(id,c);
	    	}else {
	    		throw new RuntimeException("Age>18 is needed and Name, Location cannot be empty");
	    	}
	    }
	    
	    @PostMapping("/login")
	    public void loginCustomer(@RequestBody Map<String, Object> authDetail) {
	    	Customer c =  customerService.getCustomer((String)authDetail.get("id")).orElseThrow(() -> new RuntimeException("Customer not found"));
	    	if(c.getPassword().equals((String)authDetail.get("password"))) {
	    		c.setIsLoggedIn(true);
	    		customerService.updateCustomer(c.getId(), c);
	    	}else {
	    		throw new RuntimeException("Customer Password Wrong");
	    	}
	    }
	    
	    @PostMapping("/logout")
	    public void logoutCustomer(@RequestBody Map<String, Object> authDetail) {
	    	Customer c =  customerService.getCustomer((String)authDetail.get("id")).orElseThrow(() -> new RuntimeException("Customer not found"));
	    	c.setIsLoggedIn(false);
	    	customerService.updateCustomer(c.getId(), c);
	    }
}
