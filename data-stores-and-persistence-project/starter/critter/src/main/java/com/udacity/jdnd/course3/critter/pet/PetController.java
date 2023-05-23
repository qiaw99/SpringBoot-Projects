package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    private PetService petService;

    @Autowired
    private CustomerService customerService;

    public PetController(PetService petService, CustomerService customerService) {
        this.petService = petService;
        this.customerService = customerService;
    }

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        return convertPetToDTO(this.petService.savePet(convertDTOToPet(petDTO)));
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        return convertPetToDTO(this.petService.getPet(petId));
    }

    @GetMapping
    public List<PetDTO> getPets() {
        List<Pet> pets = this.petService.getPets();
        List<PetDTO> petDTOS = new ArrayList<>();
        for (Pet pet: pets){
            petDTOS.add(convertPetToDTO(pet));
        }
        return petDTOS;
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        Customer customer = this.customerService.getCustomerById(ownerId);
        List<Pet> pets = customer.getPets();
        List<PetDTO> petDTOS = new ArrayList<>();

        if (pets != null) {
            for (Pet pet : pets) {
                petDTOS.add(convertPetToDTO(pet));
            }
        }
        return petDTOS;
    }

    private Pet convertDTOToPet(PetDTO petDTO){
        Pet pet = new Pet();
        pet.setId(petDTO.getId());
        pet.setType(petDTO.getType());
        pet.setName(petDTO.getName());
        pet.setBirthDate(petDTO.getBirthDate());
        pet.setNotes(petDTO.getNotes());
        Long ownerId = petDTO.getOwnerId();

        if (ownerId != null){
            Customer customer = this.customerService.getCustomerById(ownerId);
            pet.setCustomer(customer);
        }

        return pet;
    }

    private PetDTO convertPetToDTO(Pet pet){
        PetDTO petDTO = new PetDTO();
        petDTO.setId(pet.getId());
        petDTO.setType(pet.getType());
        petDTO.setName(pet.getName());
        petDTO.setOwnerId(pet.getCustomer().getId());
        petDTO.setBirthDate(pet.getBirthDate());
        petDTO.setNotes(pet.getNotes());

        return petDTO;
    }
}
