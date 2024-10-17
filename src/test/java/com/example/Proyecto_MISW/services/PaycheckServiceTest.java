package com.example.Proyecto_MISW.services;

import com.example.Proyecto_MISW.entities.Employee;
import com.example.Proyecto_MISW.entities.Paycheck;
import com.example.Proyecto_MISW.repositories.PaycheckRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;

@SpringBootTest
public class PaycheckServiceTest {

    @Mock
    private PaycheckRepository paycheckRepository;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private OfficeHRMService officeHRMService;

    @InjectMocks
    private PaycheckService paycheckService;

    public PaycheckServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    // Test para: public Boolean calculatePaychecks (int month, int year)
    //Calcula un pago
    @Test
    void calculateSomePaychecks() {

        // Creación empleado
        List<Employee> employees = new ArrayList<>();
        Employee employee1 = new Employee(
                1L,
                "12.345.678-9",
                "Juan",
                "Perez",
                "A",
                Date.from(LocalDate.of(2018, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())
        );
        employees.add(employee1);

        given(employeeService.getEmployees()).willReturn((ArrayList<Employee>) employees);
        // Salario fijo
        given(officeHRMService.getFixedMonthlySalary(employee1)).willReturn(1700000);
        // Bonificación por antigüedad
        given(officeHRMService.getSalaryBonus(employee1)).willReturn(170000);
        // Descuento por atraso
        given(officeHRMService.salaryDiscountArrears(employee1, 9, 2024)).willReturn(0);
        // Bonificación por horas extra
        given(officeHRMService.getExtraHoursBonus(employee1, 9, 2024)).willReturn(250000);
        // Descuento de seguridad social
        given(officeHRMService.getSocialSecurityDiscount(2120000)).willReturn(212000);
        // Descuento de salud
        given(officeHRMService.getHealthDiscount(2120000)).willReturn(169600);

        Boolean result = paycheckService.calculatePaychecks(9, 2024);

        assertThat(result).isTrue();

        // Verificar que el repositorio haya almacenado el valor
        verify(paycheckRepository, times(1)).save(any(Paycheck.class));
    }

    // Test para: public Paycheck getPaycheckByRutAndMonth(String rut, int month, int year)
    // Obtener pagos con rut y fechas correctas
    @Test
    void paycheckByRutAndMonth_CorrectData() {
        // Datos almacenados
        Paycheck paycheck = new Paycheck();
        paycheck.setRut("12.345.678-9");
        paycheck.setMonth(9);
        paycheck.setYear(2024);

        given(paycheckRepository.findByRutAndMonthAndYear("12.345.678-9", 9, 2024)).willReturn(paycheck);

        Paycheck result = paycheckService.getPaycheckByRutAndMonth("12.345.678-9", 9, 2024);

        // Verificar datos
        assertThat(result).isNotNull();
        assertThat(result.getRut()).isEqualTo("12.345.678-9");
        assertThat(result.getMonth()).isEqualTo(9);
        assertThat(result.getYear()).isEqualTo(2024);
    }

    // Test para: public Paycheck getPaycheckByRutAndMonth(String rut, int month, int year)
    // Obtener pagos con rut incorrecto y fechas correctas
    @Test
    void nonExistentRut_WithStoredData() {
        // Datos almacenados
        Paycheck paycheck = new Paycheck();
        paycheck.setRut("12.345.678-9");
        paycheck.setMonth(9);
        paycheck.setYear(2024);

        // Rut incorrecto
        given(paycheckRepository.findByRutAndMonthAndYear("10.555.444-1", 9, 2024)).willReturn(null);

        Paycheck result = paycheckService.getPaycheckByRutAndMonth("10.555.444-1", 9, 2024);

        assertThat(result).isNull();
    }

    // Test para: public Paycheck getPaycheckByRutAndMonth(String rut, int month, int year)
    // Prueba unitaria para getPaycheckByRutAndMonth con mes no almacenado
    @Test
    void nonExistentMonth_WithStoredData() {
        // Datos almacenados
        Paycheck paycheck = new Paycheck();
        paycheck.setRut("12.345.678-9");
        paycheck.setMonth(9);
        paycheck.setYear(2024);

        // Mes no existente
        given(paycheckRepository.findByRutAndMonthAndYear("12.345.678-9", 10, 2024)).willReturn(null);

        Paycheck result = paycheckService.getPaycheckByRutAndMonth("12.345.678-9", 10, 2024);

        assertThat(result).isNull();
    }
}

