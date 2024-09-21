package com.example.Proyecto_MISW.controllers;

import com.example.Proyecto_MISW.services.DiscountHoursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/discountHours")
@CrossOrigin("*")
public class DiscountHoursController {
    @Autowired
    private DiscountHoursService discountHoursService;

    // Endpoint para aprobar las horas de descuento por RUT formato: ...approve/12.345.678-9/2024-09-16
    @PutMapping("/approve/{rut}/{date}")
    public ResponseEntity<Void> approveDiscountHours(@PathVariable String rut,
                                                     @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        discountHoursService.approveDiscountHoursByRutAndDate(rut, date);
        // Retorna un ResponseEntity vacío indicando éxito (204 No Content)
        return ResponseEntity.noContent().build();
    }
}
