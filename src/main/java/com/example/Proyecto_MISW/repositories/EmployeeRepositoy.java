package com.example.Proyecto_MISW.repositories;

import com.example.Proyecto_MISW.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface EmployeeRepositoy extends JpaRepository<Employee, Long> {
   public Employee findByRut(String rut);

   // Método que devuelve solo la categoría del empleado basado en el RUT
   @Query("SELECT e.category FROM Employee e WHERE e.rut = :rut")
   public String findCategoryByRut(@Param("rut") String rut);
    // Método que devuelve la fecha de registro (registration_date) de un empleado basado en el RUT
    @Query("SELECT e.registration_date FROM Employee e WHERE e.rut = :rut")
    public Date findRegistrationDateByRut(@Param("rut") String rut);
}
