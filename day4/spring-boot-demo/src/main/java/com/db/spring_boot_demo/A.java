package com.db.spring_boot_demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class A {

    @Value("XYZ Corp")
    String companyName;

    @Autowired
    B b;

    public void execute(){
        System.out.println("Execute in A" + this.companyName);
        b.execute();
    }
}
