package com.example.Proyecto_MISW.services;

import com.example.Proyecto_MISW.entities.DiscountHours;
import com.example.Proyecto_MISW.entities.ExtraHours;
import com.example.Proyecto_MISW.repositories.DiscountHoursRepository;
import com.example.Proyecto_MISW.repositories.ExtraHoursRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AccessControlSystemService {

    @Autowired
    private DiscountHoursRepository discountHoursRepository;

    @Autowired
    private ExtraHoursRepository extraHoursRepository;

    public void processAccessFile(InputStream inputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            List<String> lines = reader.lines().collect(Collectors.toList());
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            // Mapa para almacenar las horas de ingreso y salida por RUT y fecha
            Map<String, List<LocalTime>> accessMap = new HashMap<>();

            // Recorrer todas las líneas
            for (String line : lines) {
                String[] fields = line.split(";");
                LocalDate date = LocalDate.parse(fields[0], dateFormatter);
                LocalTime time = LocalTime.parse(fields[1], timeFormatter);
                String rut = fields[2];

                // Generar clave única por RUT y fecha
                String key = rut + "_" + date.toString();

                // Añadir la hora de ingreso/salida al mapa
                accessMap.computeIfAbsent(key, k -> new ArrayList<>()).add(time);
            }

            // Procesar cada trabajador
            for (Map.Entry<String, List<LocalTime>> entry : accessMap.entrySet()) {
                String[] keyParts = entry.getKey().split("_");
                String rut = keyParts[0];
                LocalDate date = LocalDate.parse(keyParts[1]);

                List<LocalTime> times = entry.getValue();
                if (times.size() >= 2) {
                    // Ordenar las horas por si están desordenadas
                    Collections.sort(times);

                    // La primera hora es el ingreso, la segunda es la salida
                    LocalTime ingresoTime = times.get(0);
                    LocalTime salidaTime = times.get(1);

                    // Procesar horas de ingreso y salida
                    processDiscountHours(rut, date, ingresoTime);
                    processExtraHours(rut, date, salidaTime);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processDiscountHours(String rut, LocalDate date, LocalTime time) {
        LocalTime startWorkTime = LocalTime.of(8, 0);

        if (time.isAfter(startWorkTime)) {
            long minutesLate = Duration.between(startWorkTime, time).toMinutes();

            DiscountHours discountHours = new DiscountHours();
            discountHours.setRut(rut);
            discountHours.setDate(java.sql.Date.valueOf(date));
            discountHours.setNumDiscountHours((int) minutesLate);
            discountHours.setApproval(false);

            discountHoursRepository.save(discountHours);
        }
    }

    private void processExtraHours(String rut, LocalDate date, LocalTime time) {
        LocalTime endWorkTime = LocalTime.of(18, 0);

        if (time.isAfter(endWorkTime)) {
            long extraMinutes = Duration.between(endWorkTime, time).toMinutes();

            ExtraHours extraHours = new ExtraHours();
            extraHours.setRut(rut);
            extraHours.setDate(java.sql.Date.valueOf(date));
            extraHours.setNumExtraHours((int) extraMinutes);
            extraHours.setApproval(false);

            extraHoursRepository.save(extraHours);
        }
    }
}


