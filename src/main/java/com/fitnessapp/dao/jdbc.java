package com.fitnessapp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class jdbc {
    static final String db_url = "jdbc:mysql://aa9xz8i0zjsz4s.ct7fhre5oddy.us-east-2.rds.amazonaws.com:3306/fitness";
    static final String username = "";
    static final String password = "";

    public static void testQ() {

        try {
            Connection connection = DriverManager.getConnection(db_url, username, password);
            Statement statement = connection.createStatement();
            String query = "INSERT INTO exercises (workout_id, exercise_id) VALUES (1, 8)";
            statement.executeUpdate(query);
        }
        catch(SQLException e) {
            System.out.println(e);
        }
    }
}
