package com.example.Proyecto_MISW.controllers;

import com.example.Proyecto_MISW.entities.Employee;
import com.example.Proyecto_MISW.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
@CrossOrigin("*")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    @GetMapping("/")
    public ResponseEntity<List<Employee>> listEmployees() {
        List<Employee> employees = employeeService.getEmployees();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{rut}")
    public ResponseEntity<Employee> getEmployeeByRut(@PathVariable String rut) {
        Employee employee = employeeService.getEmployeeByRut(rut);
        if (employee != null) {
            return ResponseEntity.ok(employee);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/yearsOfService/{rut}")
    public ResponseEntity<Integer> getYearsOfServiceByRut(@PathVariable String rut) {
        int yearsOfService = employeeService.calculateYearsOfService(rut);
        if (yearsOfService != -1) {
            return ResponseEntity.ok(yearsOfService);
        } else {
            return ResponseEntity.notFound().build();  // Si el RUT no existe
        }
    }

}
