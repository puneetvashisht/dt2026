package com.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class A {
    B b;

    @Autowired
    public void setB(B b) {
        this.b = b;
    }

    public void execute(){
        System.out.println("Execute in A" );
        b.execute();
    }
}
