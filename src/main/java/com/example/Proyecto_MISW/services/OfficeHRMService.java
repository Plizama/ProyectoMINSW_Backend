package com.example.Proyecto_MISW.services;

import com.example.Proyecto_MISW.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

import static java.lang.Math.round;

@Service
public class OfficeHRMService {
    @Autowired
    private ExtraHoursService extraHoursService;
    @Autowired
    private DiscountHoursService discountHoursService;

    //Salario fijo Mensual según categoria
    public int getFixedMonthlySalary(Employee employee) {
        int fixedMonthlySalary = 0;
        if (employee.getCategory().equals("A")) {
            fixedMonthlySalary = 1700000;
        } else if (employee.getCategory().equals("B")) {
            fixedMonthlySalary = 1200000;
        } else {
            fixedMonthlySalary = 800000;
        }
        return fixedMonthlySalary;
    }

    // bonificación por tiempo de servicio (>=5 : 5%, >=10 : 8%, >=15 : 11%, >=20:14%, >=25 : 17%) sobre fijo mensual
    public int getSalaryBonus(Employee employee) {
        int salaryBonus = 0;

        // Convertir registration_date (Date) a LocalDate
        LocalDate registrationDate = employee.getRegistration_date().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        // Obtener la fecha actual
        LocalDate currentDate = LocalDate.now();

        // Calcular la diferencia en años entre registrationDate y currentDate
        int yearsOfService = Period.between(registrationDate, currentDate).getYears();

        // Obtener el salario mensual fijo
        int fixedMonthlySalary = getFixedMonthlySalary(employee);

        // Calcular el bono basado en los años de servicio
        if (yearsOfService >= 25) {
            salaryBonus = round(fixedMonthlySalary * 0.17f);  // 17% para >= 25 años
        } else if (yearsOfService >= 20) {
            salaryBonus = round(fixedMonthlySalary * 0.14f);  // 14% para >= 20 años
        } else if (yearsOfService >= 15) {
            salaryBonus = round(fixedMonthlySalary * 0.11f);  // 11% para >= 15 años
        } else if (yearsOfService >= 10) {
            salaryBonus = round(fixedMonthlySalary * 0.08f);  // 8% para >= 10 años
        } else if (yearsOfService >= 5) {
            salaryBonus = round(fixedMonthlySalary * 0.05f);  // 5% para >= 5 años
        }

        return salaryBonus;
    }

    //Pago de horas extra:
    // Solo se pagan si estan aprobadas
    // Categoria A = 25000 por hora, Categoria B = 20000 por hora, Categoria C = 10000 por hora
    public int getExtraHoursBonus(Employee employee, int month, int year) {
        // Obtener los minutos de horas extra aprobadas
        int minutesExtraHours = extraHoursService.getTotalApprovedExtraHoursByRutAndMonth(employee.getRut(), month, year);

        int extraHoursBonus = 0;

        if (employee.getCategory().equals("A")) {
            // Dividimos la tarifa por 60 para obtener el valor por minuto
            extraHoursBonus = Math.round(minutesExtraHours * (25000.0f / 60.0f));
        } else if (employee.getCategory().equals("B")) {
            extraHoursBonus = Math.round(minutesExtraHours * (20000.0f / 60.0f));
        } else {
            extraHoursBonus = Math.round(minutesExtraHours * (10000.0f / 60.0f));
        }

        return extraHoursBonus;
    }

    //Descuento por atrasos
    public int salaryDiscountArrears(Employee employee, int month, int year){
        int minutesDiscountHours = discountHoursService.getTotalUnapprovedDiscountHoursByRutAndMonth(employee.getRut(), month,year);
        int salaryDiscountArrears = 0;
        int fixedMonthlySalary = getFixedMonthlySalary(employee);
        if(minutesDiscountHours > 70){
            salaryDiscountArrears = round(fixedMonthlySalary * 0.15f);
        } else if (minutesDiscountHours >45) {
            salaryDiscountArrears = round(fixedMonthlySalary * 0.06f);
        } else if (minutesDiscountHours >25) {
            salaryDiscountArrears = round(fixedMonthlySalary * 0.03f);
        } else if (minutesDiscountHours >10) {
            salaryDiscountArrears = round(fixedMonthlySalary * 0.01f);
        }
        return salaryDiscountArrears;
    }

    //Descuento Legales

    //seguridad social
    public int getSocialSecurityDiscount(int totalSalary){
        int SocialSecurityDiscount = 0;
        //Descuento cotización
       SocialSecurityDiscount = round(totalSalary * 0.1f);

        return SocialSecurityDiscount;
    }

    //Salud
    public int getHealthDiscount(int totalSalary){
        int HealthDiscount = 0;
        //Descuento cotización
        HealthDiscount = round(totalSalary * 0.08f);

        return HealthDiscount;
    }


}

