package com.example.Proyecto_MISW.services;

import com.example.Proyecto_MISW.entities.DepartureTime;
import com.example.Proyecto_MISW.repositories.DepartureTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class DepartureTimeService {

    @Autowired
    private DepartureTimeRepository departureTimeRepository;

    public void saveDepartureTime(String rut, LocalDate date, LocalTime departureTime) {
        DepartureTime departure = new DepartureTime();
        departure.setRut(rut);
        departure.setDate(java.sql.Date.valueOf(date));
        departure.setDeparture_time(departureTime);

        departureTimeRepository.save(departure);
    }
    public List<DepartureTime> getAllDepartureTimes() {
        return departureTimeRepository.findAll();
    }
}
