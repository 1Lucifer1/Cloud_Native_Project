package com.example.cloudnativeproject.controller;

import com.example.cloudnativeproject.controller.limit.RequestLimit;
import com.example.cloudnativeproject.service.GreetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    @Autowired
    private GreetingService greetingService;

    @GetMapping("/greeting")
    @RequestLimit(count=100)
    public Object greeting() {
        return greetingService.greeting();
    }
}
