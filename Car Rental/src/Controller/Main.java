package Controller;

import Model.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        Database database = new Database();
        CarService carService = new CarService();
        UserService userService = new UserService();
        Scanner s = new Scanner(System.in);

        System.out.println("Welcome to Car Rental System");

        // Sincronizează `UserService` cu baza de date
        synchronizeUserService(userService, database);

        // Adaugă câțiva utilizatori pentru testare
        Admin admin = new Admin();
        admin.setID(getNextUserId(database));
        admin.setFirstName("Admin");
        admin.setLastName("One");
        admin.setEmail("admin@example.com");
        admin.setPhoneNumber("1234567890"); // Setăm numărul de telefon
        admin.setPassword("admin");

        Client client = new Client();
        client.setID(getNextUserId(database));
        client.setFirstName("Client");
        client.setLastName("One");
        client.setEmail("client@example.com");
        client.setPhoneNumber("0987654321"); // Setăm numărul de telefon
        client.setPassword("client");

        userService.addUser(admin);
        userService.addUser(client);

        try {
            // Adăugăm utilizatorii și în baza de date
            addUserToDatabase(database, admin);
            addUserToDatabase(database, client);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Sincronizează `UserService` cu baza de date
        synchronizeUserService(userService, database);

        while (true) {
            System.out.println("Enter your email:\n(-1) to create new account");
            String email = s.next();
            if (email.equals("-1")) {
                System.out.println("Enter account type (0 for Client, 1 for Admin):");
                int accType = s.nextInt();
                AddNewAccount newAccountOperation = new AddNewAccount(accType);
                newAccountOperation.operation(database, s, null);

                // Sincronizează `UserService` cu baza de date
                synchronizeUserService(userService, database);
                continue;
            }
            System.out.println("Enter password:");
            String password = s.next();

            // Sincronizează `UserService` cu baza de date
            synchronizeUserService(userService, database);

            ArrayList<User> users = new ArrayList<>(userService.getAllUsers());

            boolean authenticated = false;
            for (User u : users) {
                if (u.getEmail().equals(email) && u.getPassword().equals(password)) {
                    authenticated = true;
                    System.out.println("Welcome " + u.getFirstName() + "!");
                    u.showList(database, s);
                    break;
                }
            }

            if (!authenticated) {
                System.out.println("Invalid email or password. Please try again.");
            }
        }
    }

    private static void synchronizeUserService(UserService userService, Database database) {
        try {
            String select = "SELECT * FROM users;";
            ResultSet rs = database.getStatement().executeQuery(select);
            userService.clearUsers(); // Curăță utilizatorii existenți
            while (rs.next()) {
                User user;
                int ID = rs.getInt("ID");
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");
                String em = rs.getString("Email");
                String phoneNumber = rs.getString("PhoneNumber");
                String pass = rs.getString("Password");
                int type = rs.getInt("Type");
                switch (type) {
                    case 0:
                        user = new Client();
                        break;
                    case 1:
                        user = new Admin();
                        break;
                    default:
                        user = new Client();
                        break;
                }
                user.setID(ID);
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmail(em);
                user.setPhoneNumber(phoneNumber);
                user.setPassword(pass);
                userService.addUser(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addUserToDatabase(Database database, User user) throws SQLException {
        // Verificăm dacă ID-ul există deja în baza de date
        int id = getNextUserId(database);
        user.setID(id);

        String insert = "INSERT INTO users (ID, FirstName, LastName, Email, PhoneNumber, Password, Type) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = database.getConnection().prepareStatement(insert);
        pstmt.setInt(1, user.getID());
        pstmt.setString(2, user.getFirstName());
        pstmt.setString(3, user.getLastName());
        pstmt.setString(4, user.getEmail());
        pstmt.setString(5, user.getPhoneNumber());
        pstmt.setString(6, user.getPassword());
        pstmt.setInt(7, (user instanceof Admin) ? 1 : 0);
        pstmt.executeUpdate();
    }

    private static int getNextUserId(Database database) {
        try {
            String query = "SELECT MAX(ID) FROM users";
            ResultSet rs = database.getStatement().executeQuery(query);
            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1; // În caz de eroare, returnăm 1 ca ID implicit
    }
}
