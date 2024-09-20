package com.example.Proyecto_MISW.repositories;

import com.example.Proyecto_MISW.entities.DepartureTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartureTimeRepository extends JpaRepository<DepartureTime, Long> {
}
