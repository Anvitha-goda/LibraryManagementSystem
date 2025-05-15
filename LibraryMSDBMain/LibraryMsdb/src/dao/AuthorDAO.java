package dao;

import java.sql.*;
import java.util.*;

public class AuthorDAO {
    Connection con;

    public AuthorDAO(Connection con) {
        this.con = con;
    }

    public boolean updateAuthorById(String name, String birthdate, String nationality) {
        String query = "UPDATE author SET authorName = ?, authorDOB = ?, nationality = ? ";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, name);
            ps.setDate(2, java.sql.Date.valueOf(birthdate)); 
            ps.setString(3, nationality);
            int rowsUpdated = ps.executeUpdate();
            ps.close();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Failed to update author: " + e.getMessage());
            return false;
        }
    }

    public List<String> getAllAuthors() {
        List<String> authors = new ArrayList<>();
        try  {
            Statement ps = con.createStatement();
            ResultSet rs = ps.executeQuery("SELECT name FROM authors");
            while (rs.next()) {
                authors.add(rs.getString("name"));
            }
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error fetching authors: " + e.getMessage());
        }
        return authors;
    }

    public int getAuthorIdByName(String name){
        String query = "SELECT authorId FROM author WHERE authorName = ? ";
        try (PreparedStatement ps = con.prepareStatement(query)){
                ps.setString(1,name);
                ResultSet rs = ps.executeQuery();
                if(rs.next()) return rs.getInt("authorId");
            ps.close();
        }catch (SQLException e){
            System.out.println("author id not found." + e);
        }
        return 0;
    }

    public boolean isAuthorByNameAvailable(String name){
        String query = "SELECT authorId FROM author WHERE authorName = ? ";
        try (PreparedStatement ps = con.prepareStatement(query)){
                ps.setString(1,name);
                ResultSet rs = ps.executeQuery();
                if(rs.next()) return rs.getInt(1)>=1;
            ps.close();
        }catch (SQLException e){
            System.out.println("author name not available." + e);
        }
        return false;
    }

    public String getAuthorNameById(int id){
        String query = "SELECT authorName FROM author WHERE authorId = ? ";
        try (PreparedStatement ps = con.prepareStatement(query)){
                ps.setInt(1,id);
                ResultSet rs = ps.executeQuery();
                if(rs.next()) return rs.getString("authorName");
            ps.close();
        }catch (SQLException e){
            System.out.println("author name not found." + e);
        }
        return null;
    }

    public boolean addAuthor(String name, String birthdate, String nationality) {
        String query = "INSERT INTO author (authorName, authorDOB, nationality) VALUES (?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, name);
                ps.setDate(2, java.sql.Date.valueOf(birthdate)); 
                ps.setString(3, nationality);
                int rows = ps.executeUpdate();
                ps.close();
                return rows > 0;
        } catch (SQLException e) {
            System.out.println("Failed to add author: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteAuthorById(int id) {
        String query = "DELETE FROM author WHERE authorId = ?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Failed to delete author: " + e.getMessage());
            return false;
        }
    }

}
