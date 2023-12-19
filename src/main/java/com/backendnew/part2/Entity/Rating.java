package com.backendnew.part2.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

@Entity
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int value;

    @OneToOne
    @JoinColumn(name = "skill_id")
    @JsonBackReference
    private Skill skill;

    // getters and setters

    public Long getId() {
        return id;
    }

    // Add a constructor that takes an int parameter
    public Rating(int value) {
        this.value = value;
    }
    public Rating(Long id, int value, Skill skill) {
        this.id = id;
        this.value = value;
        this.skill = skill;
    }

    public Rating() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

}


