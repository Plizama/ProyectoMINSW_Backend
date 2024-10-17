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

    //Obtener listado de empleados
    public ArrayList<Employee> getEmployees() {
        return new ArrayList<>(employeeRepositoy.findAll());
    }

    //Obtener empleado por rut
    public Employee getEmployeeByRut(String rut) {
        return employeeRepositoy.findByRut(rut);
    }

    //Calcular años de servicio de empleado
    public int calculateYearsOfService(String rut) {
        Employee employee = employeeRepositoy.findByRut(rut);
        if (employee != null) {
            LocalDate registrationDate = employee.getRegistration_date().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            LocalDate currentDate = LocalDate.now();

            // Calcular la diferencia en años desde el registro a fecha actual
            return Period.between(registrationDate, currentDate).getYears();
        } else {
            // En caso de que el empleado no exista:
            return -1;
        }
    }
}
