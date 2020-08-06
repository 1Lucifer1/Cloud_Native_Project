package com.example.cloudnativeproject.controller.limit;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.TOO_MANY_REQUESTS)
public class RequestLimitException extends Exception{

}
