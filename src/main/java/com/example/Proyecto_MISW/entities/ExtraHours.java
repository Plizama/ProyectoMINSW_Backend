package com.example.Proyecto_MISW.entities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "extraHours")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtraHours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    private String rut;
    private Date date;
    private int numExtraHours;
    //debe inicial tabla en false.
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean approval;
}
