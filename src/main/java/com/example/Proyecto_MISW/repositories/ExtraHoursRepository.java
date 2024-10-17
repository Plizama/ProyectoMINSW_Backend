package com.example.Proyecto_MISW.repositories;

import com.example.Proyecto_MISW.entities.ExtraHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ExtraHoursRepository extends JpaRepository<ExtraHours, Long> {

    //Obtener lista de horas extra segun rut ingresado
    public List<ExtraHours> findByRut(String rut);
    //Obtener listado de horas extra segun rut y fecha
    List<ExtraHours> findByRutAndDate(String rut, Date date);
    // Obtener listado de horas extra segun rango de fechas
    List<ExtraHours> findByRutAndDateBetween(String rut, Date startDate, Date endDate);
}
