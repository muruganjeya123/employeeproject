package com.example.demo.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeRepository repo;

    // CREATE
    @PostMapping
    public Employee saveEmployee(@RequestBody Employee emp) {
        return repo.save(emp);
    }

    // READ ALL
    @GetMapping
    public List<Employee> getAllEmployees() {
        return repo.findAll();
    }

    // UPDATE
    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable Integer id,
                                   @RequestBody Employee emp) {

        Employee existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        existing.setName(emp.getName());

        return repo.save(existing);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String deleteEmployee(@PathVariable Integer id) {
        repo.deleteById(id);
        return "Deleted Successfully";
    }
}