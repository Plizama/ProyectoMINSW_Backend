package com.example.Proyecto_MISW.services;

import com.example.Proyecto_MISW.entities.Employee;
import com.example.Proyecto_MISW.repositories.EmployeeRepositoy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class EmployeeService {
    @Autowired
    EmployeeRepositoy employeeRepositoy;
    public ArrayList<Employee> getEmployees(){
        return (ArrayList<Employee>) employeeRepositoy.findAll();
    }
}
