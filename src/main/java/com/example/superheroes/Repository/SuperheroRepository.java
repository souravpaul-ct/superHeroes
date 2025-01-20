package com.example.superheroes.Repository;

import com.example.superheroes.Model.Superhero;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository

public interface SuperheroRepository extends MongoRepository<Superhero, String> {
    Superhero findByName(String name);
    List<Superhero> findByUniverse(String universe);
}
