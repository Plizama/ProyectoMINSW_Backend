package com.example.Proyecto_MISW.entities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "paychecks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paycheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    private String rut;
    private int year;
    private int month;
    //Salario Fijo Mensual
    private int monthlySalary;
    //Bonificación por años de servicio
    private int salaryBonus;
    //Descuento por atrasos
    private int discountHours;
    //Pago de horas extra
    private int extraHoursBonus;
    //Sueldo Bruto
    private int grossSalary;
    //Descuentos legales
    //Decuentos seguridad social (cotizaciones)
    private int SocialSecurityDiscount;
    //Descuentos Salud
    private int HealthDiscount;
    //Salario Liquido
    private int totalSalary;
}
