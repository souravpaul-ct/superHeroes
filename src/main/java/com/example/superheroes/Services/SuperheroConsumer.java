package com.example.superheroes.Services;

import com.example.superheroes.Model.Superhero;
import com.example.superheroes.Repository.SuperheroRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import io.awspring.cloud.sqs.annotation.SqsListener;

@Service
@RequiredArgsConstructor
public class SuperheroConsumer {

    private final SuperheroService superheroService;
    private final SuperheroRepository superheroRepository;

    @SqsListener("localstack-queue")
    public void receiveMessage(String superheroJson) {
        try {
            // Deserialize the superhero JSON to an object
            ObjectMapper objectMapper = new ObjectMapper();
            Superhero updatedSuperhero = objectMapper.readValue(superheroJson, Superhero.class);


            Superhero existingSuperhero = superheroRepository.findByName(updatedSuperhero.getName())
                    .orElseThrow(() -> new RuntimeException("Superhero not found with name: " + updatedSuperhero.getName()));


            System.out.println("Old Superhero details: " + existingSuperhero);


            Superhero finalUpdatedSuperhero = superheroService.updateSuperhero(updatedSuperhero.getName(), updatedSuperhero);


            System.out.println("Updated Superhero details: " + finalUpdatedSuperhero);

        } catch (Exception e) {
            System.err.println("Error processing message for: " + superheroJson + " | Error: " + e.getMessage());
        }
    }
}
