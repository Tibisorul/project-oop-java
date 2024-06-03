package Controller;

import java.util.Scanner;
import Model.Operation;
import Model.Database;
import Model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeleteCar implements Operation {
    @Override
    public void operation(Database database, Scanner s, User user) {
        System.out.println("Enter the ID of the car you want to delete:");
        int carID = s.nextInt();
        s.nextLine(); // Consuma newline

        try {
            // Verificăm dacă mașina există
            String query = "SELECT * FROM cars WHERE ID = ?";
            PreparedStatement pstmt = database.getConnection().prepareStatement(query);
            pstmt.setInt(1, carID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String brand = rs.getString("Brand");
                String model = rs.getString("Model");
                System.out.println("Are you sure you want to delete the car with ID " + carID + " (Brand: " + brand + ", Model: " + model + ")? (yes/no)");
                String confirmation = s.nextLine();

                if (confirmation.equalsIgnoreCase("yes")) {
                    // Actualizăm statutul mașinii pentru a o marca ca ștearsă
                    String update = "UPDATE cars SET Available = ? WHERE ID = ?";
                    PreparedStatement updatePstmt = database.getConnection().prepareStatement(update);
                    updatePstmt.setInt(1, 2); // 2 pentru statutul șters
                    updatePstmt.setInt(2, carID);

                    updatePstmt.executeUpdate();

                    System.out.println("Car with ID " + carID + " has been marked as deleted.");
                } else {
                    System.out.println("Car deletion cancelled.");
                }
            } else {
                System.out.println("Car with ID " + carID + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
