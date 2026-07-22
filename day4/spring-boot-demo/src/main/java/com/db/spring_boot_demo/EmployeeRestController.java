package com.db.spring_boot_demo;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class EmployeeRestController {

    Logger logger = LoggerFactory.getLogger(EmployeeRestController.class);
    List<Employee> employees = new ArrayList<>();
    {
        employees.add(new Employee(12,"Ravi", 34343.34));
        employees.add(new Employee(2,"Riya", 44343.34));
    }
    @GetMapping("/employees")
    public List<Employee> fetchAllEmployees(){
        logger.warn(employees.toString());
        logger.debug("ONly debug");
        logger.error("Only error");
        return employees;
    }

    @PostMapping("/employees")
    public void addEmployee(@RequestBody Employee employee){
        System.out.println(employee);
        employees.add(employee);
    }
}
