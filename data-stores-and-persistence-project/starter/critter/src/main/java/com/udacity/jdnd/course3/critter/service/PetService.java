package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.pet.PetDTO;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PetService {
    @Autowired
    private PetRepository petRepository;

    public PetService(PetRepository petRepository){
        this.petRepository = petRepository;
    }

    public Pet savePet(Pet pet){
        Pet savedPet = this.petRepository.save(pet);
        List<Pet> pets = savedPet.getCustomer().getPets();
        if(pets == null)
            pets = new ArrayList<>();
        pets.add(pet);
        savedPet.getCustomer().setPets(pets);
        return savedPet;
    }

    public Pet getPet(long petId){
        return this.petRepository.findById(petId).get();
    }

    public List<Pet> getPets(){
        return (List<Pet>) this.petRepository.findAll();
    }

//    public List<Pet> getPetsByOwner(long ownerId){
//        return this.petRepository.getPetsByOwner(ownerId);
//    }
}
