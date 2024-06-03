package Model;

import Model.Database;
import java.util.Scanner;
import Model.User;

public interface Operation {
    public void operation(Database database, Scanner s,User user);
}
