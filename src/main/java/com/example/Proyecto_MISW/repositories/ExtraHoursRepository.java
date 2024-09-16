package com.example.Proyecto_MISW.repositories;

import com.example.Proyecto_MISW.entities.ExtraHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExtraHoursRepository extends JpaRepository<ExtraHours, Long> {
    public List<ExtraHours> findByRut(String rut);

}
