package com.fitnessapp.security;
 
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
 
import java.util.HashMap;
import java.util.Map;
 
@ControllerAdvice
public class ExceptionHandler {
 
    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    Map<String,String> showCustomMessage(Exception e){
 
        Map<String,String> response = new HashMap<>();
        System.out.println(e.toString());
        response.put("Error", "Bad Request");
 
        return response;
    }
}