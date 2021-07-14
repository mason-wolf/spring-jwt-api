package com.fitnessapp.model;


import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="workouts")
public class Workout {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer workout_id; 
    
    @Column(name="date")
    private String date;

    @Column(name="user_id")
    private Integer userId;

    @Column(name="title")
    private String title;

    @Transient
	private Set<Exercise> exercises;
    
    public Integer getId() {
        return workout_id;
    }

    public void setId(Integer workoutId) {
        this.workout_id = workoutId;
    }

    public String getDate() {
        return date;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public Set<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(Set<Exercise> exercises) {
        this.exercises = exercises;
    }
}
