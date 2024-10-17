package com.example.Proyecto_MISW.services;

import com.example.Proyecto_MISW.entities.DiscountHours;
import com.example.Proyecto_MISW.repositories.DiscountHoursRepository;
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

        // Creación de información Rut
        String rut = "12.345.678-9";

        // Descuento 1
        DiscountHours discount1 = new DiscountHours();
        discount1.setRut(rut);
        discount1.setDate(new Date());
        discount1.setNumDiscountHours(30);
        discount1.setApproval(false);

        // Descuento 2
        DiscountHours discount2 = new DiscountHours();
        discount2.setRut(rut);
        discount2.setDate(new Date());
        discount2.setNumDiscountHours(60);
        discount2.setApproval(false);

        // Crear Lista de descuentos
        List<DiscountHours> discountHoursList = new ArrayList<>();
        discountHoursList.add(discount1);
        discountHoursList.add(discount2);

        given(discountHoursRepository.findByRut(rut)).willReturn(discountHoursList);

        List<DiscountHours> result = discountHoursService.getDiscountHoursByRut(rut);

        // Se verifica informacion anterior se obtenga con funcion
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(2);
        assertThat(result).extracting(DiscountHours::getRut).containsOnly(rut);
        assertThat(result).extracting(DiscountHours::getNumDiscountHours).containsExactly(30, 60);
        assertThat(result).extracting(DiscountHours::isApproval).containsOnly(false);
    }


    //RUT no existente
    @Test
    public void nonExistingRut() {
        //Busqueda por rut inexistente
        String rut = "5.111.444-5";
        //lista vacia
        List<DiscountHours> discountHoursList = Collections.emptyList();

        given(discountHoursRepository.findByRut(rut)).willReturn(discountHoursList);

        List<DiscountHours> result = discountHoursService.getDiscountHoursByRut(rut);

        assertThat(result).isEmpty();
    }

    // Test para: public void saveDiscountHours(DiscountHours discountHours)
    // Guardar Horas de descuento
    @Test
    public void saveSomeDiscountHours() {
        // Crear horas de descuento
        DiscountHours discountHours = new DiscountHours();
        discountHours.setRut("12.345.678-9");
        discountHours.setDate(new Date());
        discountHours.setNumDiscountHours(100);
        discountHours.setApproval(false);

        discountHoursService.saveDiscountHours(discountHours);

        //Verificar que se utilice repositorio y que se obtengan datos indicados
        verify(discountHoursRepository, times(1)).save(discountHours);

        assertThat(discountHours.getRut()).isEqualTo("12.345.678-9");
        assertThat(discountHours.getDate()).isNotNull();
        assertThat(discountHours.getNumDiscountHours()).isEqualTo(100);
        assertThat(discountHours.isApproval()).isFalse();
    }


    // Test para: public void approveDiscountHoursByRutAndDate(String rut, Date date)
    //Aprobar horas descontadas de un rut que existe
    @Test
    public void approveDiscountHoursByExistingRut() {

        //Creacion de infromacion
        String rut = "12.345.678-9";
        Date date = new Date();

        DiscountHours discount1 = new DiscountHours();
        discount1.setRut(rut);
        discount1.setDate(date);
        discount1.setNumDiscountHours(120);
        discount1.setApproval(false);

        List<DiscountHours> discountHoursList = Collections.singletonList(discount1);


        given(discountHoursRepository.findByRutAndDate(rut, date)).willReturn(discountHoursList);

        discountHoursService.approveDiscountHoursByRutAndDate(rut, date);

        assertThat(discount1.isApproval()).isTrue();

        verify(discountHoursRepository, times(1)).save(discount1);

        assertThat(discount1.getRut()).isEqualTo(rut);
        assertThat(discount1.getDate()).isEqualTo(date);
        assertThat(discount1.getNumDiscountHours()).isEqualTo(120);
    }


    //Aprobar horas en descuento para rut inexistente
    @Test
    public void approveDiscountHoursByNonExistingRut() {
        //Crear datos de rut que no existe
        String rut = "1.111.111-1";
        Date date = new Date();

        List<DiscountHours> discountHoursList = Collections.emptyList();

        given(discountHoursRepository.findByRutAndDate(rut, date)).willReturn(discountHoursList);

        discountHoursService.approveDiscountHoursByRutAndDate(rut, date);

        //Verificar que el repositorio no haya encoentrado el rut
        verify(discountHoursRepository, times(0)).save(any(DiscountHours.class));
    }

    // Test para: public int getTotalUnapprovedDiscountHoursByRutAndMonth(String rut, int month, int year)
    //Rut y mes que no tiene descuento
    @Test
    public void discountHoursByRutAndMonth_noDiscountHours() {
        //Crear datos
        String rut = "12.345.678-9";
        int month = 9;
        int year = 2024;

        List<DiscountHours> discountHoursList = Collections.emptyList();

        given(discountHoursRepository.findByRutAndDateBetween(any(), any(), any())).willReturn(discountHoursList);

        int totalDiscountHours = discountHoursService.getTotalUnapprovedDiscountHoursByRutAndMonth(rut, month, year);
        //Rut no tiene descuentos
        assertThat(totalDiscountHours).isEqualTo(0);
    }

    // Empleado con horas de descuento > 70 (no aprobadas)
    @Test
    public void discountHoursByRutAndMonth_withDiscountHoursGreaterThan70_unapproved() {

        // Datos de prueba
        String rut = "12.345.678-9";
        int month = 9;
        int year = 2024;

        // Fecha de descuento
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 15);
        Date date = calendar.getTime();

        DiscountHours discount1 = new DiscountHours();
        discount1.setRut(rut);
        discount1.setDate(date);
        discount1.setNumDiscountHours(100);
        discount1.setApproval(false);


        List<DiscountHours> discountHoursList = Collections.singletonList(discount1);

        given(discountHoursRepository.findByRutAndDateBetween(any(), any(), any())).willReturn(discountHoursList);

        int totalDiscountHours = discountHoursService.getTotalUnapprovedDiscountHoursByRutAndMonth(rut, month, year);

        // Verificar datos
        assertThat(totalDiscountHours).isEqualTo(100);
        assertThat(discount1.getRut()).isEqualTo(rut);
        assertThat(discount1.getDate()).isEqualTo(date);
        assertThat(discount1.getNumDiscountHours()).isEqualTo(100);
        assertThat(discount1.isApproval()).isFalse();
    }


    // Empelado con horas de descuento > 70 (aprobadas)
    @Test
    public void discountHoursByRutAndMonth_withDiscountHoursGreaterThan70_approved() {
        // Datos de prueba
        String rut = "12.345.678-9";
        int month = 9;
        int year = 2024;

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 15);
        Date date = calendar.getTime();

        DiscountHours discount1 = new DiscountHours();
        discount1.setRut(rut);
        discount1.setDate(date);
        discount1.setNumDiscountHours(100);
        discount1.setApproval(true);

        List<DiscountHours> discountHoursList = Collections.singletonList(discount1);


        given(discountHoursRepository.findByRutAndDateBetween(any(), any(), any())).willReturn(discountHoursList);


        int totalDiscountHours = discountHoursService.getTotalUnapprovedDiscountHoursByRutAndMonth(rut, month, year);

        // Verificar que no sume horas, ya que estan aprobadas
        assertThat(totalDiscountHours).isEqualTo(0);
        assertThat(discount1.getRut()).isEqualTo(rut);
        assertThat(discount1.getDate()).isEqualTo(date);
        assertThat(discount1.getNumDiscountHours()).isEqualTo(100);
        assertThat(discount1.isApproval()).isTrue();
    }
    // Test para: public List<DiscountHours> getDiscountHoursByRutAndMonth(String rut, int month, int year)
    //Retorna informacion de un rut a pesar de existir informacion para dos ruts
    @Test
    public void getDiscountHoursByRutAndMonth_withTwoRuts_returnsOnlySelectedRut() {
        // Datos de prueba
        String rut1 = "12.345.678-9";
        String rut2 = "8.765.432-1";
        int month = 9;
        int year = 2024;

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 15);
        Date date = calendar.getTime();

        DiscountHours discount1 = new DiscountHours();
        discount1.setRut(rut1);
        discount1.setDate(date);
        discount1.setNumDiscountHours(50);
        discount1.setApproval(true);

        DiscountHours discount2 = new DiscountHours();
        discount2.setRut(rut2);
        discount2.setDate(date);
        discount2.setNumDiscountHours(80);
        discount2.setApproval(false);

        // Lista con informacion de los dos ruts
        List<DiscountHours> discountHoursList = Arrays.asList(discount1, discount2);


        given(discountHoursRepository.findByRutAndDateBetween(eq(rut1), any(), any())).willReturn(Collections.singletonList(discount1));


        List<DiscountHours> result = discountHoursService.getDiscountHoursByRutAndMonth(rut1, month, year);

        // Verificar que resultado tenga solo rut1
        assertThat(result).hasSize(1);
        assertThat(result).extracting(DiscountHours::getRut).containsOnly(rut1);
        assertThat(result).extracting(DiscountHours::getNumDiscountHours).containsOnly(50);
    }

    //No existe informacion del mes para el rut indicado
    @Test
    public void getDiscountHoursByRutAndMonth_withNoDiscountInMonth_returnsEmptyList() {
        // Datos de prueba
        String rut = "12.345.678-9";
        int month = 9;
        int year = 2024;

        //Datos creados
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, 7, 15);
        Date dateOutsideMonth = calendar.getTime();

        DiscountHours discount1 = new DiscountHours();
        discount1.setRut(rut);
        discount1.setDate(dateOutsideMonth);
        discount1.setNumDiscountHours(100);
        discount1.setApproval(false);

        given(discountHoursRepository.findByRutAndDateBetween(eq(rut), any(), any())).willReturn(Collections.emptyList());

        List<DiscountHours> result = discountHoursService.getDiscountHoursByRutAndMonth(rut, month, year);

        // Verificar no encuentra mes
        assertThat(result).isEmpty();
    }






}
