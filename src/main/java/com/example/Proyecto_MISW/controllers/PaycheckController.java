package com.example.Proyecto_MISW.controllers;

import com.example.Proyecto_MISW.entities.Paycheck;
import com.example.Proyecto_MISW.services.PaycheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paychecks")
@CrossOrigin("*")
public class PaycheckController {
    @Autowired
    private PaycheckService paycheckService;

    // Endpoint para calcular las nóminas para un mes y año específicos con respuesta vacía
    @GetMapping("/calculate/{year}/{month}")
    public ResponseEntity<Void> calculatePaychecks(@PathVariable int year, @PathVariable int month) {
        paycheckService.calculatePaychecks(month, year);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/paycheck/{rut}/{year}/{month}")
    public ResponseEntity<Paycheck> getPaycheckDetails(@PathVariable String rut,
                                                       @PathVariable int year,
                                                       @PathVariable int month) {
        Paycheck paycheck = paycheckService.getPaycheckByRutAndMonth(rut, month, year);

        if (paycheck != null) {
            return ResponseEntity.ok(paycheck);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
