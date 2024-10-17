package com.example.Proyecto_MISW.repositories;

import com.example.Proyecto_MISW.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface EmployeeRepositoy extends JpaRepository<Employee, Long> {

    //Buscar empeado segun el rut
   public Employee findByRut(String rut);

   // Categoría del empleado segun el RUT
   @Query("SELECT e.category FROM Employee e WHERE e.rut = :rut")
   public String findCategoryByRut(@Param("rut") String rut);

    // Fecha de registro (registration_date) de un empleado segun el RUT
    @Query("SELECT e.registration_date FROM Employee e WHERE e.rut = :rut")
    public Date findRegistrationDateByRut(@Param("rut") String rut);
}
