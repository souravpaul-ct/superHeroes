package com.example.superheroes.Services;


import com.example.superheroes.Model.Superhero;
import com.example.superheroes.Repository.SuperheroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SuperheroService {

    private final SuperheroRepository superheroRepository;

    @Autowired
    public SuperheroService(SuperheroRepository superheroRepository) {
        this.superheroRepository = superheroRepository;
    }

    public Superhero getSuperheroByName(String name) {
        return superheroRepository.findByName(name);
    }

    public List<Superhero> getSuperheroesByUniverse(String universe) {
        return superheroRepository.findByUniverse(universe);
    }

    public Superhero addSuperhero(Superhero superhero) {
        return superheroRepository.save(superhero);
    }

    public List<Superhero> addManySuperheroes(List<Superhero> superheroes) {
        return superheroRepository.saveAll(superheroes);
    }

    public Superhero updateSuperhero(String name, Superhero superhero) {
        Optional<Superhero> existingSuperhero = Optional.ofNullable(superheroRepository.findByName(name));
        if (existingSuperhero.isPresent()) {
            superhero.setName(name);
            return superheroRepository.save(superhero);
        }
        return null;
    }

    public void deleteSuperhero(String name) {
        superheroRepository.deleteById(name);
    }
}