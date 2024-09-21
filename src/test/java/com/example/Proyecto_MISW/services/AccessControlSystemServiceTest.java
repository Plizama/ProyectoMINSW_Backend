package com.example.Proyecto_MISW.services;

import com.example.Proyecto_MISW.entities.ArrivalTime;
import com.example.Proyecto_MISW.entities.DepartureTime;
import com.example.Proyecto_MISW.entities.Employee;
import com.example.Proyecto_MISW.entities.DiscountHours;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class AccessControlSystemServiceTest {

    @Mock
    private ArrivalTimeService arrivalTimeService;

    @Mock
    private DepartureTimeService departureTimeService;

    @Mock
    private DiscountHoursService discountHoursService;

    @Mock
    private ExtraHoursService extraHoursService;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private AccessControlSystemService accessControlSystemService;

    public AccessControlSystemServiceTest() {
        MockitoAnnotations.openMocks(this);
    }
    //Test para: public void processAccessFile(InputStream inputStream)
    // Simulación de registro entrada y salida de un Employee
    @Test
    public void testProcessAccessFile_withValidData() {

        // Simulación del archivo de acceso con entrada y salida
        String accessData = "2024/09/19;08:05;12.345.678-9\n2024/09/19;17:50;12.345.678-9";
        InputStream inputStream = new ByteArrayInputStream(accessData.getBytes());

        Employee employee = new Employee(1L, "12.345.678-9", "Juan", "Perez", "A", null);
        List<Employee> employees = new ArrayList<>(Collections.singletonList(employee));

        given(employeeService.getEmployees()).willReturn((ArrayList<Employee>) employees);

        accessControlSystemService.processAccessFile(inputStream);

        assertThat(arrivalTimeService).satisfies(service -> {
            verify(service, times(1)).saveArrivalTime(eq("12.345.678-9"), eq(LocalDate.of(2024, 9, 19)), eq(LocalTime.of(8, 5)));
        });
        assertThat(departureTimeService).satisfies(service -> {
            verify(service, times(1)).saveDepartureTime(eq("12.345.678-9"), eq(LocalDate.of(2024, 9, 19)), eq(LocalTime.of(17, 50)));
        });
    }

    // Simulación de registro de entrada y salida y un empleado que se ausentó ese día
    @Test
    public void testProcessAccessFile_withMissingEmployeeAccess() {

        String accessData = "2024/09/19;08:05;12.345.678-9\n2024/09/19;17:50;12.345.678-9";
        InputStream inputStream = new ByteArrayInputStream(accessData.getBytes());

        Employee employee1 = new Employee(1L, "12.345.678-9", "Juan", "Perez", "A", null);
        Employee employee2 = new Employee(2L, "7.654.321-0", "Maria", "Lopez", "B", null);
        List<Employee> employees = new ArrayList<>(Arrays.asList(employee1, employee2));

        given(employeeService.getEmployees()).willReturn((ArrayList<Employee>) employees);

        accessControlSystemService.processAccessFile(inputStream);

        assertThat(arrivalTimeService).satisfies(service -> {
            verify(service, times(1)).saveArrivalTime(eq("12.345.678-9"), eq(LocalDate.of(2024, 9, 19)), eq(LocalTime.of(8, 5)));
        });

        assertThat(departureTimeService).satisfies(service -> {
            verify(service, times(1)).saveDepartureTime(eq("12.345.678-9"), eq(LocalDate.of(2024, 9, 19)), eq(LocalTime.of(17, 50)));
        });

        assertThat(discountHoursService).satisfies(service -> {
            verify(service, times(1)).saveDiscountHours(argThat(discount ->
                    discount.getRut().equals("7.654.321-0") &&
                            discount.getDate().equals(java.sql.Date.valueOf(LocalDate.of(2024, 9, 19))) &&
                            discount.getNumDiscountHours() == 600 &&
                            !discount.isApproval()
            ));
        });
    }

    //Test para: public void processArrivalTimesForDiscounts()
    //Empleado que llega tarde
    @Test
    public void testProcessArrivalTimesForDiscounts_employeeArrivesAfter8AM() {

        ArrivalTime arrivalTime = new ArrivalTime();
        arrivalTime.setRut("12.345.678-9");
        arrivalTime.setDate(java.sql.Date.valueOf(LocalDate.of(2024, 9, 19)));
        arrivalTime.setArrival_time(LocalTime.of(8, 15));

        List<ArrivalTime> arrivalTimes = Collections.singletonList(arrivalTime);

        given(arrivalTimeService.getAllArrivalTimes()).willReturn(arrivalTimes);

        accessControlSystemService.processArrivalTimesForDiscounts();

        assertThat(discountHoursService).satisfies(service -> {
            verify(service, times(1)).saveDiscountHours(argThat(discount ->
                    discount.getRut().equals("12.345.678-9") &&
                            discount.getDate().equals(java.sql.Date.valueOf(LocalDate.of(2024, 9, 19))) &&
                            discount.getNumDiscountHours() == 15 &&
                            !discount.isApproval()
            ));
        });
    }

    @Test
    public void testProcessArrivalTimesForDiscounts_employeeArrivesBefore8AM() {
        ArrivalTime arrivalTime = new ArrivalTime();
        arrivalTime.setRut("12.345.678-9");
        arrivalTime.setDate(java.sql.Date.valueOf(LocalDate.of(2024, 9, 19)));
        arrivalTime.setArrival_time(LocalTime.of(7, 45));

        List<ArrivalTime> arrivalTimes = Collections.singletonList(arrivalTime);

        given(arrivalTimeService.getAllArrivalTimes()).willReturn(arrivalTimes);

        accessControlSystemService.processArrivalTimesForDiscounts();


        assertThat(discountHoursService).satisfies(service -> {
            verify(service, times(0)).saveDiscountHours(any());
        });
    }
    //Test para: public void processDepartureTimesForExtraHours()
    // Empleado que sale a las 18:00
    @Test
    public void testProcessDepartureTimesForExtraHours_employeeDepartsAt6PM() {
        DepartureTime departureTime = new DepartureTime();
        departureTime.setRut("12.345.678-9");
        departureTime.setDate(java.sql.Date.valueOf(LocalDate.of(2024, 9, 19)));
        departureTime.setDeparture_time(LocalTime.of(18, 0));

        List<DepartureTime> departureTimes = Collections.singletonList(departureTime);

        given(departureTimeService.getAllDepartureTimes()).willReturn(departureTimes);

        accessControlSystemService.processDepartureTimesForExtraHours();

        assertThat(extraHoursService).satisfies(service -> {
            verify(service, times(0)).saveExtraHours(any());
        });
    }
    // Empleado que sale después de las 18:00
    @Test
    public void testProcessDepartureTimesForExtraHours_employeeDepartsAfter6PM() {
        DepartureTime departureTime = new DepartureTime();
        departureTime.setRut("12.345.678-9");
        departureTime.setDate(java.sql.Date.valueOf(LocalDate.of(2024, 9, 19)));
        departureTime.setDeparture_time(LocalTime.of(18, 30));

        List<DepartureTime> departureTimes = Collections.singletonList(departureTime);

        given(departureTimeService.getAllDepartureTimes()).willReturn(departureTimes);

        accessControlSystemService.processDepartureTimesForExtraHours();

        assertThat(extraHoursService).satisfies(service -> {
            verify(service, times(1)).saveExtraHours(argThat(extraHours ->
                    extraHours.getRut().equals("12.345.678-9") &&
                            extraHours.getDate().equals(java.sql.Date.valueOf(LocalDate.of(2024, 9, 19))) &&
                            extraHours.getNumExtraHours() == 30 &&
                            !extraHours.isApproval()
            ));
        });
    }


}


