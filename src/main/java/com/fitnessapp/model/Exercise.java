package com.fitnessapp.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="exercises")
public class Exercise implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer exercise_id;

    @Column(name="name")
    private String name;

    @Column(name="sets")
    private Integer sets;

    @Column(name="reps")
    private Integer reps;

    @Column(name="workout_id")
    private Integer workout_id;

    @Column(name="weight")
    private Integer weight;

    public Integer getId() {
        return exercise_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    public void setId(Integer id) {
        this.exercise_id = id;
    }

    public Integer getSets() {
        return sets;
    }

    public void setSets(Integer sets) {
        this.sets = sets;
    }

    public Integer getReps() {
        return reps;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
    }

    public Integer getWorkoutId() {
        return workout_id;
    }

    public void setWorkoutId(Integer id) {
        this.workout_id = id;
    }

    public Integer getWeight() {
        return this.weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
