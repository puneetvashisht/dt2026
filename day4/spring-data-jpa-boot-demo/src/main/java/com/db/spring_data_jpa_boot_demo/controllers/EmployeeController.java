package com.db.spring_data_jpa_boot_demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.db.spring_data_jpa_boot_demo.entities.Employee;
import com.db.spring_data_jpa_boot_demo.repos.EmployeeRespository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RequestMapping("/api/v1/employees")
@RestController
public class EmployeeController {

    @Autowired
    EmployeeRespository employeeRespository;
    
    @GetMapping("/")
    public List<Employee> getEmployees() {
        return employeeRespository.findAll();
    }

    @PostMapping("/")
    public void addEmployee(@RequestBody Employee employee) {
        employeeRespository.save(employee);
    }   
    
}
