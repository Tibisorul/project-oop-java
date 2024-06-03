package Model;

import Controller.*;
// Import other operations

import java.util.Scanner;

public class Admin extends User {
    private Operation[] operations = new Operation[] {
            new AddNewAccount(1),
            new ViewCars(),
            new RentCar(),
            new ReturnCar(),
            new AddNewAdmin(),
            new ShowRents(),
            new AddNewCar(),
            new UpdateCar(),
            new DeleteCar()
            // Add other operations here
    };

    public Admin(){
        super();
    }

    @Override
    public void showList(Database database, Scanner s){
        while (true) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Add new Car");
            System.out.println("2. View Cars");
            System.out.println("3. Update Car");
            System.out.println("4. Delete Car");
            System.out.println("5. Add new Admin");
            System.out.println("6. Show Rents");
            System.out.println("7. Quit\n");

            int i = s.nextInt();

            switch (i) {
                case 1:
                    operations[6].operation(database, s, this);
                    break;
                case 2:
                    operations[1].operation(database, s, this);
                    break;
                case 3:
                    operations[7].operation(database, s, this); // Logic to Update Car
                    break;
                case 4:
                    operations[8].operation(database, s, this);  // Logic to Delete Car
                    break;
                case 5:
                    operations[4].operation(database, s, this); // Noua opțiune pentru adăugarea unui admin
                    break;
                case 6:
                    operations[5].operation(database, s, this); // Show Rents
                    break;
                case 7:
                    System.out.println("Logging out...");
                    return; // Exit method to close the menu
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
