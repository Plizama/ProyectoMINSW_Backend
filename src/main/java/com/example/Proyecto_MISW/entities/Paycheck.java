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
    private int monthlySalary;
    private int salaryBonus;
    private int salaryDiscount;
    private int discountHours;
    private int extraHoursBonus;
    private int totalSalary;
}
