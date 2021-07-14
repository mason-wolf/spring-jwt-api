package com.fitnessapp.dao;

import java.util.List;
import java.util.Optional;
import com.fitnessapp.model.Workout;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutRepository extends JpaRepository<Workout, Integer> {
    public Optional<Workout> findById(Integer workoutId);
    public Optional<List<Workout>> findByUserId(Integer userId);
}
