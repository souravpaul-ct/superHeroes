package com.example.superheroes.Model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "superheroes")

public class Superhero {

    @Id
    private String name;
    private String power;
    private String gender;
    private int age;
    private String universe;
    private List<String> archEnemies;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getUniverse() {
        return universe;
    }

    public void setUniverse(String universe) {
        this.universe = universe;
    }

    public List<String> getArchEnemies() {
        return archEnemies;
    }

    public void setArchEnemies(List<String> archEnemies) {
        this.archEnemies = archEnemies;
    }
}
