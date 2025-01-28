package com.example.superheroes.Model;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "superheroes")

public class Superhero {

    @Id

    private String name;
    private String power;
    private String gender;
    private int age;
    private String universe;
    private List<String> archEnemies;

}
