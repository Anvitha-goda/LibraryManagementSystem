package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public  class DBConnection{
   
    private final String URL = "jdbc:mysql://localhost:3306/libraryData";
    private final String USER = "root";
    private final String PASSWORD = "root";

    public Connection getConnection(){
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); 
            System.out.println("Connecting to database...");
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connection successful!");
        } catch (ClassNotFoundException e) {
            System.out.println("MYSQL JDBC Driver not found: " + e);
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e);
        }
        return con;
    }
}













// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.SQLException;

// public class DBConnection {
//     private static final String URL = "jdbc:mysql://localhost:3306/libraryData";
//     private static final String USER = "root";
//     private static final String PASSWORD = "root";

//     public static Connection getConnection() throws SQLException {
//         return DriverManager.getConnection(URL, USER, PASSWORD);
//     }
// }

