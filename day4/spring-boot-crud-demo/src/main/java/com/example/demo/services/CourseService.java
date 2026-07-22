package com.example.demo.services;

import com.example.demo.entities.Course;
import com.example.demo.repos.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    CourseRepository courseRepository;
    public List<Course> fetchAllCourses(){
        // logic
        return courseRepository.findAll();
    }

    public int add(int x, int y){
        return x+y;
    }

}
