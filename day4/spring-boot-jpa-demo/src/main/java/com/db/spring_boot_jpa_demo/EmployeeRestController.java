package com.db.spring_boot_jpa_demo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EmployeeRestController {

    @Autowired
    EmployeeRepository employeeRepository;

    @Operation(summary = "Fetch All Employees")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched All Employees",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Employee.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Invalid URL",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Employee.class))
                    })
    })
    @GetMapping("/employees")
    public List<Employee> fetchAllEmployees(){
        return employeeRepository.findAll();
    }

    @PostMapping("/employees")
    public void addEmployee(@RequestBody Employee employee){
        employeeRepository.save(employee);
    }
}
