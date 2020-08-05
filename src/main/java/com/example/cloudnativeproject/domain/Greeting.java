package com.example.cloudnativeproject.domain;

public class Greeting {

    private final String msg;

    public Greeting(String content) {
        this.msg = content;
    }

    public String getContent() {
        return msg;
    }
}
