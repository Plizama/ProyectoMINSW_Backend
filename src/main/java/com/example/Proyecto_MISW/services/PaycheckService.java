package com.example.Proyecto_MISW.services;

import com.example.Proyecto_MISW.entities.Employee;
import com.example.Proyecto_MISW.entities.Paycheck;
import com.example.Proyecto_MISW.repositories.PaycheckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaycheckService {
    @Autowired
    PaycheckRepository paycheckRepository;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    OfficeHRMService officeHRMService;

    //Calculo de Pagos con descuentos y bonos
    public Boolean calculatePaychecks (int month, int year){
        List<Employee> employees = employeeService.getEmployees();

        //Para cada empleado calcula:
        for (Employee employee : employees){
            Paycheck paycheck = new Paycheck();
            paycheck.setRut(employee.getRut());
            paycheck.setYear(year);
            paycheck.setMonth(month);

            //Salario Fijo Mensual
            int monthlySalary = officeHRMService.getFixedMonthlySalary(employee);
            paycheck.setMonthlySalary(monthlySalary);

            //Bonificación por años de servicio
            int salaryBonus = officeHRMService.getSalaryBonus(employee);
            paycheck.setSalaryBonus(salaryBonus);

            //Descuento por atrasos
            int discountHours = officeHRMService.salaryDiscountArrears(employee, month,year);
            paycheck.setDiscountHours(discountHours);

            //Pago de horas extra
            int extraHoursBonus = officeHRMService.getExtraHoursBonus(employee,month,year);
            paycheck.setExtraHoursBonus(extraHoursBonus);

            //Descuentos legales
            int totalSalario = monthlySalary + salaryBonus + extraHoursBonus - discountHours;
            paycheck.setGrossSalary(totalSalario);

            //Decuentos seguridad social (cotizaciones)
            int socialSecurityDiscount = officeHRMService.getSocialSecurityDiscount(totalSalario);
            paycheck.setSocialSecurityDiscount(socialSecurityDiscount);
            //Descuento Salud
            int healthDiscount = officeHRMService.getHealthDiscount(totalSalario);
            paycheck.setHealthDiscount(healthDiscount);

            //Salario líquido
            int finalSalary = (totalSalario - socialSecurityDiscount) - healthDiscount ;
            paycheck.setTotalSalary(finalSalary);

            paycheckRepository.save(paycheck);

        }
        return true;
    }

    //Obtener pagos segun rut, mes y año
    public Paycheck getPaycheckByRutAndMonth(String rut, int month, int year) {
        return paycheckRepository.findByRutAndMonthAndYear(rut, month, year);
    }

}
