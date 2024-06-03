package Controller;

import java.util.Scanner;
import Model.Operation;
import Model.Database;
import Model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UpdateCar implements Operation {
    @Override
    public void operation(Database database, Scanner s, User user) {
        System.out.println("Enter the ID of the car you want to update:");
        int carID = s.nextInt();
        s.nextLine(); // Consuma newline

        try {
            // Preluăm valorile curente ale mașinii
            String query = "SELECT * FROM cars WHERE ID = ?";
            PreparedStatement pstmt = database.getConnection().prepareStatement(query);
            pstmt.setInt(1, carID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String currentBrand = rs.getString("Brand");
                String currentModel = rs.getString("Model");
                String currentColor = rs.getString("Color");
                int currentYear = rs.getInt("Year");
                double currentPrice = rs.getDouble("Price");
                int currentAvailable = rs.getInt("Available");

                // Solicităm noile valori de la utilizator
                System.out.println("Enter new Brand (the current Brand is: " + currentBrand + "):");
                String brand = s.nextLine();
                System.out.println("Enter new Model (the current Model is: " + currentModel + "):");
                String model = s.nextLine();
                System.out.println("Enter new Color (the current Color is: " + currentColor + "):");
                String color = s.nextLine();
                System.out.println("Enter new Year (the current Year is: " + currentYear + "):");
                String yearStr = s.nextLine();
                System.out.println("Enter new Price (the current Price is: " + currentPrice + "):");
                String priceStr = s.nextLine();
                System.out.println("Enter new Availability (the current Availability is: " + currentAvailable + "):");
                String availableStr = s.nextLine();

                // Determinăm noile valori
                String newBrand = brand.isEmpty() ? currentBrand : brand;
                String newModel = model.isEmpty() ? currentModel : model;
                String newColor = color.isEmpty() ? currentColor : color;
                int newYear = yearStr.isEmpty() ? currentYear : Integer.parseInt(yearStr);
                double newPrice = priceStr.isEmpty() ? currentPrice : Double.parseDouble(priceStr);
                int newAvailable = availableStr.isEmpty() ? currentAvailable : Integer.parseInt(availableStr);

                // Pregătim și executăm interogarea de actualizare
                String update = "UPDATE cars SET Brand = ?, Model = ?, Color = ?, Year = ?, Price = ?, Available = ? WHERE ID = ?";
                PreparedStatement updatePstmt = database.getConnection().prepareStatement(update);
                updatePstmt.setString(1, newBrand);
                updatePstmt.setString(2, newModel);
                updatePstmt.setString(3, newColor);
                updatePstmt.setInt(4, newYear);
                updatePstmt.setDouble(5, newPrice);
                updatePstmt.setInt(6, newAvailable);
                updatePstmt.setInt(7, carID);

                updatePstmt.executeUpdate();

                System.out.println("Car updated successfully.");
            } else {
                System.out.println("Car with ID " + carID + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
