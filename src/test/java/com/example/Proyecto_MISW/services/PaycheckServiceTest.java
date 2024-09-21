package com.example.Proyecto_MISW.services;

import com.example.Proyecto_MISW.entities.Employee;
import com.example.Proyecto_MISW.entities.Paycheck;
import com.example.Proyecto_MISW.repositories.PaycheckRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
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
    @Test
    void calculateSomePaychecks() {

        List<Employee> employees = new ArrayList<>();
        Employee employee1 = new Employee();
        employee1.setRut("12.345.678-9");
        employee1.setCategory("A");
        employees.add(employee1);

        given(employeeService.getEmployees()).willReturn((ArrayList<Employee>) employees);
        given(officeHRMService.getFixedMonthlySalary(employee1)).willReturn(1700000);
        given(officeHRMService.getSalaryBonus(employee1)).willReturn(170000);
        given(officeHRMService.salaryDiscountArrears(employee1, 9, 2024)).willReturn(0);
        given(officeHRMService.getExtraHoursBonus(employee1, 9, 2024)).willReturn(250000);
        given(officeHRMService.getSocialSecurityDiscount(2120000)).willReturn(212000);
        given(officeHRMService.getHealthDiscount(2120000)).willReturn(169600);

        Boolean result = paycheckService.calculatePaychecks(9, 2024);

        assertThat(result).isTrue();
        verify(paycheckRepository, times(1)).save(any(Paycheck.class));
    }

    //Test para: public Paycheck getPaycheckByRutAndMonth(String rut, int month, int year)
    //Obtener pagos con rut, y fechas correctas
    @Test
    void paycheckByRutAndMonth_CorrectData() {
        Paycheck paycheck = new Paycheck();
        paycheck.setRut("12.345.678-9");
        paycheck.setMonth(9);
        paycheck.setYear(2024);

        given(paycheckRepository.findByRutAndMonthAndYear("12.345.678-9", 9, 2024)).willReturn(paycheck);

        Paycheck result = paycheckService.getPaycheckByRutAndMonth("12.345.678-9", 9, 2024);

        assertThat(result).isNotNull();
        assertThat(result.getRut()).isEqualTo("12.345.678-9");
        assertThat(result.getMonth()).isEqualTo(9);
        assertThat(result.getYear()).isEqualTo(2024);
    }

    //Obtener pagos con rut incorrecto, y fechas correctas
    @Test
    void nonExistentRut_WithStoredData() {
        Paycheck paycheck = new Paycheck();
        paycheck.setRut("12.345.678-9");
        paycheck.setMonth(9);
        paycheck.setYear(2024);

        given(paycheckRepository.findByRutAndMonthAndYear("12.345.678-9", 9, 2024)).willReturn(paycheck);

        given(paycheckRepository.findByRutAndMonthAndYear("10.555.444-1", 9, 2024)).willReturn(null);

        Paycheck result = paycheckService.getPaycheckByRutAndMonth("10.555.444-1", 9, 2024);

        assertThat(result).isNull();
    }

    // Prueba unitaria para getPaycheckByRutAndMonth con mes no almacenado
    @Test
    void nonExistentMonth_WithStoredData() {
        Paycheck paycheck = new Paycheck();
        paycheck.setRut("12.345.678-9");
        paycheck.setMonth(9);
        paycheck.setYear(2024);

        given(paycheckRepository.findByRutAndMonthAndYear("12.345.678-9", 9, 2024)).willReturn(paycheck);

        given(paycheckRepository.findByRutAndMonthAndYear("12.345.678-9", 10, 2024)).willReturn(null);

        Paycheck result = paycheckService.getPaycheckByRutAndMonth("12.345.678-9", 10, 2024);

        assertThat(result).isNull();
    }
}
