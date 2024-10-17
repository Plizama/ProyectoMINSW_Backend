package com.example.Proyecto_MISW.controllers;

import com.example.Proyecto_MISW.entities.ExtraHours;
import com.example.Proyecto_MISW.services.ExtraHoursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/extraHours")
@CrossOrigin("*")
public class ExtraHoursController {

    @Autowired
    private ExtraHoursService extraHoursService;

    // Controlador aprueba horas estra segun rut y fecha, formato: ...approve/12.345.678-9/2024-09-16
    @PutMapping("/approve/{rut}/{date}")
    public ResponseEntity<Void> approveExtraHours(@PathVariable String rut,
                                                  @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        extraHoursService.approveExtraHoursByRutAndDate(rut, date);
        // Retorna un ResponseEntity vacío indicando éxito (204 No Content)
        return ResponseEntity.noContent().build();
    }

    //Controlador realiza llamado a funcion que obtiene horas extra con rut, mes y año
    @GetMapping("/getByRutAndMonth/{rut}/{month}/{year}")
    public ResponseEntity<List<ExtraHours>> getExtraHoursByRutAndMonth(
            @PathVariable String rut,
            @PathVariable int month,
            @PathVariable int year) {

        List<ExtraHours> extraHoursList = extraHoursService.getExtraHoursByRutAndMonth(rut, month, year);

        // Devolver un 404 si no se encuentran registros
        if (extraHoursList.isEmpty()) {
            return ResponseEntity.status(404).body(null);
        }

        return ResponseEntity.ok(extraHoursList);
    }

}
