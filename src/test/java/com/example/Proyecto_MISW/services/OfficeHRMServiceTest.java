package com.example.Proyecto_MISW.services;

import com.example.Proyecto_MISW.entities.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
@SpringBootTest
public class OfficeHRMServiceTest {

    @Mock
    private ExtraHoursService extraHoursService;

    @Mock
    private DiscountHoursService discountHoursService;

    @InjectMocks
    private OfficeHRMService officeHRMService;

    Employee employee = new Employee();

    // Test para: public int getFixedMonthlySalary(Employee employee)
    //Salario fijo para empleado categoría "A"
    @Test
    void salaryForCategoryA() {
        employee.setCategory("A");

        int salary = officeHRMService.getFixedMonthlySalary(employee);

        assertThat(salary).isEqualTo(1700000);
    }
    //Salario fijo para empleado categoría "B"
    @Test
    void salaryForCategoryB() {
        employee.setCategory("B");

        int salary = officeHRMService.getFixedMonthlySalary(employee);

        assertThat(salary).isEqualTo(1200000);
    }
    //Salario fijo para empleado categoría "C"
    @Test
    void salaryForCategoryC() {
        employee.setCategory("C");

        int salary = officeHRMService.getFixedMonthlySalary(employee);

        assertThat(salary).isEqualTo(800000);
    }

