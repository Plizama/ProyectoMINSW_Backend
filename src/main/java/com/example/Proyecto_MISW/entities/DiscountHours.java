package com.example.Proyecto_MISW.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "discount_hours")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountHours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    private String rut;
    private Date date;
    //Minutos de ingreso desdpués de las 08:00
    private int numDiscountHours;
    //debe inicial tabla en false.
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean approval;
}
