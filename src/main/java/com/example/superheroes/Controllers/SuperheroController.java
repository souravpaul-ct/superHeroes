package com.example.superheroes.Controllers;

import com.example.superheroes.Config.SqsConfig;
import com.example.superheroes.Model.Superhero;
import com.example.superheroes.Services.SuperheroService;
import com.example.superheroes.Services.SuperheroConsumer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.util.List;

@RestController
@RequestMapping("/superheroes")
@RequiredArgsConstructor
public class SuperheroController {

    private final SuperheroService superheroService;
    private final SuperheroConsumer superheroConsumer;
    private final SqsClient sqsClient;
    private final SqsConfig sqsConfig;

    // POST method to add a single superhero
    @PostMapping
    public Superhero addSuperhero(@RequestBody Superhero superhero) {
        return superheroService.addSuperhero(superhero);
    }

    // POST method to add multiple superheroes
    @PostMapping("/many")
    public List<Superhero> addManySuperheroes(@RequestBody List<Superhero> superheroes) {
        return superheroService.addManySuperheroes(superheroes);
    }

    // PUT method to update a superhero
    @PutMapping("/{name}")
    public Superhero updateSuperhero(@PathVariable String name, @RequestBody Superhero superhero) {
        return superheroService.updateSuperhero(name, superhero);
    }

    // PATCH method for partial update of a superhero
    @PatchMapping("/{name}")
    public ResponseEntity<Superhero> patchSuperhero(@PathVariable String name, @RequestBody Superhero superhero) {
        try {
            Superhero updatedSuperhero = superheroService.patchSuperhero(name, superhero);
            return ResponseEntity.status(HttpStatus.OK).body(updatedSuperhero);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // DELETE method to delete a superhero by name
    @DeleteMapping("/{name}")
    public void deleteSuperhero(@PathVariable String name) {
        superheroService.deleteSuperhero(name);
    }

    // Basic hello endpoint for testing
    @GetMapping("/hello")
    public String getHello(@RequestParam(value = "username", defaultValue = "World") String username) {
        SendMessageResponse res = sqsClient.sendMessage(SendMessageRequest.builder()
                .queueUrl(sqsConfig.getQueueUrl())
                .messageBody("Sourav Paul").build());
        return String.format("Hello %s! %s", username, res.messageId());
    }

    // Asynchronously updates superhero by sending its name to the SQS queue
    @PutMapping("/update_superhero_async")
    public ResponseEntity<String> asyncUpdateSuperhero(@RequestBody Superhero superhero) {
        try {
            // Send superhero object to SQS for asynchronous processing
            ObjectMapper objectMapper = new ObjectMapper();
            SendMessageResponse response = sqsClient.sendMessage(SendMessageRequest.builder()
                    .queueUrl(sqsConfig.getQueueUrl())
                    .messageBody(objectMapper.writeValueAsString(superhero)) // Serialize the superhero object
                    .build());

            return ResponseEntity.accepted().body("Request to update " +
                    superhero.getName() + " has been queued. Message ID: " + response.messageId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending message to SQS");
        }
    }


    // GET method to fetch a superhero by name
    @GetMapping("/name/{name}")
    public ResponseEntity<Superhero> getSuperheroByName(@PathVariable String name) {
        try {
            Superhero superhero = superheroService.getSuperheroByName(name);
            return ResponseEntity.ok(superhero);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // GET method to fetch superheroes by universe
    @GetMapping("/universe/{universe}")
    public List<Superhero> getSuperheroesByUniverse(@PathVariable String universe) {
        return superheroService.getSuperheroesByUniverse(universe);
    }
}
