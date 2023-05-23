package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.EmployeeRequest;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private PetService petService;
    @Autowired
    private EmployeeService employeeService;

    public UserController(CustomerService customerService, PetService petService, EmployeeService employeeService) {
        this.customerService = customerService;
        this.petService = petService;
        this.employeeService = employeeService;
    }

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        return convertCostomerToCustomerDTO(this.customerService.saveCustomer(convertCostomerDTOToCustomer(customerDTO)));
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        List<Customer> customers = this.customerService.getAllCustomers();
        List<CustomerDTO> customerDTOS = new ArrayList<>();
        for(Customer customer : customers){
            customerDTOS.add(convertCostomerToCustomerDTO(customer));
        }

        return customerDTOS;
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        Customer customer = this.customerService.getCustomerById(this.petService.getPet(petId).getCustomer().getId());
        return convertCostomerToCustomerDTO(customer);
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
       return convertEmployeeToEmployeeDTO(this.employeeService.saveEmployee(convertEmployeeDTOToEmployee(employeeDTO)));
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        return convertEmployeeToEmployeeDTO(this.employeeService.getEmployee(employeeId));
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        this.employeeService.setAvailability(daysAvailable, employeeId);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        List<Employee> employees = this.employeeService.findEmployeesForService(convertERDToER(employeeDTO));
        List<EmployeeDTO> employeeDTOS = new ArrayList<>();
        for (Employee employee : employees){
            employeeDTOS.add(convertEmployeeToEmployeeDTO(employee));
        }

        return employeeDTOS;
    }

    private EmployeeRequest convertERDToER(EmployeeRequestDTO employeeDTO){
        EmployeeRequest employeeRequest = new EmployeeRequest();
        employeeRequest.setDate(employeeDTO.getDate());
        employeeRequest.setSkills(employeeDTO.getSkills());

        return employeeRequest;
    }

    private EmployeeRequestDTO convertERToERD(EmployeeRequest employee){
        EmployeeRequestDTO employeeRequestDTO = new EmployeeRequestDTO();
        employeeRequestDTO.setDate(employee.getDate());
        employeeRequestDTO.setSkills(employee.getSkills());

        return employeeRequestDTO;
    }

    private Employee convertEmployeeDTOToEmployee(EmployeeDTO employeeDTO){
        Employee employee = new Employee();
        employee.setId(employeeDTO.getId());
        employee.setName(employeeDTO.getName());
        employee.setSkills(employeeDTO.getSkills());
        employee.setDaysAvailable(employeeDTO.getDaysAvailable());

        return employee;
    }

    private EmployeeDTO convertEmployeeToEmployeeDTO(Employee employee){
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setName(employee.getName());
        employeeDTO.setSkills(employee.getSkills());
        employeeDTO.setDaysAvailable(employee.getDaysAvailable());

        return employeeDTO;
    }

    private Customer convertCostomerDTOToCustomer(CustomerDTO customerDTO){
        Customer customer = new Customer();
        customer.setId(customerDTO.getId());

        if(customerDTO.getName() != null) {
            customer.setName(customerDTO.getName());
        }

        if(customerDTO.getPhoneNumber() != null)
            customer.setPhoneNumber(customerDTO.getPhoneNumber());

        if (customerDTO.getNotes() != null)
            customer.setNotes(customerDTO.getNotes());

        if(customerDTO.getPetIds() != null) {
            List<Long> petIds = customerDTO.getPetIds();

            List<Pet> pets = new ArrayList<>();

            for (Long id : petIds) {
                pets.add(this.petService.getPet(id));
            }
            customer.setPets(pets);
        }
        return customer;
    }

    private CustomerDTO convertCostomerToCustomerDTO(Customer customer){
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        customerDTO.setPhoneNumber(customer.getPhoneNumber());
        customerDTO.setNotes(customer.getNotes());

        if(customer.getPets() != null) {
            List<Long> ids = new ArrayList<>();

            for (Pet pet : customer.getPets()) {
                ids.add(pet.getId());
            }
            customerDTO.setPetIds(ids);
        }
        return customerDTO;
    }
}
