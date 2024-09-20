package com.example.Proyecto_MISW.services;

import com.example.Proyecto_MISW.entities.ExtraHours;
import com.example.Proyecto_MISW.repositories.ExtraHoursRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ExtraHoursService {

    @Autowired
    private ExtraHoursRepository extraHoursRepository;

    public void saveExtraHours(ExtraHours extraHours) {
        extraHoursRepository.save(extraHours);
    }
    public void approveExtraHoursByRutAndDate(String rut, Date date) {
        // Obtener los registros con el RUT y la fecha proporcionada
        List<ExtraHours> extraHoursList = extraHoursRepository.findByRutAndDate(rut, date);

        // Cambiar el campo approval a true para cada registro
        for (ExtraHours extraHours : extraHoursList) {
            if (!extraHours.isApproval()) {
                extraHours.setApproval(true);
                extraHoursRepository.save(extraHours);
            }
        }
    }
    // Función para sumar horas extra aprobadas en un mes y año específicos . Formato : ("12345678-9", 10, 2024)
    public int getTotalApprovedExtraHoursByRutAndMonth(String rut, int month, int year) {
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

        // Obtener todas las horas extra entre las fechas de inicio y fin del mes especificado
        List<ExtraHours> extraHoursList = extraHoursRepository.findByRutAndDateBetween(rut, startDate, endDate);

        // Sumar solo las horas extra aprobadas
        int totalApprovedExtraHours = 0;
        for (ExtraHours extraHours : extraHoursList) {
            if (extraHours.isApproval()) {  // Solo sumar las horas extra aprobadas
                totalApprovedExtraHours += extraHours.getNumExtraHours();
            }
        }

        return totalApprovedExtraHours;
    }
}
