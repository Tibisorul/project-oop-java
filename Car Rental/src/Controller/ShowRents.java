package Controller;

import Model.Database;
import Model.Operation;
import Model.User;
import Model.Admin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ShowRents implements Operation {
    @Override
    public void operation(Database database, Scanner s, User user) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ResultSet carRs = null;
        try {
            String query;
            if (user instanceof Admin) {
                query = "SELECT r.ID, r.Car, r.DateTime, r.Hours, r.Total, r.Status, u.FirstName, u.LastName, u.Email " +
                        "FROM rents r JOIN users u ON r.\"User\" = u.ID";
            } else {
                query = "SELECT r.ID, r.Car, r.DateTime, r.Hours, r.Total, r.Status, u.FirstName, u.LastName, u.Email " +
                        "FROM rents r JOIN users u ON r.\"User\" = u.ID WHERE u.ID = ?";
            }
            stmt = database.getConnection().prepareStatement(query);
            if (!(user instanceof Admin)) {
                stmt.setInt(1, user.getID());
            }
            rs = stmt.executeQuery();
            System.out.println("Rental history:");

            while (rs.next()) {
                int carId = rs.getInt("Car");
                String dateTime = rs.getString("DateTime");
                int hours = rs.getInt("Hours");
                double total = rs.getDouble("Total");
                int status = rs.getInt("Status");
                String statusText = (status == 1) ? "Rented" : "Returned";
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");
                String email = rs.getString("Email");

                // Interogare suplimentară pentru a obține informațiile despre mașină
                String carQuery = "SELECT * FROM cars WHERE ID = ?";
                PreparedStatement carStmt = database.getConnection().prepareStatement(carQuery);
                carStmt.setInt(1, carId);
                carRs = carStmt.executeQuery();

                if (carRs.next()) {
                    String brand = carRs.getString("Brand");
                    String model = carRs.getString("Model");
                    String color = carRs.getString("Color");

                    System.out.printf("Car ID: %d, Brand: %s, Model: %s, Color: %s, Rented by: %s %s (%s), DateTime: %s, Hours: %d, Total: %.2f, Status: %s\n",
                            carId, brand, model, color, firstName, lastName, email, dateTime, hours, total, statusText);
                }
                carStmt.close();
                carRs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (carRs != null) carRs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
