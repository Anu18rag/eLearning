package eLearning;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection  {
    public static Connection connect() {
        try {
            String url = "jdbc:mysql://localhost:3306/elearning";
            String user = "root"; // Your MySQL username
            String password = "password"; // Your MySQL password
            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

