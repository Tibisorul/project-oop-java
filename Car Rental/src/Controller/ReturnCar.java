package Controller;

import Model.Database;
import Model.Operation;
import Model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ReturnCar implements Operation {
    @Override
    public void operation(Database database, Scanner s, User user) {
        System.out.println("Enter the ID of the car you want to return:");
        int carId = s.nextInt();
        s.nextLine(); // Consumă newline

        try {
            // Verificăm dacă mașina este închiriată de utilizatorul curent
            String checkRentQuery = "SELECT * FROM rents WHERE \"User\" = ? AND Car = ? AND Status = 1";
            PreparedStatement checkRentStmt = database.getConnection().prepareStatement(checkRentQuery);
            checkRentStmt.setInt(1, user.getID());
            checkRentStmt.setInt(2, carId);
            ResultSet rs = checkRentStmt.executeQuery();

            if (rs.next()) {
                // Actualizăm statusul închirierii pentru a marca mașina ca returnată
                String updateRentQuery = "UPDATE rents SET Status = 0 WHERE \"User\" = ? AND Car = ? AND Status = 1";
                PreparedStatement updateRentStmt = database.getConnection().prepareStatement(updateRentQuery);
                updateRentStmt.setInt(1, user.getID());
                updateRentStmt.setInt(2, carId);
                updateRentStmt.executeUpdate();

                // Actualizăm disponibilitatea mașinii pentru a o marca ca disponibilă
                String updateCarQuery = "UPDATE cars SET Available = 0 WHERE ID = ?";
                PreparedStatement updateCarStmt = database.getConnection().prepareStatement(updateCarQuery);
                updateCarStmt.setInt(1, carId);
                updateCarStmt.executeUpdate();

                System.out.println("Car returned successfully!");
            } else {
                System.out.println("Car not found or not rented by you.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
