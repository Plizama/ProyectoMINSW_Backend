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

    // Controlador llama a funcion que calcula nominas segun mes y año
    @GetMapping("/calculate/{year}/{month}")
    public ResponseEntity<Void> calculatePaychecks(@PathVariable int year, @PathVariable int month) {
        paycheckService.calculatePaychecks(month, year);
        return ResponseEntity.noContent().build();
    }
    //Controlador llama a funcion que muestra detalles de nominas de pago
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
