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
    //Salario fijo para empleado categoría "A" (1700000)
    @Test
    void salaryForCategoryA() {
        //Datos creados
        Employee employee = new Employee(1L, "12.345.678-9", "Juan", "Perez", "A", new Date());
        employee.setCategory("A");

        int salary = officeHRMService.getFixedMonthlySalary(employee);

        //Verificar salario categoria
        assertThat(salary).isEqualTo(1700000);
    }


    //Salario fijo para empleado categoría "B" (1200000)
    @Test
    void salaryForCategoryB() {
        //Datos creados
        Employee employee = new Employee(2L, "8.765.432-1", "Maria", "Lopez", "B", new Date());
        employee.setCategory("B");

        int salary = officeHRMService.getFixedMonthlySalary(employee);
        //Verificar salario categoria
        assertThat(salary).isEqualTo(1200000);
    }

    //Salario fijo para empleado categoría "C" (800000)
    @Test
    void salaryForCategoryC() {
        //Datos creados
        Employee employee = new Employee(3L, "23.456.789-0", "Carlos", "Sanchez", "C", new Date());
        employee.setCategory("C");

        int salary = officeHRMService.getFixedMonthlySalary(employee);
        //Verificar salario categoria
        assertThat(salary).isEqualTo(800000);
    }

    // Test para: public int getSalaryBonus(Employee employee)
    //bonificación por tiempo de servicio (>=5 : 5%, >=10 : 8%, >=15 : 11%, >=20:14%, >=25 : 17%) sobre fijo mensual
    // Cálculo de Bonus 3 años de servicio
    @Test
    void bonusWith3YearsOfService() {
        //Datos creados
        Employee employee = new Employee(1L, "12.345.678-9", "Juan", "Perez", "A", Date.from(LocalDate.of(2020, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        employee.setCategory("A");

        int bonus = officeHRMService.getSalaryBonus(employee);

        //Verificar no registra bono ya que tiene menos de 5 años
        assertThat(bonus).isEqualTo(0);
    }
    // Cálculo de Bonus 6 años de servicio (>=5 : 5%)
    @Test
    void bonusWith6YearsOfService() {
        //Datos creados
        Employee employee = new Employee(1L, "12.345.678-9", "Juan", "Perez", "A", Date.from(LocalDate.of(2016, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        employee.setCategory("A");

        int bonus = officeHRMService.getSalaryBonus(employee);
        //5% de 1700000
        assertThat(bonus).isEqualTo(85000);
    }
    // Cálculo de Bonus 12 años de servicio (>=10 : 8%)
    @Test
    void bonusWith12YearsOfService() {
        //Datos creados
        Employee employee = new Employee(1L, "12.345.678-9", "Juan", "Perez", "A", Date.from(LocalDate.of(2010, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        employee.setCategory("A");

        int bonus = officeHRMService.getSalaryBonus(employee);
        //8% de 1700000
        assertThat(bonus).isEqualTo(136000);
    }
    //Cálculo de Bonus 16 años de servicio (>=15 : 11%)
    @Test
    void bonusWith16YearsOfService() {
        //Datos creados
        Employee employee = new Employee(1L, "12.345.678-9", "Juan", "Perez", "A", Date.from(LocalDate.of(2007, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        employee.setCategory("A");

        int bonus = officeHRMService.getSalaryBonus(employee);
        //11% de 1700000
        assertThat(bonus).isEqualTo(187000);
    }
    //Cálculo de Bonus 20 años de servicio (>=20:14%)
    @Test
    void bonusWith20YearsOfService() {
        //Datos creados
        Employee employee = new Employee(1L, "12.345.678-9", "Juan", "Perez", "A", Date.from(LocalDate.of(2004, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        employee.setCategory("A");

        int bonus = officeHRMService.getSalaryBonus(employee);
        //14% de 1700000
        assertThat(bonus).isEqualTo(238000);
    }
    //Cálculo de Bonus 28 años de servicio (>=25 : 17%)
    @Test
    void bonusWith28YearsOfService() {
        //Datos creados
        Employee employee = new Employee(1L, "12.345.678-9", "Juan", "Perez", "A", Date.from(LocalDate.of(1994, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        employee.setCategory("A");

        int bonus = officeHRMService.getSalaryBonus(employee);
        //17% de 1700000
        assertThat(bonus).isEqualTo(289000);
    }

    // Pruebas para: public int getExtraHoursBonus(Employee employee, int month, int year)
    // Categoria A = 25000 por hora, Categoria B = 20000 por hora, Categoria C = 10000 por hora

    // Horas extra en categoria "A"
    @Test
    void extraHoursForCategoryA() {
        //Datos creados
        Employee employee = new Employee(1L, "12.345.678-9", "Juan", "Perez", "A", new Date());
        employee.setCategory("A");

        given(extraHoursService.getTotalApprovedExtraHoursByRutAndMonth(employee.getRut(), 9, 2024)).willReturn(180);

        int bonus = officeHRMService.getExtraHoursBonus(employee, 9, 2024);

        //Verificar 180 minutos = 3 horas
        assertThat(bonus).isEqualTo(75000);
    }
    // Horas extra en categoria "B"
    @Test
    void extraHoursForCategoryB() {
        //Datos creados
        Employee employee = new Employee(1L, "12.345.678-9", "Juan", "Perez", "B", new Date());
        employee.setCategory("B");

        given(extraHoursService.getTotalApprovedExtraHoursByRutAndMonth(employee.getRut(), 9, 2024)).willReturn(180);

        int bonus = officeHRMService.getExtraHoursBonus(employee, 9, 2024);

        //Verificar 180 minutos = 3 horas
        assertThat(bonus).isEqualTo(60000);
    }
    // Horas extra en categoria "C"
    @Test
    void extraHoursForCategoryC() {
        //Datos creados
        Employee employee = new Employee(1L, "12.345.678-9", "Juan", "Perez", "C", new Date());
        employee.setCategory("C");

        given(extraHoursService.getTotalApprovedExtraHoursByRutAndMonth(employee.getRut(), 9, 2024)).willReturn(180);
        int bonus = officeHRMService.getExtraHoursBonus(employee, 9, 2024);
        //Verificar 180 minutos = 3 horas
        assertThat(bonus).isEqualTo(30000);
    }
    // Cero Horas extra en categoria "A"
    @Test
    void zeroExtraHours() {
        //Datos creados
        Employee employee = new Employee(1L, "12.345.678-9", "Juan", "Perez", "A", new Date());
        employee.setCategory("A");

        given(extraHoursService.getTotalApprovedExtraHoursByRutAndMonth(employee.getRut(), 9, 2024)).willReturn(0);

        int bonus = officeHRMService.getExtraHoursBonus(employee, 9, 2024);

        //Verificar que no hay horas extra por lo cual no hay bonificacion
        assertThat(bonus).isEqualTo(0);
    }

    // Test para: public int salaryDiscountArrears(Employee employee, int month, int year)
    // Monto descuentos: > 10 min: 1%, > 25 min: 3%, > 45 min: 6%, > 70 min: Se considera inasistencia.
    //Cálculo descuento por atraso con cero minutos de atraso.
    @Test
    void discountArrearsWithZeroMinutesDiscountHours() {

        //Datos creados
        Employee employee = new Employee(1L, "12.345.678-9", "Juan", "Perez", "A", new Date());
        employee.setCategory("A");

        given(discountHoursService.getTotalUnapprovedDiscountHoursByRutAndMonth(employee.getRut(), 9, 2024)).willReturn(0);

        int discount = officeHRMService.salaryDiscountArrears(employee, 9, 2024);

        //Verificar no hay descuento
        assertThat(discount).isEqualTo(0);
    }
    //Cálculo descuento por atraso con 6 minutos de atraso.
    @Test
    void discountArrearsWith6MinutesDiscountHours() {
        //Datos creados
        Employee employee = new Employee(1L, "12.345.678-9", "Juan", "Perez", "A", new Date());
        employee.setCategory("A");

        given(discountHoursService.getTotalUnapprovedDiscountHoursByRutAndMonth(employee.getRut(), 9, 2024)).willReturn(6);

        int discount = officeHRMService.salaryDiscountArrears(employee, 9, 2024);

        //Descuento 0, menor a 10 minutos
        assertThat(discount).isEqualTo(0);
    }
    //Cálculo descuento por atraso con 15 minutos de atraso.
    @Test
    void discountArrearsWith15MinutesDiscountHours() {
        //Datos creados
        Employee employee = new Employee(1L, "12.345.678-9", "Juan", "Perez", "A", new Date());
        employee.setCategory("A");

        given(discountHoursService.getTotalUnapprovedDiscountHoursByRutAndMonth(employee.getRut(), 9, 2024)).willReturn(15);

        int discount = officeHRMService.salaryDiscountArrears(employee, 9, 2024);

        // Descuento 15 minutos, 1% de 1.700.000
        assertThat(discount).isEqualTo(17000);
    }
    //Cálculo descuento por atraso con 33 minutos de atraso.
    @Test
    void discountArrearsWith33MinutesDiscountHours() {
        //Datos creados
        Employee employee = new Employee(1L, "12.345.678-9", "Juan", "Perez", "A", new Date());
        employee.setCategory("A");

        given(discountHoursService.getTotalUnapprovedDiscountHoursByRutAndMonth(employee.getRut(), 9, 2024)).willReturn(33);

        int discount = officeHRMService.salaryDiscountArrears(employee, 9, 2024);
        //Descuento 33 minutos 3% 1.700.000
        assertThat(discount).isEqualTo(51000);
    }
    //Cálculo descuento por atraso con 50 minutos de atraso.
    @Test
    void discountArrearsWith50MinutesDiscountHours() {
        //Datos creados
        Employee employee = new Employee(1L, "12.345.678-9", "Juan", "Perez", "A", new Date());
        employee.setCategory("A");

        given(discountHoursService.getTotalUnapprovedDiscountHoursByRutAndMonth(employee.getRut(), 9, 2024)).willReturn(50);

        int discount = officeHRMService.salaryDiscountArrears(employee, 9, 2024);
        // Descuento 50 minutos 6% 1.700.000
        assertThat(discount).isEqualTo(102000);
    }
    @Test
    void discountArrearsWith80MinutesDiscountHours() {
        //Datos creados
        Employee employee = new Employee(1L, "12.345.678-9", "Juan", "Perez", "A", new Date());
        employee.setCategory("A");

        given(discountHoursService.getTotalUnapprovedDiscountHoursByRutAndMonth(employee.getRut(), 9, 2024)).willReturn(80);

        int discount = officeHRMService.salaryDiscountArrears(employee, 9, 2024);

        //Descuento 50 minutos 15 % del sueldo
        assertThat(discount).isEqualTo(255000);
    }

    //Test para: public int getSocialSecurityDiscount(int totalSalary)
    @Test
    void socialSecurityDiscount() {
        //Datos salario
        int totalSalary = 1000000;

        int socialSecurityDiscount = officeHRMService.getSocialSecurityDiscount(totalSalary);

        //calculo 10%
        assertThat(socialSecurityDiscount).isEqualTo(100000);
    }

    //Test para: public int getHealthDiscount(int totalSalary)
    @Test
    void healthDiscount() {
        //datos salario
        int totalSalary = 1000000;

        int healthDiscount = officeHRMService.getHealthDiscount(totalSalary);
        //Calculo 8%
        assertThat(healthDiscount).isEqualTo(80000);
    }
}
