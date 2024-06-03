package Controller;

import Model.Database;
import Model.Operation;
import Model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class AddNewAccount implements Operation {
    private int accType;

    public AddNewAccount(int accType) {
        this.accType = accType;
    }

    @Override
    public void operation(Database database, Scanner s, User user) {
        System.out.println("Enter First Name:");
        String firstName = s.next();
        System.out.println("Enter Last Name:");
        String lastName = s.next();
        System.out.println("Enter Email:");
        String email = s.next();
        System.out.println("Enter Phone Number:");
        String phoneNumber = s.next();
        System.out.println("Enter Password:");
        String password = s.next();
        System.out.println("Confirm password:");
        String confirmPassword = s.next();
        while (!password.equals(confirmPassword)) {
            System.out.println("Password doesn`t match");
            System.out.println("Enter Password:");
            password = s.next();
            System.out.println("Confirm password:");
            confirmPassword = s.next();
        }

        try {
            // Obține toate emailurile existente pentru a verifica duplicarea
            ArrayList<String> emails = new ArrayList<>();
            ResultSet rs0 = database.getStatement().executeQuery("SELECT Email FROM users");
            while (rs0.next()) {
                emails.add(rs0.getString("Email"));
            }
            if (emails.contains(email)) {
                System.out.println("This email is used before");
                return;
            }

            // Obține numărul maxim de ID-uri pentru a calcula noul ID
            ResultSet rs = database.getStatement().executeQuery("SELECT MAX(ID) FROM users;");
            rs.next();
            int ID = rs.getInt(1) + 1;

            // Pregătește interogarea SQL
            String insert = "INSERT INTO users (ID, FirstName, LastName, Email, PhoneNumber, Password, Type) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = database.getConnection().prepareStatement(insert);
            pstmt.setInt(1, ID);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setString(4, email);
            pstmt.setString(5, phoneNumber);
            pstmt.setString(6, password);
            pstmt.setInt(7, accType);

            // Execută interogarea
            pstmt.executeUpdate();

            System.out.println("New account added successfully with ID: " + ID);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
