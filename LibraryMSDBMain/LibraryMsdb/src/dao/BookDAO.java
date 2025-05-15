package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Book;

public class BookDAO  {
    private Connection con;

    public BookDAO(Connection con) {
        this.con = con;
    }

    public String getBookTitleById(int id){
        String query = "SELECT bookTitle FROM book WHERE bookId = ? ";
        try (PreparedStatement ps = con.prepareStatement(query)){
                ps.setInt(1,id);
                ResultSet rs = ps.executeQuery();
                if(rs.next()) return rs.getString("bookTitle");
            ps.close();
        }catch (SQLException e){
            System.out.println("book title not found." + e);
        }
        return null;
    }


    public List<Book> getBooksByCategoryId(int categoryId) {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM book WHERE categoryId = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, categoryId);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    books.add(new Book(
                        rs.getInt("bookId"),
                        rs.getInt("authorID"),
                        rs.getInt("categoryId"),
                        rs.getString("bookTitle"),
                        rs.getString("pubYear"),
                        rs.getInt("bookCopies")
                    ));
                }
            ps.close();
        }catch(SQLException e){
            System.out.println("Books not found with category id." + e);
        }
        return books;
    }

    public boolean isBooksByCategoryIdAvailable(int categoryId) {
        String query = "SELECT * FROM book WHERE categoryId = ? ";
        try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, categoryId);
                ResultSet rs = ps.executeQuery();
                if(rs.next()) return true;
            ps.close();
        }catch(SQLException e){
            System.out.println("Books not found with category id." + e);
        }
        return false;
    }

    public boolean isBooksByAuthorIdAvailable(int authorID) {
        String query = "SELECT * FROM book WHERE authorId = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, authorID);
                ResultSet rs = ps.executeQuery();
                if(rs.next()) return true;
            ps.close();
        }catch(SQLException e){
            System.out.println("Books not found with author id." + e);
        }
        return false;
    }

    public int countBooksByCategory(int categoryId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM book WHERE categoryId = ? AND bookCopies > 0";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, categoryId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) return rs.getInt(1);
            ps.close();
        }
        return 0;
    }

    public int getAvailableCopies(int bookId) {
        String query = "SELECT bookCopies FROM book WHERE categoryId = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, bookId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) return rs.getInt("bookCopies");
            ps.close();
        }catch(SQLException e){
            System.out.println("Unable to get available book copies" + e);
        }
        return 0;
    }

    public void updateBookCopies(int bookId, int newCopies) {
        String query = "UPDATE book SET bookCopies = ? WHERE bookId = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, newCopies);
                ps.setInt(2, bookId);
                ps.executeUpdate();
            ps.close();
        }catch(SQLException e){
            System.out.println("Unable to update book copies" + e);
        }
    }

    public void displayBooksByCategory(int categoryId) {
        List<Book> books = getBooksByCategoryId(categoryId);
        if (books.isEmpty()) {
            System.out.println("No books available in this category.");
            return;
        }
        System.out.println("Books in this category:");
        for (Book book : books) {
            System.out.printf("ID: %d | Title: %s | Copies: %d\n",
                book.getId(), book.getTitle(), book.getCopies());
        }
    }

    public Book getBookById(int bookId){
        Book book = null;

        String query = "SELECT * FROM book WHERE bookId = (?) ";
        try (PreparedStatement ps = con.prepareStatement(query)){
                ps.setInt(1,bookId);
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    book = new Book(
                        rs.getInt("bookId"),
                        rs.getInt("authorID"),
                        rs.getInt("categoryId"),
                        rs.getString("bookTitle"),
                        rs.getString("pubYear"),
                        rs.getInt("bookCopies")
                    );
                }
            ps.close();
        } catch (SQLException e) {
            System.out.println("Book not found by id" + e);
        }
        return book;
    }

    public void addBookToDB(int authorId,int categoryId,String bookTitle,String pubYear,int bookCopies){
        String query = "INSERT INTO book (authorId,categoryId,bookTitle, pubYear,bookCopies) values (?,?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1,authorId);
                ps.setInt(2,categoryId);
                ps.setString(3,bookTitle);
                ps.setString(4,pubYear);
                ps.setInt(5,bookCopies);
                ps.executeUpdate();
            ps.close();
        }catch(SQLException e){
            System.out.println("Book not inserted" + e);
        }

    }

    public List<Book> getAllBooksByBookTitle(String bookTitle){
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM book WHERE bookTitle = ?";
        try(PreparedStatement ps = con.prepareStatement(query)){
            ps.setString(1, bookTitle);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                books.add(new Book(
                    rs.getInt("bookId"),
                    rs.getInt("authorId"),
                    rs.getInt("categoryId"),
                    rs.getNString("bookTitle"),
                    rs.getString("pubYear"),
                    rs.getInt("bookCopies")
                ));
            }
        }catch(SQLException e){
            System.out.println("Books are not available." + e);
        }
        return books;
    }

    public boolean isBookOnLoan(int bookId){
        String query = "SELECT COUNT(*) FROM loan WHERE bookId = ? AND returnDate IS NULL";
        try(PreparedStatement ps = con.prepareStatement(query)){
                ps.setInt(1,bookId);
                ResultSet rs = ps.executeQuery();
                if(rs.next()) return rs.getInt(1) > 0;
            ps.close();
        }catch(SQLException e){
            System.out.println("Checking of book in loan not possible." + e);
        }
        return false;
    }

    public void deleteBook(int bookId)  {
        String query = "DELETE FROM book WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, bookId);
                ps.executeUpdate();
            ps.close();
        }catch(SQLException e){
            System.out.println("book deletion not possible" + e);
        }
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM book";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                books.add(new Book(
                    rs.getInt("bookId"),
                    rs.getInt("authorId"),
                    rs.getInt("categoryId"),
                    rs.getString("bookTitle"),
                    rs.getString("pubYear"),
                    rs.getInt("bookCopies")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Books are not available: " + e.getMessage());
        }

        return books;
    }

    public void deleteBooksByCategoryId(int categoryId){
        String query = "DELETE FROM book WHERE categoryId = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, categoryId);
                ps.executeUpdate();
            ps.close();
        }catch(SQLException e){
            System.out.println("book deletion not possible" + e);
        }
    }


}
