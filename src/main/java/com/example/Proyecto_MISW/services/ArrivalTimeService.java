package com.example.Proyecto_MISW.services;

import com.example.Proyecto_MISW.entities.ArrivalTime;
import com.example.Proyecto_MISW.repositories.ArrivalTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ArrivalTimeService {

    @Autowired
    private ArrivalTimeRepository arrivalTimeRepository;

    public void saveArrivalTime(String rut, LocalDate date, LocalTime arrivalTime) {
        ArrivalTime arrival = new ArrivalTime();
        arrival.setRut(rut);
        arrival.setDate(java.sql.Date.valueOf(date));
        arrival.setArrival_time(arrivalTime);

        arrivalTimeRepository.save(arrival);
    }
    public List<ArrivalTime> getAllArrivalTimes() {
        return arrivalTimeRepository.findAll();
    }

}
