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

    // bonificación por tiempo de servicio
    // (>=5 : 5%, >=10 : 8%, >=15 : 11%, >=20:14%, >=25 : 17%) sobre fijo mensual
    public int getSalaryBonus(Employee employee) {
        int salaryBonus = 0;

        // Convertir registration_date (Date) a LocalDate
        LocalDate registrationDate = employee.getRegistration_date().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        // Fecha actual
        LocalDate currentDate = LocalDate.now();

        // Calcular años
        int yearsOfService = Period.between(registrationDate, currentDate).getYears();

        // Obtener el salario mensual fijo
        int fixedMonthlySalary = getFixedMonthlySalary(employee);

        // Calcular el bono basado en los años de servicio
        if (yearsOfService >= 25) {
            // 17% para >= 25 años
            salaryBonus = round(fixedMonthlySalary * 0.17f);
        } else if (yearsOfService >= 20) {
            // 14% para >= 20 años
            salaryBonus = round(fixedMonthlySalary * 0.14f);
        } else if (yearsOfService >= 15) {
            // 11% para >= 15 años
            salaryBonus = round(fixedMonthlySalary * 0.11f);
        } else if (yearsOfService >= 10) {
            // 8% para >= 10 años
            salaryBonus = round(fixedMonthlySalary * 0.08f);
        } else if (yearsOfService >= 5) {
            // 5% para >= 5 años
            salaryBonus = round(fixedMonthlySalary * 0.05f);
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
    // Monto descuentos: > 10 min: 1%, > 25 min: 3%, > 45 min: 6%, > 70 min: Se considera inasistencia.
    public int salaryDiscountArrears(Employee employee, int month, int year){
        int minutesDiscountHours = discountHoursService.getTotalUnapprovedDiscountHoursByRutAndMonth(employee.getRut(), month,year);
        int salaryDiscountArrears = 0;
        int fixedMonthlySalary = getFixedMonthlySalary(employee);
        //> 70 min: Se considera inasistencia.
        if(minutesDiscountHours > 70){
            salaryDiscountArrears = round(fixedMonthlySalary * 0.15f);
            //> 45 min: 6%
        } else if (minutesDiscountHours >45) {
            salaryDiscountArrears = round(fixedMonthlySalary * 0.06f);
            //> 25 min: 3%
        } else if (minutesDiscountHours >25) {
            salaryDiscountArrears = round(fixedMonthlySalary * 0.03f);
            //> 10 min: 1%
        } else if (minutesDiscountHours >10) {
            salaryDiscountArrears = round(fixedMonthlySalary * 0.01f);
        }
        return salaryDiscountArrears;
    }

    //----------Descuento Legales-----------------

    //seguridad social (10 % de descuento)
    public int getSocialSecurityDiscount(int totalSalary){
        int SocialSecurityDiscount = 0;
        //Descuento cotización
       SocialSecurityDiscount = round(totalSalary * 0.1f);

        return SocialSecurityDiscount;
    }

    //Salud (8% de descuento)
    public int getHealthDiscount(int totalSalary){
        int HealthDiscount = 0;
        //Descuento cotización
        HealthDiscount = round(totalSalary * 0.08f);

        return HealthDiscount;
    }


}

