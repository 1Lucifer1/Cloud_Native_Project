package com.example.cloudnativeproject.controller;

import com.example.cloudnativeproject.service.GreetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    @Autowired
    private GreetingService greetingService;

    @GetMapping("/greeting")
    public Object greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return greetingService.greeting();
    }
}
