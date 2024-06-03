package Controller;

import Model.Operation;
import Model.Database;
import Model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AddNewCar implements Operation {
    @Override
    public void operation(Database database, Scanner s, User user) {
        System.out.println("Enter Car Brand:");
        String brand = s.next();
        System.out.println("Enter Car Model:");
        String model = s.next();
        System.out.println("Enter Car Color:");
        String color = s.next();
        System.out.println("Enter Car Year:");
        int year = s.nextInt();
        System.out.println("Enter Car Price:");
        double price = s.nextDouble();
        System.out.println("Enter Car Availability (0 for available, 1 for rented, 2 for deleted):");
        int available = s.nextInt();

        try {
            // Obține numărul maxim de ID-uri pentru a calcula noul ID
            ResultSet rs = database.getStatement().executeQuery("SELECT MAX(ID) AS max_id FROM cars;");
            rs.next();
            int ID = rs.getInt("max_id") + 1;

            // Pregătește interogarea SQL
            String insert = "INSERT INTO cars (ID, Brand, Model, Color, Year, Price, Available) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = database.getConnection().prepareStatement(insert);
            pstmt.setInt(1, ID);
            pstmt.setString(2, brand);
            pstmt.setString(3, model);
            pstmt.setString(4, color);
            pstmt.setInt(5, year);
            pstmt.setDouble(6, price);
            pstmt.setInt(7, available);

            // Execută interogarea
            pstmt.executeUpdate();

            System.out.println("New car added successfully with ID: " + ID);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
