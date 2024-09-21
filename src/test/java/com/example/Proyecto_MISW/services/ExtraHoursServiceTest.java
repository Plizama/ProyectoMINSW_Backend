package com.example.Proyecto_MISW.services;

import com.example.Proyecto_MISW.entities.ExtraHours;
import com.example.Proyecto_MISW.repositories.ExtraHoursRepository;
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
public class ExtraHoursServiceTest {

    @Mock
    private ExtraHoursRepository extraHoursRepository;

    @InjectMocks
    private ExtraHoursService extraHoursService;

    public ExtraHoursServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    // Test para: public void saveExtraHours(ExtraHours extraHours)
    //Guarda algunas horas extra
    @Test
    public void saveSomeExtraHours() {
        ExtraHours extraHours = new ExtraHours();
        extraHours.setRut("12.345.678-9");
        extraHours.setNumExtraHours(4);

        extraHoursService.saveExtraHours(extraHours);

        verify(extraHoursRepository, times(1)).save(extraHours);
    }

    // Test para: public void approveExtraHoursByRutAndDate(String rut, Date date)
    //Aprobar horas extra con rut y fecha correctas
    @Test
    public void approveExtraHoursWithcorrectRutAndDate() {
        String rut = "12.345.678-9";
        Date date = new Date();

        ExtraHours extra1 = new ExtraHours();
        extra1.setRut(rut);
        extra1.setDate(date);
        extra1.setApproval(false);

        List<ExtraHours> extraHoursList = Collections.singletonList(extra1);

        given(extraHoursRepository.findByRutAndDate(rut, date)).willReturn(extraHoursList);

        extraHoursService.approveExtraHoursByRutAndDate(rut, date);

        assertThat(extra1.isApproval()).isTrue();
        verify(extraHoursRepository, times(1)).save(extra1);
    }

    // Aprobar horas extra con rut incorrecto

    @Test
    public void approveExtraHoursWithIncorrectRut() {

        String storedRut = "12.345.678-9";
        Date date = new Date();
        ExtraHours extra1 = new ExtraHours();
        extra1.setRut(storedRut);
        extra1.setDate(date);
        extra1.setNumExtraHours(5);
        extra1.setApproval(false);

        List<ExtraHours> extraHoursList = Collections.singletonList(extra1);

        given(extraHoursRepository.findByRutAndDate(storedRut, date)).willReturn(extraHoursList);

        String incorrectRut = "11.555.444-9";

        given(extraHoursRepository.findByRutAndDate(incorrectRut, date)).willReturn(Collections.emptyList());

        extraHoursService.approveExtraHoursByRutAndDate(incorrectRut, date);

        verify(extraHoursRepository, times(0)).save(any(ExtraHours.class));
    }


    // Aprobar horas extra con fecha que no existe
    @Test
    public void testApproveExtraHoursByRutAndDate_nonExistentDate() {
        String rut = "12.345.678-9";
        Date storedDate = new Date();
        ExtraHours extra1 = new ExtraHours();
        extra1.setRut(rut);
        extra1.setDate(storedDate);
        extra1.setNumExtraHours(5);
        extra1.setApproval(false);

        List<ExtraHours> extraHoursList = Collections.singletonList(extra1);

        given(extraHoursRepository.findByRutAndDate(rut, storedDate)).willReturn(extraHoursList);

        Date nonExistentDate = new Date(2025-01-12);

        given(extraHoursRepository.findByRutAndDate(rut, nonExistentDate)).willReturn(Collections.emptyList());

        extraHoursService.approveExtraHoursByRutAndDate(rut, nonExistentDate);

        verify(extraHoursRepository, times(0)).save(any(ExtraHours.class));
    }

    // Test para: public int getTotalApprovedExtraHoursByRutAndMonth(String rut, int month, int year)
    //Obtener suma de horas, todas aprobadas.
    @Test
    public void getTotalApprovedExtraHoursByRutAndMonth_allApproved() {
        String rut = "12.345.678-9";
        int month = 9;
        int year = 2024;
        ExtraHours extra1 = new ExtraHours();
        extra1.setRut(rut);
        extra1.setNumExtraHours(5);
        extra1.setApproval(true);
        ExtraHours extra2 = new ExtraHours();
        extra2.setRut(rut);
        extra2.setNumExtraHours(3);
        extra2.setApproval(true);

        List<ExtraHours> extraHoursList = new ArrayList<>();
        extraHoursList.add(extra1);
        extraHoursList.add(extra2);

        given(extraHoursRepository.findByRutAndDateBetween(any(), any(), any())).willReturn(extraHoursList);

        int totalExtraHours = extraHoursService.getTotalApprovedExtraHoursByRutAndMonth(rut, month, year);

        assertThat(totalExtraHours).isEqualTo(8);
    }

    // Obtener suma de horas extra, algunas aprobadas, otras no
    @Test
    public void testGetTotalApprovedExtraHoursByRutAndMonth_someApproved() {
        String rut = "12.345.678-9";
        int month = 9;
        int year = 2024;

        ExtraHours extra1 = new ExtraHours();
        extra1.setRut(rut);
        extra1.setNumExtraHours(5);
        extra1.setApproval(true);
        ExtraHours extra2 = new ExtraHours();
        extra2.setRut(rut);
        extra2.setNumExtraHours(3);
        extra2.setApproval(false);

        List<ExtraHours> extraHoursList = new ArrayList<>();
        extraHoursList.add(extra1);
        extraHoursList.add(extra2);

        given(extraHoursRepository.findByRutAndDateBetween(any(), any(), any())).willReturn(extraHoursList);

        int totalExtraHours = extraHoursService.getTotalApprovedExtraHoursByRutAndMonth(rut, month, year);

        assertThat(totalExtraHours).isEqualTo(5);
    }

    // Suma de horas extra, ninguna aprobada.
    @Test
    public void testGetTotalApprovedExtraHoursByRutAndMonth_noneApproved() {
        String rut = "12.345.678-9";
        int month = 9;
        int year = 2024;

        ExtraHours extra1 = new ExtraHours();
        extra1.setRut(rut);
        extra1.setNumExtraHours(5);
        extra1.setApproval(false);

        ExtraHours extra2 = new ExtraHours();
        extra2.setRut(rut);
        extra2.setNumExtraHours(3);
        extra2.setApproval(false);

        List<ExtraHours> extraHoursList = new ArrayList<>();
        extraHoursList.add(extra1);
        extraHoursList.add(extra2);

        given(extraHoursRepository.findByRutAndDateBetween(any(), any(), any())).willReturn(extraHoursList);

        int totalExtraHours = extraHoursService.getTotalApprovedExtraHoursByRutAndMonth(rut, month, year);

        assertThat(totalExtraHours).isEqualTo(0);
    }
}
