package com.example.Proyecto_MISW.controllers;

import com.example.Proyecto_MISW.services.AccessControlSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/access-control")
public class AccessControlSystemController {

    @Autowired
    private AccessControlSystemService accessControlSystemService;

    // Endpoint para procesar el archivo de control de acceso a partir de un archivo subido
    @PostMapping("/process-file")
    public String processAccessFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "Error: No se ha subido ningún archivo.";
        }

        try {
            // Llamar al servicio para procesar el archivo utilizando InputStream
            accessControlSystemService.processAccessFile(file.getInputStream());
            return "Archivo procesado con éxito.";

        } catch (IOException e) {
            return "Error al procesar el archivo: " + e.getMessage();
        }
    }
}
