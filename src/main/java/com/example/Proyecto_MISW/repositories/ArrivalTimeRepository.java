package com.example.Proyecto_MISW.repositories;

import com.example.Proyecto_MISW.entities.ArrivalTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArrivalTimeRepository extends JpaRepository<ArrivalTime, Long> {
}
