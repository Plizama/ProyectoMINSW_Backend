package com.example.Proyecto_MISW.services;

import com.example.Proyecto_MISW.entities.ArrivalTime;
import com.example.Proyecto_MISW.repositories.ArrivalTimeRepository;
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
public class ArrivalTimeServiceTest {

    @Mock
    private ArrivalTimeRepository arrivalTimeRepository;

    @InjectMocks
    private ArrivalTimeService arrivalTimeService;

    public ArrivalTimeServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    // Test para: public void saveArrivalTime(String rut, LocalDate date, LocalTime arrivalTime)
    //Guarda datos de entrada
    @Test
    public void testSaveArrivalTime() {
        String rut = "12.345.678-9";
        LocalDate date = LocalDate.of(2024, 9, 19);
        LocalTime arrivalTime = LocalTime.of(8, 5);

        arrivalTimeService.saveArrivalTime(rut, date, arrivalTime);

        verify(arrivalTimeRepository, times(1)).save(argThat(arrival ->
                arrival.getRut().equals(rut) &&
                        arrival.getDate().equals(java.sql.Date.valueOf(date)) &&
                        arrival.getArrival_time().equals(arrivalTime)
        ));
    }

    // Test para: public List<ArrivalTime> getAllArrivalTimes()
    //Obtiene datos de llegada almacenados en lista
    @Test
    public void testGetAllArrivalTimes_withArrivalTimes() {
        ArrivalTime arrival1 = new ArrivalTime();
        arrival1.setRut("12.345.678-9");
        arrival1.setDate(java.sql.Date.valueOf(LocalDate.of(2024, 9, 19)));
        arrival1.setArrival_time(LocalTime.of(8, 5));

        ArrivalTime arrival2 = new ArrivalTime();
        arrival2.setRut("7.654.321-0");
        arrival2.setDate(java.sql.Date.valueOf(LocalDate.of(2024, 9, 20)));
        arrival2.setArrival_time(LocalTime.of(8, 10));

        List<ArrivalTime> arrivalTimes = new ArrayList<>();
        arrivalTimes.add(arrival1);
        arrivalTimes.add(arrival2);

        given(arrivalTimeRepository.findAll()).willReturn(arrivalTimes);

        List<ArrivalTime> result = arrivalTimeService.getAllArrivalTimes();

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(2);
        assertThat(result).extracting(ArrivalTime::getRut).contains("12.345.678-9", "7.654.321-0");
    }

    // Prueba con lista vacia
    @Test
    public void testGetAllArrivalTimes_withEmptyList() {
        List<ArrivalTime> arrivalTimes = Collections.emptyList();

        given(arrivalTimeRepository.findAll()).willReturn(arrivalTimes);

        List<ArrivalTime> result = arrivalTimeService.getAllArrivalTimes();

        assertThat(result).isEmpty();
        assertThat(result).hasSize(0);
    }
}
