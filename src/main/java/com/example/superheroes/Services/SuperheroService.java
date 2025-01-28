package com.example.superheroes.Services;

import com.example.superheroes.Model.Superhero;
import com.example.superheroes.Repository.SuperheroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SuperheroService {

    private final SuperheroRepository superheroRepository;

    // Fetch superhero by name using Optional to handle nulls
    public Superhero getSuperheroByName(String name) {
        return superheroRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Superhero not found with name: " + name));
    }

    // Fetch superheroes by universe
    public List<Superhero> getSuperheroesByUniverse(String universe) {
        return superheroRepository.findByUniverse(universe);
    }

    public Superhero addSuperhero(Superhero superhero) {
        return superheroRepository.save(superhero);
    }

    public List<Superhero> addManySuperheroes(List<Superhero> superheroes) {
        return superheroRepository.saveAll(superheroes);
    }

    // Update superhero if exists, else throw exception
    public Superhero updateSuperhero(String name, Superhero superhero) {
        return superheroRepository.findByName(name)
                .map(existingSuperhero -> {
                    // Update only fields that are not null or modified
                    if (superhero.getPower() != null) {
                        existingSuperhero.setPower(superhero.getPower());
                    }
                    if (superhero.getAge() > 0) {  // Ensure valid age
                        existingSuperhero.setAge(superhero.getAge());
                    }
                    if (superhero.getGender() != null) {
                        existingSuperhero.setGender(superhero.getGender());
                    }
                    if (superhero.getUniverse() != null) {
                        existingSuperhero.setUniverse(superhero.getUniverse());
                    }
                    if (superhero.getArchEnemies() != null && !superhero.getArchEnemies().isEmpty()) {
                        existingSuperhero.setArchEnemies(superhero.getArchEnemies());
                    }

                    // Save and return the updated superhero
                    return superheroRepository.save(existingSuperhero);
                })
                .orElseThrow(() -> new RuntimeException("Superhero not found with name: " + name));
    }

    public void deleteSuperhero(String name) {
        superheroRepository.deleteById(name);
    }

    public Superhero patchSuperhero(String name, Superhero superhero) {
        Superhero existingSuperhero = superheroRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Superhero not found with name: " + name));

        // Only update fields that are not null
        if (superhero.getPower() != null) {
            existingSuperhero.setPower(superhero.getPower());
        }
        if (superhero.getAge() > 0) {  // Age should be a valid positive number
            existingSuperhero.setAge(superhero.getAge());
        }
        if (superhero.getGender() != null) {
            existingSuperhero.setGender(superhero.getGender());
        }
        if (superhero.getUniverse() != null) {
            existingSuperhero.setUniverse(superhero.getUniverse());
        }
        if (superhero.getArchEnemies() != null && !superhero.getArchEnemies().isEmpty()) {
            existingSuperhero.setArchEnemies(superhero.getArchEnemies());
        }

        // Save and return the updated superhero
        return superheroRepository.save(existingSuperhero);
    }
}
