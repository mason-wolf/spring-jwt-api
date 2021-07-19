package com.fitnessapp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.fitnessapp.model.CustomUserDetails;
import com.fitnessapp.model.Exercise;
import com.fitnessapp.model.Workout;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;


public class WorkoutDAO {
    static final String url = "jdbc:mysql://aa9xz8i0zjsz4s.ct7fhre5oddy.us-east-2.rds.amazonaws.com:3306/fitness";
    static final String user = "";
    static final String password = "";

    public static ArrayList<Workout> getWorkoutsByUserId(Integer userId) {

        try {
            String query = "SELECT * FROM workouts WHERE user_id=?";
            Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            ArrayList<Workout> workouts = new ArrayList<Workout>();
            
            while (resultSet.next()) {
                Workout workout = new Workout();
                workout.setId(resultSet.getInt("workout_id"));
                workout.setTitle(resultSet.getString("title"));
                workout.setDate(resultSet.getString("date"));
                workout.setUserId(resultSet.getInt("user_id"));
                workouts.add(workout);
            }

            connection.close();
            return workouts;
        }
        catch(SQLException e) {
            System.out.println(e);
            return null;
        }
    }

    public static ResponseEntity<?> updateWorkout(Workout workout) {

        ResponseEntity<?> response = ResponseEntity.ok(HttpStatus.PROCESSING);

        try {
            CustomUserDetails userDetails = (CustomUserDetails)SecurityContextHolder.getContext().
            getAuthentication().getPrincipal();

            if (workout.getUserId() != userDetails.getId()) {
                System.out.println(workout.getUserId() + 1);
                response = ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
            }
            else {
                String query = "UPDATE workouts SET title=?, date=? WHERE workout_id=? and user_id=?";

                Connection connection = DriverManager.getConnection(url, user, password);
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, workout.getTitle());
                statement.setString(2, workout.getDate());
                statement.setInt(3, workout.getId());
                statement.setInt(4, userDetails.getId());
                statement.executeUpdate();
    

                query = "UPDATE exercises SET name=?, sets=?, reps=? WHERE exercise_id=?";

                for(Exercise exercise : workout.getExercises()) {
                    if (exercise.getId() != null) {
                        statement = connection.prepareStatement(query);
                        statement.setString(1, exercise.getName());
                        statement.setInt(2, exercise.getSets());
                        statement.setInt(3, exercise.getReps());
                        statement.setInt(4, exercise.getId());
                        statement.executeUpdate();
                    }
                    else {
                        query = "INSERT INTO exercises (workout_id, name, sets, reps) VALUES (?, ?, ?, ?)";
                        ArrayList<Workout> userWorkouts = getWorkoutsByUserId(workout.getUserId());
                        int workoutId = userWorkouts.get(userWorkouts.size() - 1).getId();        
                        statement = connection.prepareStatement(query);
                        statement.setInt(1, workoutId);
                        statement.setString(2, exercise.getName());
                        statement.setInt(3, exercise.getSets());
                        statement.setInt(4, exercise.getReps());
                        statement.executeUpdate();
                    }
                }
                connection.close();
                response = ResponseEntity.ok(HttpStatus.OK);
            }
        }
        catch (SQLException e) {
            System.out.println(e);
        }

        return response;
    }

    public static ResponseEntity<?> deleteWorkout(Workout workout) {

        ResponseEntity<?> response = ResponseEntity.ok(HttpStatus.PROCESSING);

        try {
            CustomUserDetails userDetails = (CustomUserDetails)SecurityContextHolder.getContext().
            getAuthentication().getPrincipal();

            if (workout.getUserId() != userDetails.getId()) {
                response = ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
            }
            else {
                String query = "DELETE FROM exercises WHERE workout_id=?";
                Connection connection = DriverManager.getConnection(url, user, password);
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, workout.getId());
                statement.executeUpdate();

                query = "DELETE FROM workouts WHERE workout_id=?";
                statement = connection.prepareStatement(query);
                statement.setInt(1, workout.getId());
                statement.executeUpdate();
                connection.close();
                response = ResponseEntity.ok(HttpStatus.OK);
            }

        }
        catch(Exception e) {
            System.out.println(e);
        }
        return response;
    }

    public static ResponseEntity<?> deleteExercise(Integer exerciseId) {

        ResponseEntity<?> response = ResponseEntity.ok(HttpStatus.PROCESSING);
        try {

            CustomUserDetails userDetails = (CustomUserDetails)SecurityContextHolder.getContext().
            getAuthentication().getPrincipal();

            String query = "SELECT * FROM exercises e INNER JOIN workouts " +
            "w on w.workout_id = e.workout_id WHERE exercise_id=?";

            Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, exerciseId);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                if (resultSet.getInt("user_id") != userDetails.getId()) {
                    response = ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
                }
                else {
                    query = "DELETE FROM exercises WHERE exercise_id=?";
                    connection = DriverManager.getConnection(url, user, password);
                    statement = connection.prepareStatement(query);
                    statement.setInt(1, exerciseId);
                    statement.executeUpdate();
                    response = ResponseEntity.ok(HttpStatus.OK);
                }
            }

            connection.close();
        }
        catch(SQLException e){
            System.out.println(e);
        }

        return response;
    }
    public static Workout getWorkoutById(Integer workoutId, Integer userId) {
        try {
            String query = "SELECT * FROM workouts WHERE workout_id=? and user_id=?";
            Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, workoutId);
            statement.setInt(2, userId);
            ResultSet resultSet = statement.executeQuery();
            Workout workout = new Workout();
            
            while (resultSet.next()) {
                workout.setId(resultSet.getInt("workout_id"));
                workout.setTitle(resultSet.getString("title"));
                workout.setDate(resultSet.getString("date"));
                workout.setUserId(resultSet.getInt("user_id"));
            }

            query = "SELECT * FROM exercises WHERE workout_id=?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, workoutId);
            resultSet = statement.executeQuery();
            ArrayList<Exercise> exercises = new ArrayList<Exercise>();

            while (resultSet.next()) {
                Exercise exercise = new Exercise();
                exercise.setId(resultSet.getInt("exercise_id"));
                exercise.setWorkoutId(resultSet.getInt("workout_id"));
                exercise.setName(resultSet.getString("name"));
                exercise.setSets(resultSet.getInt("sets"));
                exercise.setReps(resultSet.getInt("reps"));
                exercises.add(exercise);
            }

            workout.setExercises(exercises);
            connection.close();
            return workout;
        }
        catch(SQLException e) {
            System.out.println(e);
            return null;
        }
    }

    public static void addWorkout(Workout workout) {
        try {
            String query = "INSERT INTO workouts (user_id, title, date) VALUES (?, ?, ?)";
            Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, workout.getUserId());
            statement.setString(2, workout.getTitle());
            statement.setString(3, workout.getDate());
            statement.executeUpdate();

            query = "INSERT INTO exercises (workout_id, name, sets, reps) VALUES (?, ?, ?, ?)";

            ArrayList<Workout> userWorkouts = getWorkoutsByUserId(workout.getUserId());


            int workoutId = userWorkouts.get(userWorkouts.size() - 1).getId();

            for (Exercise exercise : workout.getExercises()) {
                statement = connection.prepareStatement(query);
                statement.setInt(1, workoutId);
                statement.setString(2, exercise.getName());
                statement.setInt(3, exercise.getSets());
                statement.setInt(4, exercise.getReps());
             //   statement.setInt(5, exercise.getWeight());
                statement.executeUpdate();
            }

            connection.close();
        }
        catch(SQLException e) {
            System.out.println(e);
        }
    }

}
