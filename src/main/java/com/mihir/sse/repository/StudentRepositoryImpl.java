package com.mihir.sse.repository;

import com.mihir.sse.domain.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;
import java.util.Map;

public class StudentRepositoryImpl implements StudentRepository{

    private static final String KEY = "Student";

    private RedisTemplate<String, Student> redisTemplate;

    private HashOperations hashOps;

    @Autowired
    public StudentRepositoryImpl(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init() {
        hashOps = redisTemplate.opsForHash();
    }

    public void saveStudent(Student student) {
        hashOps.put(KEY, student.getId(), student);
    }

    public void updateStudent(Student student) {
        hashOps.put(KEY, student.getId(), student);
    }

    public Student findStudent(String id) {
        return (Student) hashOps.get(KEY, id);
    }

    public Map<Object, Object> findAllStudents() {
        return hashOps.entries(KEY);
    }

    public void deleteStudent(String id) {
        hashOps.delete(KEY, id);
    }
}
