package com.db.spring_data_jpa_boot_demo.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.db.spring_data_jpa_boot_demo.entities.Employee;

public interface EmployeeRespository extends JpaRepository<Employee, Integer> {
    
}
