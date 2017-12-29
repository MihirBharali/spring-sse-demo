package com.mihir.sse.controllers;

import com.mihir.sse.domain.Student;
import com.mihir.sse.processors.StudentDataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class StudentController {
    @Autowired
    private StudentDataManager studentDataManager;

    @GetMapping("/save/{id}/{name}/{gender}/{grade}")
    @ResponseBody
    public String save(@PathVariable String id,
                       @PathVariable String name,
                       @PathVariable String gender,
                       @PathVariable int grade) {

        try {
            Student student = new Student(name, gender, id, grade);
            studentDataManager.saveStudent(student);
        } catch (IllegalArgumentException iae) {
            return "FAILURE : Invalid data";
        }
        return "SUCCESS";
    }


    @GetMapping("/get/{id}")
    @ResponseBody
    public Student get(@PathVariable String id) {
        return studentDataManager.getStudent(id);
    }

}
