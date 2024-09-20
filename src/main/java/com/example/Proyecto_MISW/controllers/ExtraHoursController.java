package com.example.Proyecto_MISW.controllers;

import com.example.Proyecto_MISW.services.ExtraHoursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/extraHours")
@CrossOrigin("*")
public class ExtraHoursController {

    @Autowired
    private ExtraHoursService extraHoursService;

    // Endpoint para aprobar las horas extra por RUT y fecha, formato: ...approve/12.345.678-9/2024-09-16
    @PutMapping("/approve/{rut}/{date}")
    public void approveExtraHours(@PathVariable String rut,
                                  @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        extraHoursService.approveExtraHoursByRutAndDate(rut, date);
    }
}
