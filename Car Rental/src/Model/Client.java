package Model;

import Controller.ViewCars;
import Controller.RentCar;
import Controller.ReturnCar;
import Controller.ShowRentsClient;
import Controller.EditUserData;
import java.util.Scanner;

public class Client extends User {
    private Operation[] operations = new Operation[] {
            new ViewCars(),
            new RentCar(),
            new ReturnCar(),
            new ShowRentsClient(),
            new EditUserData(),
    };

    public Client(){
        super();
    }

    @Override
    public void showList(Database database, Scanner s) {
        while (true) {
            System.out.println("\nClient Menu:");
            System.out.println("1. View Cars");
            System.out.println("2. Rent Car");
            System.out.println("3. Return Car");
            System.out.println("4. Show my Rents");
            System.out.println("5. Edit My Data");
            System.out.println("6. Quit\n");

            int i = s.nextInt();

            switch (i) {
                case 1:
                    operations[0].operation(database, s, this);
                    break;
                case 2:
                    operations[1].operation(database, s, this);
                    break;
                case 3:
                    operations[2].operation(database, s, this);
                    break;
                case 4:
                    operations[3].operation(database, s, this);
                    break;
                case 5:
                    operations[4].operation(database, s, this);
                    break;
                case 6:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
