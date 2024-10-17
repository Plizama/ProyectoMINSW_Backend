package com.example.Proyecto_MISW.controllers;

import com.example.Proyecto_MISW.entities.DiscountHours;
import com.example.Proyecto_MISW.services.DiscountHoursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/discountHours")
@CrossOrigin("*")
public class DiscountHoursController {
    @Autowired
    private DiscountHoursService discountHoursService;

    // Controlador para aprobar horas de descuento. formato: ...approve/12.345.678-9/2024-09-16
    @PutMapping("/approve/{rut}/{date}")
    public ResponseEntity<Void> approveDiscountHours(@PathVariable String rut,
                                                     @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        discountHoursService.approveDiscountHoursByRutAndDate(rut, date);
        return ResponseEntity.noContent().build();
    }
    //Controlador obtiene descuentos segun rut, mes y año
    @GetMapping("/getByRutAndMonth/{rut}/{month}/{year}")
    public ResponseEntity<List<DiscountHours>> getDiscountHoursByRutAndMonth(
            @PathVariable String rut,
            @PathVariable int month,
            @PathVariable int year) {

        List<DiscountHours> discountHoursList = discountHoursService.getDiscountHoursByRutAndMonth(rut, month, year);

        if (discountHoursList.isEmpty()) {
            return ResponseEntity.status(404).body(null);
        }

        return ResponseEntity.ok(discountHoursList);
    }
}
