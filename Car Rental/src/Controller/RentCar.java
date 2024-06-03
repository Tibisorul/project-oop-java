package Controller;

import Model.Database;
import Model.Operation;
import Model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class RentCar implements Operation {
    @Override
    public void operation(Database database, Scanner s, User user) {
        System.out.println("Enter the ID of the car you want to rent:");
        int carId = s.nextInt();
        System.out.println("Enter the number of hours you want to rent the car:");
        int hours = s.nextInt();
        try {
            // Verificăm dacă mașina există și este disponibilă
            String checkCarQuery = "SELECT Price, Available FROM cars WHERE ID = ?";
            PreparedStatement checkCarStmt = database.getConnection().prepareStatement(checkCarQuery);
            checkCarStmt.setInt(1, carId);
            ResultSet rs = checkCarStmt.executeQuery();

            if (rs.next()) {
                int available = rs.getInt("Available");
                if (available == 0) {
                    double pricePerHour = rs.getDouble("Price");
                    double total = pricePerHour * hours;

                    // Obțineți un nou ID unic pentru închiriere
                    ResultSet rsMaxId = database.getStatement().executeQuery("SELECT MAX(ID) AS max_id FROM rents;");
                    rsMaxId.next();
                    int newRentId = rsMaxId.getInt("max_id") + 1;

                    // Inserăm închirirea în baza de date
                    String query = "INSERT INTO rents (ID, \"User\", Car, DateTime, Hours, Total, Status) VALUES (?, ?, ?, NOW(), ?, ?, ?)";
                    PreparedStatement pstmt = database.getConnection().prepareStatement(query);
                    pstmt.setInt(1, newRentId);
                    pstmt.setInt(2, user.getID());
                    pstmt.setInt(3, carId);
                    pstmt.setInt(4, hours);
                    pstmt.setDouble(5, total);
                    pstmt.setInt(6, 1);
                    pstmt.executeUpdate();

                    // Actualizăm disponibilitatea mașinii
                    String updateCarQuery = "UPDATE cars SET Available = 1 WHERE ID = ?";
                    PreparedStatement updateCarStmt = database.getConnection().prepareStatement(updateCarQuery);
                    updateCarStmt.setInt(1, carId);
                    updateCarStmt.executeUpdate();

                    System.out.println("Car rented successfully!");
                } else {
                    System.out.println("Car is not available for rent.");
                }
            } else {
                System.out.println("Car not found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
