package services;

import models.*;

import java.util.*;

import dao.AuthorDAO;
import dao.BookDAO;
import dao.CategoryDAO;
import dao.StudentDAO;

// Librarian Services for user
public class LibrarianService {
    private CategoryDAO categoryData;
    private BookDAO bookData;
    private  AuthorDAO authorData;
    private StudentDAO studentData;
    Scanner sc;

    public LibrarianService(Scanner sc,CategoryDAO categoryData,BookDAO bookData,AuthorDAO authorData,StudentDAO studentData) {
        this.sc = sc;
        this.categoryData = categoryData;
        this.bookData = bookData;
        this.authorData = authorData;
        this.studentData = studentData;
    }

    public void librarianMenu() {                              
        System.out.print("Enter librarian ID: ");
        String id = sc.nextLine().trim();
        if (!"admin".equals(id)) {
            System.out.println("Unauthorized access.");
            return;
        }

        while (true) {
            System.out.println("\nLibrarian Menu:\n1. Add Category\n2. Delete Category\n3. Add Book\n4. Add Available Book Copies\n5. Delete Book\n6. Add Author\n7. Delete Author\n8. Display Categories\n9. Display Authors\n10. Display Books\n11. Display Students\n12. Back");
            System.out.print("Enter your choice: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1": addCategory();  break;
                case "2": deleteCategory(); break;
                case "3": addBook(); break;
                case "4": addAvailableBookCopies(); break;
                case "5": deleteBook(); break;
                case "6": addAuthor(); break;
                case "7": deleteAuthor(); break;
                case "8": displayCategories(); break;
                case "9": displayAuthors(); break;
                case "10": displayBooks(); break;
                case "11": displayStudents(); break;
                case "12": return;
                default: System.out.println("Invalid choice.");
            } 
        }
    }

// add category
    private void addCategory() {
        while(true){
            System.out.print("Enter Branch name: ");
            String branch = sc.nextLine().trim().toUpperCase();

            Boolean exists = categoryData.isCategoryAvailable(branch);

            if(exists){
                System.out.print("Category already exists,do you want to add another category? (yes/no): ");
                if(!sc.nextLine().trim().equalsIgnoreCase("yes")) return;
                continue;
            }
            System.out.print("Enter Description: ");
            String desc = sc.nextLine();

            if (categoryData.addCategory(branch,desc)) {
                System.out.println("Category added successfully.");
            } else {
                System.out.println("Failed to add category.");
            }

            displayCategories();
            System.out.print("Add another category? (yes/no): ");
            if (!sc.nextLine().trim().equalsIgnoreCase("yes")) return;
        }
    }

// delete category
    private void deleteCategory() {
        while(true){
            System.out.print("Enter Branch name: "); 
            String branch = sc.nextLine().trim();
            Integer foundId = null;
            foundId = categoryData.getCategoryIdByName(branch);
            if (foundId == 0) {
                System.out.println("Category not found.Try again? (yes/no): ");
                if(!sc.nextLine().trim().equalsIgnoreCase("yes")) return;
                continue;
            }
            
            if (bookData.isBooksByCategoryIdAvailable(foundId)) {  
                System.out.println("Cannot delete category.Books are associated with this category.");
                System.out.print("Delete another category? (yes/no): ");
                if(!sc.nextLine().trim().equalsIgnoreCase("yes")) return;
                continue;
            }
            categoryData.deleteCategoryById(foundId);
            bookData.deleteBooksByCategoryId(foundId);
            System.out.println("Category deleted.");
            System.out.print("Delete another category? (yes/no): ");
            if (!sc.nextLine().equalsIgnoreCase("yes")) break;
        }
    }

    private void displayCategories(){
        List<Category> categories = categoryData.getAllCategories();
        if(categories.isEmpty()){
            System.out.println("No categories available");
            return;
        }else{
            System.out.println("Available categories: ");
            categories.stream().forEach(c -> System.out.println(c.getBranch() + " " + c.getDescription()));
        }
    }
    
// add author
    private void addAuthor() {
        while(true){
            System.out.print("Enter author name: ");
            String name = sc.nextLine().trim();
    
            Boolean found = authorData.isAuthorByNameAvailable(name);
            if(found){
                System.out.print("Author already exists,do you want to add another author? (yes/no): ");
                if(!sc.nextLine().trim().equalsIgnoreCase("yes")) return;
                continue;
            }
            System.out.print("Birthdate: ");
            String birthdate = sc.nextLine();
            System.out.print("Nationality: ");
            String nationality = sc.nextLine();

            if (authorData.addAuthor(name,birthdate,nationality)) {
                System.out.println("author added successfully.");
                return;
            } else {
                System.out.println("Failed to add category.");
            }
            authorData.updateAuthorById( name, birthdate, nationality);
            System.out.println("Author: " + name + " added ");

            System.out.print("Add another author? (yes/no): ");
            if (!sc.nextLine().trim().equalsIgnoreCase("yes")) return;
        }
    }

// delete author
    private void deleteAuthor() {
        while (true) {
            System.out.print("Enter author name to delete: "); 
            String name = sc.nextLine().trim();
            Integer authorId = null;
            authorId = authorData.getAuthorIdByName(name);
            if (authorId == 0) {
                System.out.print("Author not found.Try again? (yes/no): ");
                if(!sc.nextLine().trim().equalsIgnoreCase("yes")) return;
                continue;
            }

            boolean authorInUse = bookData.isBooksByAuthorIdAvailable(authorId);

            if (authorInUse) {
                System.out.println("Cannot delete author.Books are associated with this author.");
                System.out.print("Try again? (yes/no): ");
                if(!sc.nextLine().trim().equalsIgnoreCase("yes")) return;
                continue;
            }
            authorData.deleteAuthorById(authorId);
            System.out.println("Author deleted.");
            System.out.print("Delete another author? (yes/no): ");
            if (!sc.nextLine().equalsIgnoreCase("yes")) break;
        }
    }

