package com.example.Proyecto_MISW.controllers;

import com.example.Proyecto_MISW.services.AccessControlSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/access-control")
@CrossOrigin("*")
public class AccessControlSystemController {

    @Autowired
    private AccessControlSystemService accessControlSystemService;

    // Controlador procesa archivo y llama accessControlSystemService
    @PostMapping("/process-file")
    public ResponseEntity<Void> processAccessFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            // Retorna un 400 Bad Request si no se ha subido ningún archivo
            return ResponseEntity.badRequest().build();
        }
        try {
            accessControlSystemService.processAccessFile(file.getInputStream());

            // Retorna un 204 No Content si el archivo se procesó correctamente
            return ResponseEntity.noContent().build();

        } catch (IOException e) {
            // Retorna un 500 Internal Server Error si ocurre un error durante el procesamiento
            return ResponseEntity.status(500).build();
        }
    }

    //Controlador llama servicio que procesa horas extra.
    @PostMapping("/process-extra-hours")
    public ResponseEntity<Void> processExtraHours() {
        accessControlSystemService.processDepartureTimesForExtraHours();
        return ResponseEntity.noContent().build();
    }
    //Controlador llama servicio que procesa descuentos.
    @PostMapping("/process-discounts")
    public ResponseEntity<Void> processDiscounts() {
        accessControlSystemService.processArrivalTimesForDiscounts();
        return ResponseEntity.noContent().build();
    }

}
