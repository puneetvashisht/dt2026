package com.example.demo.repos;

import com.example.demo.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    Course findByTitle(String title);

    // aggregate 

}
