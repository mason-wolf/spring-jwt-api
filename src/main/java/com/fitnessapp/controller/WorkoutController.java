package com.fitnessapp.controller;

import java.util.Optional;

import javax.validation.Valid;

import com.fitnessapp.dao.WorkoutRepository;
import com.fitnessapp.dao.jdbc;
import com.fitnessapp.model.Exercise;
import com.fitnessapp.model.Workout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/workouts")

public class WorkoutController {

    @Autowired
    WorkoutRepository workoutRepo;

    @GetMapping("/display/{id}")
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    Optional<Workout> show(@PathVariable Integer id) {
        return workoutRepo.findById(id);
    }

    @GetMapping("{userId}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    Optional<List<Workout>> showWorkoutsByUser(@PathVariable Integer userId) {

        jdbc.testQ();
        return workoutRepo.findByUserId(userId);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> register(@Valid @RequestBody Workout workout) {

        for (Exercise e : workout.getExercises()) {
            System.out.println(e.getName());
        }
        workoutRepo.save(workout);
		return ResponseEntity.ok("Workout added");
    }
}
