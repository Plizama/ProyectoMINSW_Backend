package com.example.Proyecto_MISW.services;

import com.example.Proyecto_MISW.entities.DiscountHours;
import com.example.Proyecto_MISW.repositories.DiscountHoursRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Calendar;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@SpringBootTest
public class DiscountHoursServiceTest {

    @Mock
    private DiscountHoursRepository discountHoursRepository;

    @InjectMocks
    private DiscountHoursService discountHoursService;

    public DiscountHoursServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    //Test para: public List<DiscountHours> getDiscountHoursByRut(String rut)
    // RUT existente
    @Test
    public void existingRut() {
        String rut = "12.345.678-9";

        DiscountHours discount1 = new DiscountHours();
        discount1.setRut(rut);
        DiscountHours discount2 = new DiscountHours();
        discount2.setRut(rut);

        List<DiscountHours> discountHoursList = new ArrayList<>();
        discountHoursList.add(discount1);
        discountHoursList.add(discount2);

        given(discountHoursRepository.findByRut(rut)).willReturn(discountHoursList);

        List<DiscountHours> result = discountHoursService.getDiscountHoursByRut(rut);

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(2);
        assertThat(result).extracting(DiscountHours::getRut).contains(rut);
    }

    //RUT no existente
    @Test
    public void nonExistingRut() {
        String rut = "non.existent.rut";

        List<DiscountHours> discountHoursList = Collections.emptyList();

        given(discountHoursRepository.findByRut(rut)).willReturn(discountHoursList);

        List<DiscountHours> result = discountHoursService.getDiscountHoursByRut(rut);

        assertThat(result).isEmpty();
    }

    // Test para: public void saveDiscountHours(DiscountHours discountHours)
    // Guardar Horas de descuento
    @Test
    public void saveSomeDiscountHours() {
        DiscountHours discountHours = new DiscountHours();
        discountHours.setRut("12.345.678-9");
        discountHours.setNumDiscountHours(100);

        discountHoursService.saveDiscountHours(discountHours);

        verify(discountHoursRepository, times(1)).save(discountHours);
    }

    // Test para: public void approveDiscountHoursByRutAndDate(String rut, Date date)
    //Aprobar horas descontadas de un rut que existe
    @Test
    public void approveDiscountHoursByExistingRut() {
        String rut = "12.345.678-9";
        Date date = new Date();
        DiscountHours discount1 = new DiscountHours();
        discount1.setRut(rut);
        discount1.setDate(date);
        discount1.setApproval(false);

        List<DiscountHours> discountHoursList = Collections.singletonList(discount1);

        given(discountHoursRepository.findByRutAndDate(rut, date)).willReturn(discountHoursList);

        discountHoursService.approveDiscountHoursByRutAndDate(rut, date);

        assertThat(discount1.isApproval()).isTrue();
        verify(discountHoursRepository, times(1)).save(discount1);
    }

    //Aprobar horas en descuento para rut inexistente
    @Test
    public void approveDiscountHoursByNonExistingRut() {
        String rut = "non.existent.rut";
        Date date = new Date();

        List<DiscountHours> discountHoursList = Collections.emptyList();

        given(discountHoursRepository.findByRutAndDate(rut, date)).willReturn(discountHoursList);

        discountHoursService.approveDiscountHoursByRutAndDate(rut, date);

        verify(discountHoursRepository, times(0)).save(any(DiscountHours.class));
    }

    // Test para: public int getTotalUnapprovedDiscountHoursByRutAndMonth(String rut, int month, int year)
    //Rut y mes no tiene descuento
    @Test
    public void discountHoursByRutAndMonth_noDiscountHours() {
        String rut = "12.345.678-9";
        int month = 9;
        int year = 2024;

        List<DiscountHours> discountHoursList = Collections.emptyList();

        given(discountHoursRepository.findByRutAndDateBetween(any(), any(), any())).willReturn(discountHoursList);

        int totalDiscountHours = discountHoursService.getTotalUnapprovedDiscountHoursByRutAndMonth(rut, month, year);

        assertThat(totalDiscountHours).isEqualTo(0);
    }

    // Empelado con horas de descuento > 70 (no aprobadas)
    @Test
    public void discountHoursByRutAndMonth_withDiscountHoursGreaterThan70_unapproved() {
        String rut = "12.345.678-9";
        int month = 9;
        int year = 2024;

        DiscountHours discount1 = new DiscountHours();
        discount1.setRut(rut);
        discount1.setNumDiscountHours(100);
        discount1.setApproval(false);

        List<DiscountHours> discountHoursList = Collections.singletonList(discount1);

        given(discountHoursRepository.findByRutAndDateBetween(any(), any(), any())).willReturn(discountHoursList);

        int totalDiscountHours = discountHoursService.getTotalUnapprovedDiscountHoursByRutAndMonth(rut, month, year);

        assertThat(totalDiscountHours).isEqualTo(100);
    }

    // Empelado con horas de descuento > 70 (aprobadas)
    @Test
    public void discountHoursByRutAndMonth_withDiscountHoursGreaterThan70_approved() {
        String rut = "12.345.678-9";
        int month = 9;
        int year = 2024;

        DiscountHours discount1 = new DiscountHours();
        discount1.setRut(rut);
        discount1.setNumDiscountHours(100); // Horas > 70
        discount1.setApproval(true);

        List<DiscountHours> discountHoursList = Collections.singletonList(discount1);

        given(discountHoursRepository.findByRutAndDateBetween(any(), any(), any())).willReturn(discountHoursList);

        int totalDiscountHours = discountHoursService.getTotalUnapprovedDiscountHoursByRutAndMonth(rut, month, year);

        assertThat(totalDiscountHours).isEqualTo(0);
    }
}
