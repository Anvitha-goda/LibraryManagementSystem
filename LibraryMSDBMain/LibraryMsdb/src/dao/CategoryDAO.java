package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Category;

public class CategoryDAO {
    Connection con;

    public CategoryDAO(Connection con){
        this.con = con;
    }

    public List<Category> getAllCategories(){
        List<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM category";
         
        try (PreparedStatement ps = con.prepareStatement(query)){
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    categories.add(new Category(
                        rs.getInt("categoryId"),
                        rs.getString("categoryName"),
                        rs.getString("categoryDescription")
                    ));
                }
            ps.close();
        } catch (SQLException e) {
            System.out.println("categories are not available" + e);
        }
        return categories;
    }

    public int getCategoryIdByName(String branch){
        String query = "SELECT categoryId FROM category WHERE categoryName = ? ";
        try (PreparedStatement ps = con.prepareStatement(query)){
                ps.setString(1,branch);
                ResultSet rs = ps.executeQuery();
                if(rs.next()) return rs.getInt("categoryId");
            ps.close();
        }catch (SQLException e){
            System.out.println("Category id not found." + e);
        }
        return 0;
    }

    public boolean isCategoryAvailable(String branch){
        String query = "SELECT * FROM category WHERE categoryName = ?";
        try (PreparedStatement ps = con.prepareStatement(query)){
                ps.setString(1, branch);
                ResultSet rs = ps.executeQuery();
                if(rs.next()) return rs.getString("categoryName").equalsIgnoreCase(branch);
            ps.close();
        }catch (SQLException e) {
            System.out.println("categories are not available" + e);
        }
        return false;
    }

    public boolean addCategory(String name, String description) {
        String query = "INSERT INTO category (categoryName, categoryDescription) VALUES (?, ?)";

        try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, name);
                ps.setString(2, description);
                int rowsInserted = ps.executeUpdate();
                ps.close();
                return rowsInserted > 0;
        } catch (SQLException e) {
            System.out.println("Failed to insert category: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteCategoryById(int id) {
        String query = "DELETE FROM category WHERE categoryId = ?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            int rowsDeleted = ps.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.out.println("Failed to delete category: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteCategoryByName(String name) {
        String query = "DELETE FROM category WHERE categoryName = ?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, name);
            int rowsDeleted = ps.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.out.println("Failed to delete category: " + e.getMessage());
            return false;
        }
    }

}

