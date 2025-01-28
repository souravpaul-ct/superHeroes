package com.example.superheroes.Controllers;

import com.example.superheroes.Config.SqsConfig;
import com.example.superheroes.Model.Superhero;
import com.example.superheroes.Services.SuperheroConsumer;
import com.example.superheroes.Services.SuperheroService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private SqsConfig sqsConfig;

    @Autowired
    private SqsClient sqsClient;

    @Autowired
    private SuperheroConsumer superheroConsumer;


    @Autowired
    public SuperheroController(SuperheroService superheroService, SqsClient sqsClient, SuperheroConsumer superheroConsumer) {
        this.superheroService = superheroService;
        this.sqsClient = sqsClient;
        this.superheroConsumer = superheroConsumer;
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

    @PatchMapping("/{name}")
    public ResponseEntity<Superhero> patchSuperhero(@PathVariable String name, @RequestBody Superhero superhero) {
        try {
            // Call the service to patch the superhero
            Superhero updatedSuperhero = superheroService.patchSuperhero(name, superhero);
            return ResponseEntity.status(HttpStatus.OK).body(updatedSuperhero);  // Return updated superhero with status 200
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Return 404 if superhero not found
        }
    }


    @DeleteMapping("/{name}")
    public void deleteSuperhero(@PathVariable String name) {
        superheroService.deleteSuperhero(name);
    }

    @GetMapping("/hello")
    public String getHello(@RequestParam(value = "username", defaultValue = "World") String username) {
        SendMessageResponse res = sqsClient.sendMessage(SendMessageRequest.builder()
                .queueUrl(sqsConfig.getQueueUrl())
                .messageBody("Sourav Paul").build());

        return String.format("Hello %s! %s", username, res.messageId());
    }


    // asyncronously updates with a name by sending it to the SOS queue
    @PutMapping("/update_superhero_async")
    public ResponseEntity<String> asyncUpdateSuperhero(@RequestBody Superhero superhero) {
        String superHeroName = superhero.getName();

        SendMessageResponse response = sqsClient.sendMessage(SendMessageRequest.builder()
                .queueUrl(sqsConfig.getQueueUrl())
                .messageBody(superHeroName)
                .build());

        return ResponseEntity.accepted().body("Request to update " +
                superHeroName + " has been queued. Message ID: " + response.messageId());

    }


    // it consumes a message from the SOS queue using the superhero Consumer
    @GetMapping("/get_message_from_queue")
    public ResponseEntity<List<String>> getMessage() {
       List<String> messages =  superheroConsumer.consumeAndProcessUpdates();
        return ResponseEntity.ok(messages);

    }



    @GetMapping("/name/{name}")
    public ResponseEntity<Superhero> getSuperheroByName(@PathVariable String name) {
        try {
            Superhero superhero = superheroService.getSuperheroByName(name);
            return ResponseEntity.ok(superhero);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/universe/{universe}")
    public List<Superhero> getSuperheroesByUniverse(@PathVariable String universe) {
        return superheroService.getSuperheroesByUniverse(universe);
    }
}
