
import java.sql.Connection;
import java.util.Scanner;

import services.*;
import util.DBConnection;

public class App {
    public static void main(String[] args) throws Exception {
        DBConnection dbConn = new DBConnection();
        Connection con= dbConn.getConnection();

         if (con == null) {
            System.out.println("Database connection could not be established. Exiting...");
            return;
        }

        LibraryManagementSystem system = new LibraryManagementSystem();
        system.setConnection(con);
        system.setScanner(new Scanner(System.in));
        system.initializeDAOs();
        system.start();
    }
}


