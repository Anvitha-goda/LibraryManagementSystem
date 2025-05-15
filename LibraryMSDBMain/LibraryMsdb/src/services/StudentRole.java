package services;

import dao.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import models.*;   

public class StudentRole {
  Scanner sc;
  private StudentDAO studentData;
  private LoanDAO loanData;
  private CategoryDAO categoryData;
  private BookDAO bookData;
  private AuthorDAO authorData;


  public StudentRole(Scanner sc,StudentDAO studentData,LoanDAO loanData,CategoryDAO categoryData,BookDAO bookData,AuthorDAO authorData){
    this.sc = sc;
    this.studentData = studentData;
    this.loanData = loanData;
    this.categoryData = categoryData;
    this.bookData = bookData;
    this.authorData = authorData;

  }
  // student section
    public void studentSection() {
        System.out.print("Enter Student id: ");
        int id = Integer.parseInt(sc.nextLine());
        Student student = studentData.getStudentById(id);

        if (student == null) {
            System.out.println("Student not found. Creating new membership.");
            System.out.print("Name: ");
            String name = sc.nextLine();
            System.out.print("Phone: ");
            long phone = Long.parseLong(sc.nextLine());
            System.out.print("Email: ");
            String email = sc.nextLine();
            System.out.print("Address: ");
            String address = sc.nextLine();
            int bookCount = 0;

            student = new Student(id, name, phone, email, address, bookCount);
            studentData.insertStudenttoDB(student);
            System.out.println("Created new membership.");
            List<Student> students = studentData.getStudents();
            System.out.println("Available students are: ");
            students.forEach(System.out::println);
        }
        
  // Operations performed by Student

        while (true) {
            System.out.println("\nStudent Menu:\n1. Borrow Book\n2. Return Book\n3. Available loans\n4. Back");
            System.out.print("Enter choice: ");
            String option = sc.nextLine();

            switch (option) {
                case "1":
                    borrowBook(id);
                    break;
                case "2":
                    returnBook(id);
                    break;
                case "3":
                    availableLoans(id);
                case "4":
                    availableStudents();
                case "5":
                    return;  
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
    
    private void availableStudents() {
        List<Student> students = studentData.getStudents();
        if(students.isEmpty()){
            System.out.println("Students not found");
        }
        System.out.println("Available students are: ");
        students.forEach(System.out::println);
    }
    private void availableLoans(int studentId) {
        List<Loan> studentLoans = loanData.getLoansByStudentId(studentId);
        if(studentLoans.isEmpty()){
            System.out.println("You did not borrowed or returned any books.");
        }
        System.out.println("Available loans are: ");
        studentLoans.stream().forEach(l -> System.out.println("Loan Id: " + l.getId() + "  Book Id: " + l.getBookId() + "  Book Title: " + l.getBookTitle() + "  Borrow Date: " + l.getLoanDate() + "  Return Date: " + l.getReturnDate()));
    }

    // Borrow book
    private void borrowBook(int studentId) {
        Student student = studentData.getStudentById(studentId);
        if (student.getBorrowedBooksCount() >= 5) {
            System.out.println("Borrow limit reached (5 books).");
            return;
        }

        List<Category> categories = categoryData.getAllCategories();
        if(categories.isEmpty()){
            System.out.println("No categories available");
            return;
        }else{
            System.out.println("Available categories: ");
            categories.stream().forEach(c -> System.out.println(c.getBranch() + " " + c.getDescription()));
        }

        String[] branchHolder = new String[1];               // to use anyMatch in stream string should not be reassigned
        while(true){
            System.out.print("Enter Category name: ");
            branchHolder[0] = sc.nextLine().trim().toUpperCase();
            boolean found = categories.stream().anyMatch(c -> c.getBranch().equals(branchHolder[0]));

            if(!found){ 
                System.out.print("Given category name is invalid, Try again? (yes/no): ");
                if(!sc.nextLine().trim().equalsIgnoreCase("yes")) return;
                continue;
            } 
            break;
        }

        String branch = branchHolder[0];
        
        int catId = categoryData.getCategoryIdByName(branch);

        List<Book> books = bookData.getBooksByCategoryId(catId);

        if (books.isEmpty()) {
            System.out.println("No books available in this category.");
            return;
        }else{
            System.out.println("All available books by given category: ");
            books.stream().forEach(b -> System.out.println(b.getId() + ": " + b.getTitle() + " (" + b.getYear() + "),Category Name: " + branch +", Author Name: " + authorData.getAuthorNameById(b.getAuthorId()) + ", Copies: " + b.getCopies()));
        }

        System.out.print("Enter Book ID to borrow: ");
        int bookId = Integer.parseInt(sc.nextLine());

        Book book = books.stream()
            .filter(b -> (b.getId()==bookId))
            .findFirst()
            .orElse(null);

        if (book == null || book.getCopies() <= 0) {
            System.out.println("Book not available.");
            return;
        }

        ArrayList<Loan> studentLoanList = loanData.getLoanofStudent(studentId);
        if(studentLoanList.stream().anyMatch(l -> l.getBookId() == bookId)){
            System.out.println("You have already borrowed this book.");
            return;
        }

        bookData.updateBookCopies(bookId,book.getCopies()-1);
        studentData.updateBookCount(studentId, student.getBorrowedBooksCount()+1);
        loanData.insertLoanToDB(studentId, bookId);
        System.out.println("Book borrowed successfully.");
    }

  // Return book
    private void returnBook(int studentId) {
        Student student=studentData.getStudentById(studentId);

        if(student.getBorrowedBooksCount() <= 0) {
            System.out.println("You did not borrow any books.");
            return;
        }
        ArrayList<Loan> studentLoanList = loanData.getLoanofStudent(studentId);
        System.out.println("Available loans are: ");
        studentLoanList.stream().forEach(s-> System.out.println(s));

        System.out.print("Enter book id to return: ");
        int bookId = Integer.parseInt(sc.nextLine()); 

        if(!studentLoanList.stream().anyMatch(l -> l.getBookId() == bookId)){
            System.out.println("You did not borrow this book");
            return;
        }
        Book book = bookData.getBookById(bookId);

        if(book != null){
            bookData.updateBookCopies(bookId,book.getCopies()+1);
            studentData.updateBookCount(studentId, student.getBorrowedBooksCount()-1);
            loanData.updateReturnDateByBookIdandStudentId(bookId, studentId);
            System.out.println("Book returned successfully.");
        }else{
            System.out.println("Book data not found.");
        }
    }
}
