package com.example.Proyecto_MISW.services;

import com.example.Proyecto_MISW.entities.Employee;
import com.example.Proyecto_MISW.repositories.EmployeeRepositoy;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

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
        // Datos de empleados creados
        Employee employee1 = new Employee(1L, "12.345.678-9", "Juan", "Perez", "A", null);
        Employee employee2 = new Employee(2L, "7.654.321-0", "Maria", "Lopez", "B", null);
        List<Employee> employees = new ArrayList<>();
        employees.add(employee1);
        employees.add(employee2);

        //Indica que deberia retornar
        given(employeeRepositoy.findAll()).willReturn(employees);

        ArrayList<Employee> result = employeeService.getEmployees();

        //Deberia retornar ambos epleados
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Employee::getRut).contains("12.345.678-9", "7.654.321-0");
    }

    // Lista vacia de empleados
    @Test
    public void testGetEmployees_withEmptyList() {
        //Lista vacia
        List<Employee> employees = Collections.emptyList();

        given(employeeRepositoy.findAll()).willReturn(employees);

        ArrayList<Employee> result = employeeService.getEmployees();

        //No retorna empeados
        assertThat(result).isEmpty();
        assertThat(result).hasSize(0);
    }


    //Test para: public Employee getEmployeeByRut(String rut)
    //Obtener un empleado desde lista
    @Test
    public void getEmployeeByRut_findsEmployeeFromList() {
        // Datos de prueba
        String rut = "12.345.678-9";

        // Creación de empleados
        Employee employee1 = new Employee(1L, "12.345.678-9", "Juan", "Perez", "A", new Date());
        Employee employee2 = new Employee(2L, "8.765.432-1", "Maria", "Lopez", "B", new Date());
        Employee employee3 = new Employee(3L, "11.222.245-0", "Carla", "Suarez", "A", new Date());

        given(employeeRepositoy.findByRut(rut)).willReturn(employee1);

        Employee result = employeeService.getEmployeeByRut(rut);

        // Verificar que retorno sea empleado con rut indicado
        assertThat(result).isNotNull();
        assertThat(result.getRut()).isEqualTo(rut);
        assertThat(result.getFirst_name()).isEqualTo("Juan");
        assertThat(result.getLast_name()).isEqualTo("Perez");
        assertThat(result.getCategory()).isEqualTo("A");
    }

    //No se encuentra rut indicado
    @Test
    public void getEmployeeByRut_noEmployeeFound() {
        // Datos de prueba
        String rut = "11.111.111-1";

        // Creación de empleados
        Employee employee1 = new Employee(1L, "12.345.678-9", "Juan", "Perez", "A", new Date());
        Employee employee2 = new Employee(2L, "98.765.432-1", "Maria", "Lopez", "B", new Date());
        Employee employee3 = new Employee(3L, "11.222.245-0", "Carla", "Suarez", "A", new Date());

        given(employeeRepositoy.findByRut(rut)).willReturn(null);

        Employee result = employeeService.getEmployeeByRut(rut);

        // Verificar que no devuelve empleado, ya que no se encontró
        assertThat(result).isNull();
    }
    //Test para: public int calculateYearsOfService(String rut)
    //Calcular años de servicio
    @Test
    public void calculateYearsOfService_employeeExists() {
        // Datos de prueba
        String rut = "12.345.678-9";

        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, Calendar.DECEMBER, 10);
        Date registrationDate = calendar.getTime();

        Employee employee = new Employee(1L, rut, "Juan", "Perez", "A", registrationDate);

        given(employeeRepositoy.findByRut(rut)).willReturn(employee);

        int yearsOfService = employeeService.calculateYearsOfService(rut);

        // Verificar que la diferencia entre el año de registro diciembre 2018 al 2024 hay 5 años
        assertThat(yearsOfService).isEqualTo(5);
    }

    //Rut no existe:
    @Test
    public void calculateYearsOfService_employeeDoesNotExist() {
        // Datos de prueba
        String rut = "12.111.111-1";

        given(employeeRepositoy.findByRut(rut)).willReturn(null);

        int yearsOfService = employeeService.calculateYearsOfService(rut);

        // Verificar que devuelve -1 cuando el empleado no existe
        assertThat(yearsOfService).isEqualTo(-1);
    }





}
