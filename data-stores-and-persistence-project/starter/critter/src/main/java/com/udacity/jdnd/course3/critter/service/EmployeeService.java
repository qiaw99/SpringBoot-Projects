package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.EmployeeRequest;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee saveEmployee(Employee employee) {
        return this.employeeRepository.save(employee);
    }

    public Employee getEmployee(long employeeId) {
        Optional<Employee> employee = this.employeeRepository.findById(employeeId);
        return employee.get();
    }

    public void setAvailability(Set<DayOfWeek> daysAvailable, long employeeId) {
        Employee employee = this.employeeRepository.findById(employeeId).get();
        employee.setDaysAvailable(daysAvailable);
    }

    public List<Employee> findEmployeesForService(EmployeeRequest employee) {
        DayOfWeek dayOfWeek = employee.getDate().getDayOfWeek();
        Set<EmployeeSkill> skills = employee.getSkills();

        List<Employee> employees = (List<Employee>) this.employeeRepository.findAll();
//        List<Employee> resList = new ArrayList<>();
//
//        for(Employee e : employees){
//            if (e.getDaysAvailable().contains(dayOfWeek) && e.getSkills().containsAll(skills))
//                resList.add(e);
//        }

//        return resList;
        return employees.stream().filter(e -> e.getDaysAvailable().contains(dayOfWeek) && e.getSkills().containsAll(skills)).collect(Collectors.toList());
    }
}
