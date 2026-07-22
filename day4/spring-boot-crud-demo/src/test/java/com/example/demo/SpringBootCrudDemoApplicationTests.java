package com.example.demo;

import com.example.demo.entities.Course;
import com.example.demo.repos.CourseRepository;
import com.example.demo.services.CourseService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class SpringBootCrudDemoApplicationTests {

	@Mock
	CourseRepository courseRepository;

	@InjectMocks
	CourseService courseService;
	@Test
	public void testFetchAllCourses(){

		List<Course> courses = new ArrayList<>();
		courses.add(new Course("Angular", 34434.34));
		courses.add(new Course("React", 44434.34));


		when(courseRepository.findAll()).thenReturn(courses);
		List<Course> returnedResult = courseService.fetchAllCourses();
		verify(courseRepository).findAll();
		assertEquals(2, returnedResult.size());
	}

//	@Autowired
//	CourseService courseService12;
//	@Test
//	public void testCourseService(){
//		List<Course> courses = courseService12.fetchAllCourses();
//		assertEquals(2, courses.size());
//	}

}
