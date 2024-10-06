package com.example.Proyecto_MISW.services;

import com.example.Proyecto_MISW.entities.Employee;
import com.example.Proyecto_MISW.repositories.EmployeeRepositoy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepositoy employeeRepositoy;

    public ArrayList<Employee> getEmployees() {
        return new ArrayList<>(employeeRepositoy.findAll());
    }
    public Employee getEmployeeByRut(String rut) {
        return employeeRepositoy.findByRut(rut);
    }
    public int calculateYearsOfService(String rut) {
        Employee employee = employeeRepositoy.findByRut(rut);
        if (employee != null) {
            LocalDate registrationDate = employee.getRegistration_date().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            LocalDate currentDate = LocalDate.now();

            // Calcular la diferencia en años
            return Period.between(registrationDate, currentDate).getYears();
        } else {
            // En caso de que el empleado no exista, retorna -1 o lanza una excepción
            return -1;
        }
    }
}
