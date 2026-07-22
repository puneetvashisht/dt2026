package com.example.demo.controllers;

import com.example.demo.entities.Course;
import com.example.demo.repos.CourseRepository;
import com.example.demo.services.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@CrossOrigin
//@RequestMapping("/api/v1/courses")
@RequestMapping("/api/v1/courses")
public class CourseController {

//    @Autowired
//    Logger logger;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    CourseService courseService;

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public void addCourse(@RequestBody Course course){
        // data base logic
//        logger.debug("Course recieved " + course);
        System.out.println(course);
        courseRepository.save(course);
    }
    @GetMapping("/")
    public List<Course> fetchAllCourses(){
        return courseService.fetchAllCourses();
    }

    @GetMapping("/{id}")
    public Course fetchCourse(@PathVariable("id") int id){
        Optional<Course> courseFound = courseRepository.findById(id);
        if(courseFound.isPresent()){
            return courseFound.get();
        }
        else{
            throw new CourseNotFoundException(" Course with id " + id + "not found!");
        }
    }


    @GetMapping("/search")
    public Course fetchCourseByTitle(@RequestParam("title") String title){
        return courseRepository.findByTitle(title);
    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable("id") int id) {
        try {
            courseRepository.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CourseNotFoundException(" Course with id " + id + "not found!");
        }

    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCourseTitle(@PathVariable("id") int id, @RequestBody Course course) {
        Optional<Course> courseFound = courseRepository.findById(id);
        if(courseFound.isPresent()){
            Course c = courseFound.get();
            c.setTitle(course.getTitle());
            c.setPrice(course.getPrice());
            courseRepository.save(c);
        }
        else{
            throw new CourseNotFoundException(" Course with id " + id + "not found!");
        }

    }
}
