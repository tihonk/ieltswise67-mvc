package com.ietswise.controllers;

import com.ietswise.entity.Student;
import com.ietswise.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("/test")
    public String test() {
        Student student = new Student(2, new Date());
        studentRepository.save(student);
        return "Hello";
    }
}
