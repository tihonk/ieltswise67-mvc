package com.ietswise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    StudentController() {
    }

    @GetMapping("/test")
    public String test() {
        return "Hello";
    }
}
