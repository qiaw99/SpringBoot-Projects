package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    @Autowired
    private PetService petService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ScheduleService scheduleService;

    public ScheduleController(PetService petService, EmployeeService employeeService, ScheduleService scheduleService) {
        this.petService = petService;
        this.employeeService = employeeService;
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        return convertPOJPToDTO(this.scheduleService.createSchedule(convertDTOToPOJO(scheduleDTO)));
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> schedules = this.scheduleService.getAllSchedules();
        List<ScheduleDTO> scheduleDTOS = convertToDTOList(schedules);

        return scheduleDTOS;
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        return convertToDTOList(this.scheduleService.getScheduleForPet(petId));
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        return convertToDTOList(this.scheduleService.getScheduleForEmployee(employeeId));
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        return convertToDTOList(this.scheduleService.getScheduleForCustomer(customerId));
    }

    public List<ScheduleDTO> convertToDTOList(List<Schedule> schedules) {
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        for(Schedule s : schedules){
            scheduleDTOS.add(convertPOJPToDTO(s));
        }
        return scheduleDTOS;
    }

    public Schedule convertDTOToPOJO(ScheduleDTO scheduleDTO){
        Schedule schedule = new Schedule();
        schedule.setId(scheduleDTO.getId());
        schedule.setDate(scheduleDTO.getDate());
        schedule.setActivities(scheduleDTO.getActivities());
        List<Long> petsIds = scheduleDTO.getPetIds();
        List<Long> employeeIds = scheduleDTO.getEmployeeIds();
        List<Employee> employees = new ArrayList<>();
        List<Pet> pets = new ArrayList<>();

        for(Long id : petsIds){
            pets.add(this.petService.getPet(id));
        }

        for(Long id : employeeIds){
            employees.add(this.employeeService.getEmployee(id));
        }

        schedule.setPets(pets);
        schedule.setEmployees(employees);

        return schedule;
    }

    public ScheduleDTO convertPOJPToDTO(Schedule schedule){
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setId(schedule.getId());
        scheduleDTO.setDate(schedule.getDate());
        scheduleDTO.setActivities(schedule.getActivities());

        List<Pet> pets = schedule.getPets();
        List<Employee> employees = schedule.getEmployees();

        List<Long> employeeIds = new ArrayList<>();
        List<Long> petIds = new ArrayList<>();

        for(Pet pet : pets){
            petIds.add(pet.getId());
        }

        for(Employee employee : employees){
            employeeIds.add(employee.getId());
        }

        scheduleDTO.setPetIds(petIds);
        scheduleDTO.setEmployeeIds(employeeIds);

        return scheduleDTO;
    }
}
