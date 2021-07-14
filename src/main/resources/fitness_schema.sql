USE fitness;

CREATE TABLE workouts(
	workout_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id int,
    date VARCHAR(20),
    title VARCHAR(20),
    CONSTRAINT fk_user FOREIGN KEY(user_id)
    REFERENCES users(id));
    
CREATE TABLE exercises(
	exercise_id INT AUTO_INCREMENT PRIMARY KEY,
    workout_id INT,
    name VARCHAR(100),
    sets int,
    reps int,
    weight int,
    CONSTRAINT fk_workouts FOREIGN KEY(workout_id)
    REFERENCES workouts(workout_id)
    );
    
select * from user_role;
delete from workouts where workout_id = 2;