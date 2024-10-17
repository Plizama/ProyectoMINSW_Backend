package com.example.Proyecto_MISW.services;

import com.example.Proyecto_MISW.entities.ExtraHours;
import com.example.Proyecto_MISW.repositories.ExtraHoursRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
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
    //Guarda horas extra
    @Test
    public void saveSomeExtraHours() {
        // Creación de datos
        ExtraHours extraHours = new ExtraHours();
        extraHours.setRut("12.345.678-9");
        extraHours.setDate(new Date());
        extraHours.setNumExtraHours(4);
        extraHours.setApproval(false);

        extraHoursService.saveExtraHours(extraHours);

        //Verifica que repositorio haya almacenado horas y que informacion sea la correcta
        verify(extraHoursRepository, times(1)).save(extraHours);
        assertThat(extraHours.getRut()).isEqualTo("12.345.678-9");
        assertThat(extraHours.getDate()).isNotNull();
        assertThat(extraHours.getNumExtraHours()).isEqualTo(4);
        assertThat(extraHours.isApproval()).isFalse();
    }


    // Test para: public void approveExtraHoursByRutAndDate(String rut, Date date)
    //Aprobar horas extra con rut y fecha correctas
    @Test
    public void approveExtraHoursWithCorrectRutAndDate() {

        //Datos almacenados
        String rut = "12.345.678-9";
        Date date = new Date();
        ExtraHours extra1 = new ExtraHours();
        extra1.setRut(rut);
        extra1.setDate(date);
        extra1.setNumExtraHours(4);
        extra1.setApproval(false);

        List<ExtraHours> extraHoursList = Collections.singletonList(extra1);

        given(extraHoursRepository.findByRutAndDate(rut, date)).willReturn(extraHoursList);

        extraHoursService.approveExtraHoursByRutAndDate(rut, date);

        //Verificar que este aprobada y que se haya almacenado
        assertThat(extra1.isApproval()).isTrue();
        verify(extraHoursRepository, times(1)).save(extra1);
        assertThat(extra1.getRut()).isEqualTo(rut);
        assertThat(extra1.getDate()).isEqualTo(date);
        assertThat(extra1.getNumExtraHours()).isEqualTo(4);
    }


    // Aprobar horas extra con rut incorrecto

    @Test
    public void approveExtraHoursWithIncorrectRut() {
        //Datos consulta
        String incorrectRut = "11.111.111-1";
        //Datos almacenados
        String storedRut = "12.345.678-9";
        Date date = new Date();

        ExtraHours extra1 = new ExtraHours();
        extra1.setRut(storedRut);
        extra1.setDate(date);
        extra1.setNumExtraHours(5);
        extra1.setApproval(false);

        List<ExtraHours> extraHoursList = Collections.singletonList(extra1);

        given(extraHoursRepository.findByRutAndDate(storedRut, date)).willReturn(extraHoursList);

        given(extraHoursRepository.findByRutAndDate(incorrectRut, date)).willReturn(Collections.emptyList());

        extraHoursService.approveExtraHoursByRutAndDate(incorrectRut, date);

        //Verificar repositorio no encontro rut y no cambio su aprobacion (extra1)
        verify(extraHoursRepository, times(0)).save(any(ExtraHours.class));
        assertThat(extra1.isApproval()).isFalse();
    }

    // Aprobar horas extra con fecha que no existe
    @Test
    public void testApproveExtraHoursByRutAndDate_nonExistentDate() {
        //Datos consultados
        Date nonExistentDate = new Date(2025 - 1900, Calendar.JANUARY, 12);

        //Datos almacenados
        String rut = "12.345.678-9";
        Date storedDate = new Date();

        ExtraHours extra1 = new ExtraHours();
        extra1.setRut(rut);
        extra1.setDate(storedDate);
        extra1.setNumExtraHours(5);
        extra1.setApproval(false);

        List<ExtraHours> extraHoursList = Collections.singletonList(extra1);

        given(extraHoursRepository.findByRutAndDate(rut, storedDate)).willReturn(extraHoursList);

        given(extraHoursRepository.findByRutAndDate(rut, nonExistentDate)).willReturn(Collections.emptyList());

        extraHoursService.approveExtraHoursByRutAndDate(rut, nonExistentDate);

        // verificar no encontro fecha
        verify(extraHoursRepository, times(0)).save(any(ExtraHours.class));
        assertThat(extra1.isApproval()).isFalse();
    }


    // Test para: public int getTotalApprovedExtraHoursByRutAndMonth(String rut, int month, int year)
    //Obtener suma de horas, todas aprobadas.
    @Test
    public void getTotalApprovedExtraHoursByRutAndMonth_allApproved() {

        //Crear datos
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

        //Veriicar que las horas sumadas sean 8
        assertThat(totalExtraHours).isEqualTo(8);
    }

    // Obtener suma de horas extra, algunas aprobadas, otras no
    @Test
    public void testGetTotalApprovedExtraHoursByRutAndMonth_someApproved() {

        //Datos almacenados
        String rut = "12.345.678-9";
        int month = 9;
        int year = 2024;

        //Horas asprobadas (se suman)
        ExtraHours extra1 = new ExtraHours();
        extra1.setRut(rut);
        extra1.setNumExtraHours(5);
        extra1.setApproval(true);
        //Horas no aprobadas (no se suman)
        ExtraHours extra2 = new ExtraHours();
        extra2.setRut(rut);
        extra2.setNumExtraHours(3);
        extra2.setApproval(false);

        List<ExtraHours> extraHoursList = new ArrayList<>();
        extraHoursList.add(extra1);
        extraHoursList.add(extra2);

        given(extraHoursRepository.findByRutAndDateBetween(any(), any(), any())).willReturn(extraHoursList);

        int totalExtraHours = extraHoursService.getTotalApprovedExtraHoursByRutAndMonth(rut, month, year);

        //Verifica solo suma 5 (aprobadas)
        assertThat(totalExtraHours).isEqualTo(5);
    }

    // Suma de horas extra, ninguna aprobada.
    @Test
    public void testGetTotalApprovedExtraHoursByRutAndMonth_noneApproved() {

        //Datos creados
        String rut = "12.345.678-9";
        int month = 9;
        int year = 2024;

        //Horas no estan aprobadas (no se suman)
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

        //verifica no sumo ya que no estan aprobadas
        assertThat(totalExtraHours).isEqualTo(0);
    }

    //Test para: List<ExtraHours> getExtraHoursByRutAndMonth(String rut, int month, int year)
    //Rut tiene varios meses
    @Test
    public void getExtraHoursByRutAndMonth_returnsCorrectMonth() {
        // Datos solicitud Septiembre
        String rut = "12.345.678-9";
        int month = 9;
        int year = 2024;

        // Septiembre
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, Calendar.SEPTEMBER, 15);
        Date dateInSeptember = calendar.getTime();

        //Agosto
        calendar.set(year, Calendar.AUGUST, 15);
        Date dateInAugust = calendar.getTime();

        // Creación horas extra
        ExtraHours extraInSeptember = new ExtraHours(1L, rut, dateInSeptember, 5, false);
        ExtraHours extraInAugust = new ExtraHours(2L, rut, dateInAugust, 8, false);

        List<ExtraHours> extraHoursList = Arrays.asList(extraInSeptember, extraInAugust);

        given(extraHoursRepository.findByRutAndDateBetween(eq(rut), any(), any()))
                .willReturn(Collections.singletonList(extraInSeptember));

        List<ExtraHours> result = extraHoursService.getExtraHoursByRutAndMonth(rut, month, year);

        // verificar solo devuelve septiembre
        assertThat(result).hasSize(1);
        assertThat(result).extracting(ExtraHours::getDate).containsOnly(dateInSeptember);
        assertThat(result).extracting(ExtraHours::getNumExtraHours).containsOnly(5);
    }

    //Rut no registra mes
    @Test
    public void getExtraHoursByRutAndMonth_noHoursInMonth() {
        // Datos de consulta
        String rut = "12.345.678-9";
        int month = 9;
        int year = 2024;

       //Agosto
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, Calendar.AUGUST, 15);
        Date dateInAugust = calendar.getTime();

        ExtraHours extraInAugust = new ExtraHours(1L, rut, dateInAugust, 8, false);


        given(extraHoursRepository.findByRutAndDateBetween(eq(rut), any(), any()))
                .willReturn(Collections.emptyList());

        List<ExtraHours> result = extraHoursService.getExtraHoursByRutAndMonth(rut, month, year);

        // verificar devuelve lista vacia, no encontro mes
        assertThat(result).isEmpty();
    }


}
