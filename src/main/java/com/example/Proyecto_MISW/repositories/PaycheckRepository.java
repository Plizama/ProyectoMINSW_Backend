package com.example.Proyecto_MISW.repositories;

import com.example.Proyecto_MISW.entities.Paycheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaycheckRepository extends JpaRepository<Paycheck, Long> {
    //lista de pagos segun fecha (mes y año)
    @Query(value = "SELECT * FROM paychecks WHERE paychecks.year = :year AND paychecks.month = :month ORDER BY paychecks.year, paychecks.month, paychecks.rut", nativeQuery = true)
    List<Paycheck> getPaychecksByYearMonth(@Param("year") int year, @Param("month") int month);
    //Pago segun mes, año y rut
    Paycheck findByRutAndMonthAndYear(String rut, int month, int year);

}
