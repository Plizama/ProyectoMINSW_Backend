package com.example.Proyecto_MISW.services;

import com.example.Proyecto_MISW.entities.DepartureTime;
import com.example.Proyecto_MISW.repositories.DepartureTimeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@SpringBootTest
public class DepartureTimeServiceTest {

    @Mock
    private DepartureTimeRepository departureTimeRepository;

    @InjectMocks
    private DepartureTimeService departureTimeService;

    public DepartureTimeServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    // Test para: public void saveDepartureTime(String rut, LocalDate date, LocalTime departureTime)
    //Guarda datos de salida.
    @Test
    public void testSaveDepartureTime() {
        //Creacion de datos
        String rut = "12.345.678-9";
        LocalDate date = LocalDate.of(2024, 9, 19);
        LocalTime departureTime = LocalTime.of(18, 0);

        departureTimeService.saveDepartureTime(rut, date, departureTime);

        //Verifica si es llamado el repositorio y si almacena los datos
        verify(departureTimeRepository, times(1)).save(argThat(departure ->
                departure.getRut().equals(rut) &&
                        departure.getDate().equals(java.sql.Date.valueOf(date)) &&
                        departure.getDeparture_time().equals(departureTime)
        ));
    }

    // Test para: public List<DepartureTime> getAllDepartureTimes()
    //Obtiene listado de salida guardadas
    @Test
    public void testGetAllDepartureTimes_withDepartureTimes() {
        //Creacion de listado de Salidas
        DepartureTime departure1 = new DepartureTime();
        departure1.setRut("12.345.678-9");
        departure1.setDate(java.sql.Date.valueOf(LocalDate.of(2024, 9, 19)));
        departure1.setDeparture_time(LocalTime.of(18, 0));

        DepartureTime departure2 = new DepartureTime();
        departure2.setRut("7.654.321-0");
        departure2.setDate(java.sql.Date.valueOf(LocalDate.of(2024, 9, 20)));
        departure2.setDeparture_time(LocalTime.of(18, 15));

        List<DepartureTime> departureTimes = new ArrayList<>();
        departureTimes.add(departure1);
        departureTimes.add(departure2);

        given(departureTimeRepository.findAll()).willReturn(departureTimes);

        //Verifica que contenga salidas creada anteriormente
        List<DepartureTime> result = departureTimeService.getAllDepartureTimes();
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(2);
        assertThat(result).extracting(DepartureTime::getRut).contains("12.345.678-9", "7.654.321-0");
    }

    // Obtiene listado vacío
    @Test
    public void testGetAllDepartureTimes_withEmptyList() {
        List<DepartureTime> departureTimes = Collections.emptyList();


        given(departureTimeRepository.findAll()).willReturn(departureTimes);


        List<DepartureTime> result = departureTimeService.getAllDepartureTimes();

        assertThat(result).isEmpty();
        assertThat(result).hasSize(0);
    }
}
