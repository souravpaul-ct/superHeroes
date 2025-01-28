package com.example.superheroes.Services;

import com.example.superheroes.Config.SqsConfig;
import com.example.superheroes.Model.Superhero;
import com.example.superheroes.Repository.SuperheroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.ArrayList;
import java.util.List;

@Service
public class SuperheroConsumer {

    @Autowired
    private SqsConfig sqsConfig;
    @Autowired
    private SqsClient sqsClient;
    @Autowired
    private SuperheroService superheroService;
    @Autowired
    private SuperheroRepository superheroRepository;


    public List<String> consumeAndProcessUpdates() {
        List<String> messageBodies = new ArrayList<>();

        ReceiveMessageResponse receivedMessageResponse = sqsClient.receiveMessage(ReceiveMessageRequest
                .builder()
                .queueUrl(sqsConfig.getQueueUrl())
                .maxNumberOfMessages(10)
                .build());

        List<Message> messages = receivedMessageResponse.messages();
        for (Message message : messages) {
            String superHeroName = message.body();
            messageBodies.add(superHeroName);


                Superhero existingSuperhero = superheroRepository.findByName(superHeroName)
                        .orElseThrow(() ->
                                new RuntimeException("Superhero not found with name: " + superHeroName));


                Superhero updatedSuperhero = superheroService.updateSuperhero(superHeroName, existingSuperhero);

                System.out.println("Superhero updated successfully: " + updatedSuperhero);


                sqsClient.deleteMessage(DeleteMessageRequest.builder()
                        .queueUrl(sqsConfig.getQueueUrl())
                        .receiptHandle(message.receiptHandle())
                        .build());
                System.out.println("Message processed and deleted successfully for: " + superHeroName);
        }

        return messageBodies;
    }

}