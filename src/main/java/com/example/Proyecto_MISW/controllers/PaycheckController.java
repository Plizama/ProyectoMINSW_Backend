package com.example.Proyecto_MISW.controllers;

import com.example.Proyecto_MISW.entities.Paycheck;
import com.example.Proyecto_MISW.services.PaycheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paychecks")
public class PaycheckController {
    @Autowired
    private PaycheckService paycheckService;

    // Endpoint para calcular las nóminas para un mes y año específicos con respuesta vacía
    @GetMapping("/calculate")
    public ResponseEntity<Void> calculatePaychecks(@RequestParam("year") int year, @RequestParam("month") int month) {
        // Llama al servicio para calcular las nóminas
        paycheckService.calculatePaychecks(month, year);

        // Retorna una respuesta sin contenido
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/")
    public ResponseEntity<Paycheck> getPaycheckDetails(@RequestParam("rut") String rut,
                                                       @RequestParam("year") int year,
                                                       @RequestParam("month") int month) {
        Paycheck paycheck = paycheckService.getPaycheckByRutAndMonth(rut, month, year);

        if (paycheck != null) {
            return ResponseEntity.ok(paycheck);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
