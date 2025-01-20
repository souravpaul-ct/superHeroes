package com.example.superheroes.Controllers;

import com.example.superheroes.Model.Superhero;
import com.example.superheroes.Services.SuperheroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/superheroes")
public class SuperheroController {

    private final SuperheroService superheroService;

    @Autowired
    public SuperheroController(SuperheroService superheroService) {
        this.superheroService = superheroService;
    }

    @PostMapping
    public Superhero addSuperhero(@RequestBody Superhero superhero) {
        return superheroService.addSuperhero(superhero);
    }

    @PostMapping("/many")
    public List<Superhero> addManySuperheroes(@RequestBody List<Superhero> superheroes) {
        return superheroService.addManySuperheroes(superheroes);
    }

    @PutMapping("/{name}")
    public Superhero updateSuperhero(@PathVariable String name, @RequestBody Superhero superhero) {
        return superheroService.updateSuperhero(name, superhero);
    }

    @DeleteMapping("/{name}")
    public void deleteSuperhero(@PathVariable String name) {
        superheroService.deleteSuperhero(name);
    }

    @GetMapping("/name/{name}")
    public Superhero getSuperheroByName(@PathVariable String name) {
        return superheroService.getSuperheroByName(name);
    }

    @GetMapping("/universe/{universe}")
    public List<Superhero> getSuperheroesByUniverse(@PathVariable String universe) {
        return superheroService.getSuperheroesByUniverse(universe);
    }
}