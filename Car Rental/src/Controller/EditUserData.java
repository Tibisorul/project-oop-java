package Controller;

import Model.Database;
import Model.Operation;
import Model.User;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class EditUserData implements Operation {
    @Override
    public void operation(Database database, Scanner s, User user) {
        System.out.println("Edit User Data");
        System.out.println("1. Edit First Name");
        System.out.println("2. Edit Last Name");
        System.out.println("3. Edit Email");
        System.out.println("4. Edit Phone Number");
        System.out.println("5. Edit Password");
        int choice = s.nextInt();
        s.nextLine(); // consume newline
        String newValue;
        String column = "";
        switch (choice) {
            case 1:
                System.out.println("Enter new First Name:");
                newValue = s.nextLine();
                column = "FirstName";
                break;
            case 2:
                System.out.println("Enter new Last Name:");
                newValue = s.nextLine();
                column = "LastName";
                break;
            case 3:
                System.out.println("Enter new Email:");
                newValue = s.nextLine();
                column = "Email";
                break;
            case 4:
                System.out.println("Enter new Phone Number:");
                newValue = s.nextLine();
                column = "PhoneNumber";
                break;
            case 5:
                System.out.println("Enter new Password:");
                newValue = s.nextLine();
                column = "Password";
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        try {
            String query = "UPDATE users SET " + column + " = ? WHERE ID = ?";
            PreparedStatement pstmt = database.getConnection().prepareStatement(query);
            pstmt.setString(1, newValue);
            pstmt.setInt(2, user.getID());
            pstmt.executeUpdate();
            System.out.println("User data updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
