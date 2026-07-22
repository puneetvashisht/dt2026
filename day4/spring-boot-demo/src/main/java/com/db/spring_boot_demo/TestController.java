package com.db.spring_boot_demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    A a;

    @GetMapping("/test")
    public String test(){
        a.execute();
        return  "Test String - changed";
    }
}
