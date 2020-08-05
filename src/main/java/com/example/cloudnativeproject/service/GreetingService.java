package com.example.cloudnativeproject.service;

import com.example.cloudnativeproject.domain.Greeting;
import org.springframework.stereotype.Service;

@Service
public class GreetingService {
    public Object greeting(){
        return new Greeting("Hello");
    }
}