    private void displayAuthors(){
        List<String> authors = authorData.getAllAuthors();
        if(authors.isEmpty()){
            System.out.println("No authors available");
            return;
        }else{
            System.out.println("Available authors: ");
            authors.stream().forEach(a -> System.out.println(a));
        }
    }

// add book
    private void addBook() {
        while (true) {
            System.out.print("Enter Category name: ");
            String branch = sc.nextLine().trim().toUpperCase();
            int catId = categoryData.getCategoryIdByName(branch);

            if (catId <= 0) {
                System.out.println("Invalid category. Create it first.");
                addCategory();
                catId = categoryData.getCategoryIdByName(branch);
            }

            System.out.print("Enter Author name: ");
            String authorName = sc.nextLine().trim();
            int authorId = authorData.getAuthorIdByName(authorName);

            if (authorId <= 0) {
                System.out.println("Invalid author name. Create it first.");
                addAuthor();
                authorId = authorData.getAuthorIdByName(authorName);
            }

            System.out.print("Enter Book title: ");
            String title = sc.nextLine().trim();

            System.out.print("Year: ");
            String year = sc.nextLine().trim();

            System.out.print("Number of Copies: ");
            int copies = Integer.parseInt(sc.nextLine());

            bookData.addBookToDB(authorId, catId, title, year, copies);
            System.out.println("Book added.");

            bookData.displayBooksByCategory(catId);

            System.out.print("Add another Book? (yes/no): ");
            if (!sc.nextLine().trim().equalsIgnoreCase("yes")) return;
        }
    }

    private void addAvailableBookCopies(){
        displayBooks();
        while(true){
            System.out.print("Enter Book title to add copies: ");
            String title = sc.nextLine().trim();
            if(bookData.getAllBooksByBookTitle(title).isEmpty()){
                System.out.println("Book not available with this title.");
                System.out.print("Do you want to delete another book copies? (yes/no): ");
                if(!sc.nextLine().trim().equalsIgnoreCase("yes")) return;
                continue;
            }
            System.out.println("Available book by title: ");
            List<Book> booksByTitle = bookData.getAllBooksByBookTitle(title);
            booksByTitle.forEach(System.out::println);
            
            System.out.print("Enter Book ID to add copies: ");
            int bookId = Integer.parseInt(sc.nextLine());
            if(!booksByTitle.stream().anyMatch(b -> b.getId() == bookId)){
                System.out.println("Book not found with given id.");
                System.out.print("Do you want to delete another book copies? (yes/no): ");
                if(!sc.nextLine().trim().equalsIgnoreCase("yes")) return;
                continue;
            }
            Book book = bookData.getBookById(bookId);
            System.out.print("Enter number of copies to be added: ");
            int copies = Integer.parseInt(sc.nextLine());
            bookData.updateBookCopies(bookId, book.getCopies() + copies);
            System.out.println("Book copies added.");
            System.out.print("Do you want to add another book copies? (yes/no): ");
            if(!sc.nextLine().trim().equalsIgnoreCase("yes")) break;
        }
    }

    private void deleteBook() {
        while(true){
            System.out.print("Enter Book title to delete: ");
            String title = sc.nextLine().trim();
        
            if(bookData.getAllBooksByBookTitle(title).isEmpty()){
                System.out.println("No book available.");
                System.out.print("Do you want to delete another book? (yes/no): ");
                if(!sc.nextLine().trim().equalsIgnoreCase("yes")) return;
                continue;
            }else{
                System.out.println("Available books: ");
                bookData.getAllBooksByBookTitle(title).forEach(System.out::println);
            }

            System.out.print("Enter Book ID to delete: ");
            int bookId = Integer.parseInt(sc.nextLine());
        
            Book book = bookData.getBookById(bookId);

            if (bookData.isBookOnLoan(bookId)) {
                System.out.println("Book is currently on loan. Cannot delete.");
                System.out.print("Do you want to delete another book? (yes/no): ");
                if(!sc.nextLine().trim().equalsIgnoreCase("yes")) return;
                continue;
            }

            // int copies = bookData.getAvailableCopies(bookId);
            if (book.getCopies() == 0) {
                System.out.println("Book not found.");
                System.out.print("Do you want to delete another book? (yes/no): ");
                if(!sc.nextLine().trim().equalsIgnoreCase("yes")) return;
                continue;
            }

            if (book.getCopies() > 1) {
                bookData.updateBookCopies(bookId, book.getCopies()-1);
                System.out.println("One copy of the book removed. Remaining copies: " + book.getCopies());
            } else {
                bookData.deleteBook(bookId);
                System.out.println("Book deleted.");
            }
            
            System.out.print("Do you want to delete another book? (yes/no): ");
            if(!sc.nextLine().trim().equalsIgnoreCase("yes")) break;
        }
    }

    private void displayBooks(){
        List<Book> books = bookData.getAllBooks();
        if(books.isEmpty()){
            System.out.println("No books available");
            return;
        }else{
            System.out.println("Available books: ");
            books.stream().forEach(b -> System.out.println("Book Title: " + b.getTitle() + " AuthorId: " + b.getAuthorId() + " CategoryId: " + b.getCategoryId() + " Published Year: " + b.getYear() +" Available copies: " + b.getCopies()));
        }
    }

    private void displayStudents() {
        List<Student> students = studentData.getStudents();
        if(students.isEmpty()){
            System.out.println("Students not found");
        }
        System.out.println("Available students are: ");
        students.forEach(System.out::println);
    }
}
    


