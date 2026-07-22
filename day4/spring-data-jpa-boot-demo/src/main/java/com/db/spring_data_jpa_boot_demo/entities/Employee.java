package com.db.spring_data_jpa_boot_demo.entities;

// import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Employee {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    String name;
    double salary;
    String department;
}
