package com.example.Proyecto_MISW.services;

import com.example.Proyecto_MISW.entities.DiscountHours;
import com.example.Proyecto_MISW.repositories.DiscountHoursRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Calendar;
@Service
public class DiscountHoursService {
    @Autowired
    DiscountHoursRepository discountHoursRepository;

    public List<DiscountHours> getDiscountHoursByRut(String rut){
        return (List<DiscountHours>) discountHoursRepository.findByRut(rut);
    }

    public void saveDiscountHours(DiscountHours discountHours) {
        discountHoursRepository.save(discountHours);
    }
    // Método para cambiar approval a true por RUT
    public void approveDiscountHoursByRutAndDate(String rut, Date date) {
        // Obtener los registros con el RUT y la fecha proporcionada
        List<DiscountHours> discountHoursList = discountHoursRepository.findByRutAndDate(rut, date);

        // Cambiar el campo approval a true para cada registro
        for (DiscountHours discountHours : discountHoursList) {
            if (!discountHours.isApproval()) {
                discountHours.setApproval(true);
                discountHoursRepository.save(discountHours);
            }
        }
    }
    // Función para sumar las horas de descuento no aprobadas en un mes y año específicos
    // Solo revisar approval si numDiscountHours supera 70
    public int getTotalUnapprovedDiscountHoursByRutAndMonth(String rut, int month, int year) {
        // Ajustar los parámetros para trabajar con Calendar (meses empiezan desde 0 en Java)
        Calendar calendar = Calendar.getInstance();

        // Establecer el año y el mes (restar 1 a month porque Calendar usa 0 para enero)
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);

        // Establecer el primer día del mes
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startDate = calendar.getTime();

        // Establecer el último día del mes
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date endDate = calendar.getTime();

        // Obtener todas las horas de descuento entre las fechas de inicio y fin del mes especificado
        List<DiscountHours> discountHoursList = discountHoursRepository.findByRutAndDateBetween(rut, startDate, endDate);

        // Sumar solo las horas de descuento que cumplan la condición
        int totalDiscountHours = 0;
        for (DiscountHours discountHours : discountHoursList) {
            int discountHoursInDay = discountHours.getNumDiscountHours();

            if (discountHoursInDay > 70) {
                // Si es mayor a 70, solo sumar si approval es false
                if (!discountHours.isApproval()) {
                    totalDiscountHours += discountHoursInDay;
                }
            } else {
                // Si es 70 o menor, sumar directamente sin revisar approval
                totalDiscountHours += discountHoursInDay;
            }
        }

        return totalDiscountHours;
    }


}
