package com.example.Proyecto_MISW.services;

import com.example.Proyecto_MISW.entities.DiscountHours;
import com.example.Proyecto_MISW.repositories.DiscountHoursRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DiscountHoursService {
    @Autowired
    DiscountHoursRepository discountHoursRepository;
    public ArrayList<DiscountHours> getDiscountHours(){
        return (ArrayList<DiscountHours>) discountHoursRepository.findAll();
    }

    public List<DiscountHours> getDiscountHoursByRut(String rut){
        return (List<DiscountHours>) discountHoursRepository.findByRut(rut);
    }


}
