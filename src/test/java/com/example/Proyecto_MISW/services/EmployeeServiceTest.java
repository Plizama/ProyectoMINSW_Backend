package com.example.Proyecto_MISW.services;

import com.example.Proyecto_MISW.entities.Employee;
import com.example.Proyecto_MISW.repositories.EmployeeRepositoy;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepositoy employeeRepositoy;

    @InjectMocks
    private EmployeeService employeeService;

    public EmployeeServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    //Test para: public ArrayList<Employee> getEmployees()
    //Obtener listado de empleados
    @Test
    public void testGetEmployees_withEmployeeList() {
        // Simulamos una lista de empleados en el repositorio
        Employee employee1 = new Employee(1L, "12.345.678-9", "Juan", "Perez", "A", null);
        Employee employee2 = new Employee(2L, "7.654.321-0", "Maria", "Lopez", "B", null);
        List<Employee> employees = new ArrayList<>();
        employees.add(employee1);
        employees.add(employee2);

        given(employeeRepositoy.findAll()).willReturn(employees);

        ArrayList<Employee> result = employeeService.getEmployees();

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Employee::getRut).contains("12.345.678-9", "7.654.321-0");
    }

    // Lista vacia de empleados
    @Test
    public void testGetEmployees_withEmptyList() {
        List<Employee> employees = Collections.emptyList();

        given(employeeRepositoy.findAll()).willReturn(employees);

        ArrayList<Employee> result = employeeService.getEmployees();

        assertThat(result).isEmpty();
        assertThat(result).hasSize(0);
    }
}
