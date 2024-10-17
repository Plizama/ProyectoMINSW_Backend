package com.example.Proyecto_MISW.services;

import com.example.Proyecto_MISW.entities.*;
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
    private ArrivalTimeService arrivalTimeService;

    @Autowired
    private DepartureTimeService departureTimeService;

    @Autowired
    private DiscountHoursService discountHoursService;
    @Autowired
    private ExtraHoursService extraHoursService;

    @Autowired
    private EmployeeService employeeService;

    //Procesar archivo, y almacenar horas de ingreso y salida, además de identificar trabajadores que no asistieron
    public void processAccessFile(InputStream inputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            //Lee archivo por lineas
            List<String> lines = reader.lines().collect(Collectors.toList());
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            // Mapeo para ingresar horas de entrada y salida a partir de rut y fecha.
            Map<String, List<LocalTime>> accessMap = new HashMap<>();
            //Mapeo para ingresar empleados en cada fecha
            Map<String, Set<String>> employeeAccessByDate = new HashMap<>();

            // Obtener la lista de todos los empleados
            List<Employee> employees = employeeService.getEmployees();
            //Se obtiene rut de cada empleado desde conjunto
            Set<String> allRuts = employees.stream().map(Employee::getRut).collect(Collectors.toSet());

            // Recorrer todas las líneas del archivo
            for (String line : lines) {
                String[] fields = line.split(";");
                LocalDate date = LocalDate.parse(fields[0], dateFormatter);
                LocalTime time = LocalTime.parse(fields[1], timeFormatter);
                String rut = fields[2];

                // Generar clave única por RUT y fecha
                String key = rut + "_" + date.toString();

                // Añadir la hora de ingreso/salida al mapeo
                accessMap.computeIfAbsent(key, k -> new ArrayList<>()).add(time);

                // Almacenar RUT por fecha
                String dateKey = date.toString();
                //agrega la hora de acceso al Map accessMap
                employeeAccessByDate.computeIfAbsent(dateKey, k -> new HashSet<>()).add(rut);
            }

            // Procesar cada trabajador
            for (Map.Entry<String, List<LocalTime>> entry : accessMap.entrySet()) {
                //separa fecha de rut
                String[] keyParts = entry.getKey().split("_");
                String rut = keyParts[0];
                LocalDate date = LocalDate.parse(keyParts[1]);
                // lista de horas del ruy y la fecha
                List<LocalTime> times = entry.getValue();
                // si hay dos horas una es entrada y la otra salida
                if (times.size() >= 2) {
                    // Ordenar las horas por si están desordenadas
                    Collections.sort(times);

                    // La primera hora es el ingreso, la segunda es la salida
                    LocalTime ingresoTime = times.get(0);
                    LocalTime salidaTime = times.get(1);

                    // Almacenar horas de entrada y salida utilizando los servicios
                    arrivalTimeService.saveArrivalTime(rut, date, ingresoTime);
                    departureTimeService.saveDepartureTime(rut, date, salidaTime);
                }
            }

            // Verificar que todos los empleados tengan registros por fecha (para casos de ausencia)
            for (Map.Entry<String, Set<String>> dateEntry : employeeAccessByDate.entrySet()) {
                LocalDate date = LocalDate.parse(dateEntry.getKey());

                // Comparar RUTs registrados ese día con todos los RUTs de empleados
                Set<String> rutsForThatDate = dateEntry.getValue();
                for (String rut : allRuts) {
                    if (!rutsForThatDate.contains(rut)) {
                        // Si falta un RUT, agregar un registro en DiscountHours con 600 minutos
                        DiscountHours discountHours = new DiscountHours();
                        discountHours.setRut(rut);
                        discountHours.setDate(java.sql.Date.valueOf(date));
                        discountHours.setNumDiscountHours(600); // 600 minutos de descuento
                        discountHours.setApproval(false);  // Por defecto no aprobado

                        // Guardar el registro de descuento
                        discountHoursService.saveDiscountHours(discountHours);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Procesa horas de llegada para calcular horas de descuento por atrasos
    public void processArrivalTimesForDiscounts() {
        // Obtener todos los registros de llegada
        List<ArrivalTime> arrivalTimes = arrivalTimeService.getAllArrivalTimes();

        // Hora de inicio del trabajo (08:00)
        LocalTime startWorkTime = LocalTime.of(8, 0);

        // Procesar cada llegada
        for (ArrivalTime arrival : arrivalTimes) {
            LocalTime arrivalTime = arrival.getArrival_time();

            // Si la hora de llegada es posterior a las 08:00
            if (arrivalTime.isAfter(startWorkTime)) {
                // Calcular los minutos de atraso
                long minutesLate = Duration.between(startWorkTime, arrivalTime).toMinutes();

                // Crear un nuevo registro de DiscountHours
                DiscountHours discountHours = new DiscountHours();
                discountHours.setRut(arrival.getRut());
                discountHours.setDate(arrival.getDate());
                discountHours.setNumDiscountHours((int) minutesLate);
                discountHours.setApproval(false);  // Por defecto, no aprobado

                // Guardar el registro en la base de datos
                discountHoursService.saveDiscountHours(discountHours);
            }
        }
    }
    public void processDepartureTimesForExtraHours() {
        // Obtener todos los registros de salida
        List<DepartureTime> departureTimes = departureTimeService.getAllDepartureTimes();

        // Definir la hora de finalización del trabajo (18:00)
        LocalTime endWorkTime = LocalTime.of(18, 0);

        // Procesar cada registro de salida
        for (DepartureTime departure : departureTimes) {
            LocalTime departureTime = departure.getDeparture_time();

            // Si la hora de salida es posterior a las 18:00
            if (departureTime.isAfter(endWorkTime)) {
                // Calcular los minutos de horas extra
                long extraMinutes = Duration.between(endWorkTime, departureTime).toMinutes();

                // Crear un nuevo registro de ExtraHours
                ExtraHours extraHours = new ExtraHours();
                extraHours.setRut(departure.getRut());
                extraHours.setDate(departure.getDate());
                extraHours.setNumExtraHours((int) extraMinutes);
                extraHours.setApproval(false);  // Por defecto, no aprobado

                // Guardar el registro en la base de datos
                extraHoursService.saveExtraHours(extraHours);
            }
        }
    }

}



