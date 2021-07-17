package com.fitnessapp.controller;

import java.util.Optional;

import javax.validation.Valid;

import com.fitnessapp.dao.WorkoutDAO;
import com.fitnessapp.dao.WorkoutRepository;
import com.fitnessapp.model.CustomUserDetails;
import com.fitnessapp.model.Exercise;
import com.fitnessapp.model.Workout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.http.ResponseEntity.HeadersBuilder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/workouts")

public class WorkoutController {

    @Autowired
    WorkoutRepository workoutRepo;

    @GetMapping("/view")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    ResponseEntity<?> viewWorkoutById(@RequestParam Integer workoutId) {

        CustomUserDetails user = (CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Workout workout = WorkoutDAO.getWorkoutById(workoutId, user.getId());

        if (workout == null) {
            return ResponseEntity.ok(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(workout);
    }

    @DeleteMapping("/exercises/{exerciseId}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    ResponseEntity<?> deleteExercise(@PathVariable Integer exerciseId) {
        return  WorkoutDAO.deleteExercise(exerciseId);
    }

    @PutMapping("") 
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    ResponseEntity<?> updateWorkout(@Valid @RequestBody Workout workout) {
        return WorkoutDAO.updateWorkout(workout);
    }

    @DeleteMapping("")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    ResponseEntity<?> deleteWorkout(@Valid @RequestBody Workout workout) {
        return WorkoutDAO.deleteWorkout(workout);
    }

    @GetMapping("{userId}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    List<Workout> showWorkoutsByUser(@PathVariable Integer userId) {
        List<Workout> workouts = WorkoutDAO.getWorkoutsByUserId(userId);
        return workouts;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> register(@Valid @RequestBody Workout workout) {
        
        WorkoutDAO.addWorkout(workout);
		return ResponseEntity.ok("Workout added");
    }
}
