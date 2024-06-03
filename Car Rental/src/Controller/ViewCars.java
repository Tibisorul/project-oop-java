package Controller;

import Model.Database;
import Model.Operation;
import Model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ViewCars implements Operation {
    @Override
    public void operation(Database database, Scanner s, User user) {
        try {
            // Modificăm interogarea pentru a exclude mașinile șterse
            String query = "SELECT * FROM cars WHERE Available != 2;";
            ResultSet rs = database.getStatement().executeQuery(query);
            System.out.println("List of available cars:");
            while (rs.next()) {
                int id = rs.getInt("ID");
                String brand = rs.getString("Brand");
                String model = rs.getString("Model");
                String color = rs.getString("Color");
                int year = rs.getInt("Year");
                double price = rs.getDouble("Price");
                int available = rs.getInt("Available");
                String status = available == 0 ? "Available" : "Rented";
                System.out.printf("ID: %d, Brand: %s, Model: %s, Color: %s, Year: %d, Price: %.2f, Status: %s\n",
                        id, brand, model, color, year, price, status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
