package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import com.udacity.jdnd.course3.critter.schedule.ScheduleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private CustomerService customerService;

    public ScheduleService(ScheduleRepository scheduleRepository, CustomerService customerService) {
        this.scheduleRepository = scheduleRepository;
        this.customerService = customerService;
    }

    public Schedule createSchedule(Schedule schedule){
        return this.scheduleRepository.save(schedule);
    }

    public List<Schedule> getAllSchedules(){
        return (List<Schedule>) this.scheduleRepository.findAll();
    }

    public List<Schedule> getScheduleForPet(long petId) {
        List<Schedule> schedules = (List<Schedule>) this.scheduleRepository.findAll();

        return schedules.stream().filter(s -> {
            List<Long> ids = convertPetToIds(s.getPets());
            if (ids.contains(petId))
                return true;
            else
                return false;
        }).collect(Collectors.toList());
    }

    public List<Schedule> getScheduleForEmployee(long employeeId) {
        List<Schedule> schedules = (List<Schedule>) this.scheduleRepository.findAll();

        return schedules.stream().filter(s -> {
            List<Long> ids = convertEmployeeToIds(s.getEmployees());
            if (ids.contains(employeeId))
                return true;
            else
                return false;
        }).collect(Collectors.toList());
//        return schedules;
    }

    public List<Schedule> getScheduleForCustomer(long customerId) {
        Customer customer = this.customerService.getCustomerById(customerId);
        List<Pet> pets = customer.getPets();
        List<Long> petIds = convertPetToIds(pets);
        List<Schedule> resultList = new ArrayList<>();
        for(Long petId : petIds){
            resultList.addAll(getScheduleForPet(petId));
        }
        return resultList;
    }

    public List<Long> convertEmployeeToIds(List<Employee> employees){
        List<Long> ids = new ArrayList<>();
        for(Employee e : employees) {
            ids.add(e.getId());
        }

        return ids;
    }

    public List<Long> convertPetToIds(List<Pet> pets){
        List<Long> ids = new ArrayList<>();
        for(Pet p : pets) {
            ids.add(p.getId());
        }

        return ids;
    }
}
