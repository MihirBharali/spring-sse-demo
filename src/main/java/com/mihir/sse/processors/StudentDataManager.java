package com.mihir.sse.processors;

import com.mihir.sse.domain.Student;
import com.mihir.sse.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class StudentDataManager {

    @Autowired
    private StudentRepository studentRepository;

    public void saveStudent(Student student) {
        studentRepository.saveStudent(student);
    }

    public Student getStudent(String id) {
        return studentRepository.findStudent(id);
    }

    public void updateStudentName(String id, String name) {
        Student retrievedStudent = getStudent(id);
        retrievedStudent.setName(name);
        studentRepository.updateStudent(retrievedStudent);
    }

    public void deleteStudent(String id) {
        studentRepository.deleteStudent(id);
    }
}
