package services;
// import dao.*;
import java.sql.*;
import java.util.*;

import dao.AuthorDAO;
import dao.BookDAO;
import dao.CategoryDAO;
import dao.LoanDAO;
import dao.StudentDAO;

public class LibraryManagementSystem {
    Connection con;  
    public Scanner sc;
    private StudentDAO studentData;
    private BookDAO bookData;
    private LoanDAO loanData;
    private CategoryDAO categoryData;
    private AuthorDAO authorData;


    public void setConnection(Connection con){
        this.con = con;
    }

    public void setScanner(Scanner sc){
        this.sc = sc;
    }

    public void initializeDAOs(){
        this.studentData = new StudentDAO(con);
        this.authorData = new AuthorDAO(con);
        this.categoryData = new CategoryDAO(con);
        this.bookData = new BookDAO(con);
        this.loanData = new LoanDAO(con);
    }

    public void start() {

        LibrarianService librarianService = new LibrarianService(sc,categoryData,bookData,authorData,studentData);
        StudentRole studentRole = new StudentRole(sc, studentData,loanData,categoryData,bookData,authorData);

        System.out.println("Welcome to Library Management");
        while (true) {
            System.out.println("\nMain Menu:\n1. Student\n2. Librarian\n3. Exit");
            System.out.print("Enter your choice: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    studentRole.studentSection();
                    break; 
                case "2":
                    librarianService.librarianMenu();
                    break;
                case "3":
                    System.out.println("Thank you for using the system.");
                    try {
                        con.close();
                    } catch (SQLException e) {
                        System.out.println("Not possible to close connection. " + e);
                    }
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}


