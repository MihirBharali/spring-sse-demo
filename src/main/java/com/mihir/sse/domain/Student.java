package com.mihir.sse.domain;


import java.io.Serializable;

public class Student implements Serializable {

    public enum Gender {
        MALE,
        FEMALE;
    }

    private String name;
    private Gender gender;
    private String id;
    private int grade;

    public Student(String name, String gender, String id, int grade) {
        this.name = name;
        this.gender = resolveGender(gender);
        this.id = id;
        this.grade = grade;
    }

    private Gender resolveGender(String gender) {
        if(gender.equalsIgnoreCase("male")) {
            return Gender.MALE;
        }
        if(gender.equalsIgnoreCase("female")) {
            return Gender.FEMALE;
        }
        throw new IllegalArgumentException("Invalid gender");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
