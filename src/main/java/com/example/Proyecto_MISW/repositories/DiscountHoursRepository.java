package com.example.Proyecto_MISW.repositories;

import com.example.Proyecto_MISW.entities.DiscountHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DiscountHoursRepository extends JpaRepository<DiscountHours, Long> {
    public List<DiscountHours> findByRut(String rut);
    List<DiscountHours> findByRutAndDate(String rut, Date date);
    List<DiscountHours> findByRutAndDateBetween(String rut, Date startDate, Date endDate);

}
