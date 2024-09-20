package com.example.Proyecto_MISW.repositories;

import com.example.Proyecto_MISW.entities.ExtraHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ExtraHoursRepository extends JpaRepository<ExtraHours, Long> {
    public List<ExtraHours> findByRut(String rut);
    List<ExtraHours> findByRutAndDate(String rut, Date date);
    // Método para obtener las horas extra dentro de un rango de fechas
    List<ExtraHours> findByRutAndDateBetween(String rut, Date startDate, Date endDate);
}