    // Test para: public int getSalaryBonus(Employee employee) - bonificación por tiempo de servicio (>=5 : 5%, >=10 : 8%, >=15 : 11%, >=20:14%, >=25 : 17%) sobre fijo mensual
    // Cálculo de Bonus 3 años de servicio
    @Test
    void bonusWith3YearsOfService() {
        employee.setRegistration_date(Date.from(LocalDate.of(2020, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        employee.setCategory("A");
        int bonus = officeHRMService.getSalaryBonus(employee);

        assertThat(bonus).isEqualTo(0);
    }
    // Cálculo de Bonus 6 años de servicio (>=5 : 5%)
    @Test
    void bonusWith6YearsOfService() {
        employee.setCategory("A");
        employee.setRegistration_date(Date.from(LocalDate.of(2016, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        int bonus = officeHRMService.getSalaryBonus(employee);

        assertThat(bonus).isEqualTo(85000);
    }
    // Cálculo de Bonus 12 años de servicio (>=10 : 8%)
    @Test
    void bonusWith12YearsOfService() {
        employee.setCategory("A");
        employee.setRegistration_date(Date.from(LocalDate.of(2010, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        int bonus = officeHRMService.getSalaryBonus(employee);

        assertThat(bonus).isEqualTo(136000);
    }
    //Cálculo de Bonus 16 años de servicio (>=15 : 11%)
    @Test
    void bonusWith16YearsOfService() {
        employee.setCategory("A");
        employee.setRegistration_date(Date.from(LocalDate.of(2007, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        int bonus = officeHRMService.getSalaryBonus(employee);

        assertThat(bonus).isEqualTo(187000);
    }
    //Cálculo de Bonus 20 años de servicio (>=20:14%)
    @Test
    void bonusWith20YearsOfService() {
        employee.setCategory("A");
        employee.setRegistration_date(Date.from(LocalDate.of(2004, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        int bonus = officeHRMService.getSalaryBonus(employee);

        assertThat(bonus).isEqualTo(238000);
    }
    //Cálculo de Bonus 28 años de servicio (>=25 : 17%)
    @Test
    void bonusWith28YearsOfService() {
        employee.setCategory("A");
        employee.setRegistration_date(Date.from(LocalDate.of(1994, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        int bonus = officeHRMService.getSalaryBonus(employee);

        assertThat(bonus).isEqualTo(289000);
    }

    // Pruebas para: public int getExtraHoursBonus(Employee employee, int month, int year)
    // Categoria A = 25000 por hora, Categoria B = 20000 por hora, Categoria C = 10000 por hora

    // Horas extra en categoria "A"
    @Test
    void extraHoursForCategoryA() {
        // Given
        employee.setCategory("A");
        given(extraHoursService.getTotalApprovedExtraHoursByRutAndMonth(employee.getRut(), 9, 2024)).willReturn(180);

        int bonus = officeHRMService.getExtraHoursBonus(employee, 9, 2024);

        assertThat(bonus).isEqualTo(75000);
    }
    // Horas extra en categoria "B"
    @Test
    void extraHoursForCategoryB() {
        employee.setCategory("B");
        given(extraHoursService.getTotalApprovedExtraHoursByRutAndMonth(employee.getRut(), 9, 2024)).willReturn(180);  // 3 horas extra

        int bonus = officeHRMService.getExtraHoursBonus(employee, 9, 2024);

        assertThat(bonus).isEqualTo(60000);
    }
    // Horas extra en categoria "C"
    @Test
    void extraHoursForCategoryC() {
        employee.setCategory("C");
        given(extraHoursService.getTotalApprovedExtraHoursByRutAndMonth(employee.getRut(), 9, 2024)).willReturn(180);  // 3 horas extra

        int bonus = officeHRMService.getExtraHoursBonus(employee, 9, 2024);

        assertThat(bonus).isEqualTo(30000);
    }
    // Cero Horas extra en categoria "A"
    @Test
    void zeroExtraHours() {
        employee.setCategory("A");
        given(extraHoursService.getTotalApprovedExtraHoursByRutAndMonth(employee.getRut(), 9, 2024)).willReturn(0);  // Sin horas extra

        int bonus = officeHRMService.getExtraHoursBonus(employee, 9, 2024);

        assertThat(bonus).isEqualTo(0);
    }

    // Test para: public int salaryDiscountArrears(Employee employee, int month, int year)
    //Cálculo descuento por atraso con cero minutos de atraso.
    @Test
    void discountArrearsWithZeroMinutesDiscountHours() {
        employee.setCategory("A");
        given(discountHoursService.getTotalUnapprovedDiscountHoursByRutAndMonth(employee.getRut(), 9, 2024)).willReturn(0);

        int discount = officeHRMService.salaryDiscountArrears(employee, 9, 2024);

        assertThat(discount).isEqualTo(0);
    }
    //Cálculo descuento por atraso con 6 minutos de atraso.
    @Test
    void discountArrearsWith6MinutesDiscountHours() {
        employee.setCategory("A");
        given(discountHoursService.getTotalUnapprovedDiscountHoursByRutAndMonth(employee.getRut(), 9, 2024)).willReturn(6);

        int discount = officeHRMService.salaryDiscountArrears(employee, 9, 2024);

        assertThat(discount).isEqualTo(0);
    }
    //Cálculo descuento por atraso con 15 minutos de atraso.
    @Test
    void discountArrearsWith15MinutesDiscountHours() {
        employee.setCategory("A");
        given(discountHoursService.getTotalUnapprovedDiscountHoursByRutAndMonth(employee.getRut(), 9, 2024)).willReturn(15);

        int discount = officeHRMService.salaryDiscountArrears(employee, 9, 2024);

        assertThat(discount).isEqualTo(17000);
    }
    //Cálculo descuento por atraso con 33 minutos de atraso.
    @Test
    void discountArrearsWith33MinutesDiscountHours() {
        employee.setCategory("A");
        given(discountHoursService.getTotalUnapprovedDiscountHoursByRutAndMonth(employee.getRut(), 9, 2024)).willReturn(33);

        int discount = officeHRMService.salaryDiscountArrears(employee, 9, 2024);

        assertThat(discount).isEqualTo(51000);
    }
    //Cálculo descuento por atraso con 50 minutos de atraso.
    @Test
    void discountArrearsWith50MinutesDiscountHours() {
        employee.setCategory("A");
        given(discountHoursService.getTotalUnapprovedDiscountHoursByRutAndMonth(employee.getRut(), 9, 2024)).willReturn(50);

        int discount = officeHRMService.salaryDiscountArrears(employee, 9, 2024);

        assertThat(discount).isEqualTo(102000);
    }
    //Cálculo descuento por atraso con 80 minutos de atraso.
    @Test
    void discountArrearsWith80MinutesDiscountHours() {
        employee.setCategory("A");
        given(discountHoursService.getTotalUnapprovedDiscountHoursByRutAndMonth(employee.getRut(), 9, 2024)).willReturn(80);

        int discount = officeHRMService.salaryDiscountArrears(employee, 9, 2024);

        assertThat(discount).isEqualTo(255000);
    }

    //Test para: public int getSocialSecurityDiscount(int totalSalary)
    @Test
    void socialSecurityDiscount() {
        int totalSalary = 1000000;

        int socialSecurityDiscount = officeHRMService.getSocialSecurityDiscount(totalSalary);

        assertThat(socialSecurityDiscount).isEqualTo(100000);
    }

    //Test para: public int getHealthDiscount(int totalSalary)
    @Test
    void healthDiscount() {
        int totalSalary = 1000000;

        int healthDiscount = officeHRMService.getHealthDiscount(totalSalary);

        assertThat(healthDiscount).isEqualTo(80000);
    }
}
