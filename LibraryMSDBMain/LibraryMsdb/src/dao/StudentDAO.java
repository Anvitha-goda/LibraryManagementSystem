package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import models.*;

public class StudentDAO {
    Connection con;

    public StudentDAO(Connection con){
        this.con = con;
    }

    public Student getStudentById(int studentId){
        Student student = null;
        String query = "SELECT * FROM student WHERE studentId = (?) ";
        try (PreparedStatement ps = con.prepareStatement(query)){
                ps.setInt(1,studentId);
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    student = new Student(
                        rs.getInt("studentId"),
                        rs.getString("studentName"),
                        rs.getLong("contactNumber"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getInt("bookCount")
                    );
                }
            ps.close();
        } catch (SQLException e) {
            System.out.println("student not found " + e);
        }
        return student;
    }

    public void insertStudenttoDB(Student student){
        String query = "INSERT INTO student (studentId,studentName,contactNumber,email,address,bookCount) values (?,?,?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(query)){
                ps.setInt(1,student.getId());
                ps.setString(2,student.getName());
                ps.setLong(3,student.getPhone());
                ps.setString(4,student.getEmail());
                ps.setString(5,student.getAddress());
                ps.setInt(6,student.getBorrowedBooksCount());
                ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Student insertion not possible" + e);
        }
    }

    public List<Student> getStudents(){
        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM student";
         
        try (PreparedStatement ps = con.prepareStatement(query)){
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    students.add(new Student(
                        rs.getInt("studentId"),
                        rs.getString("studentName"),
                        rs.getLong("contactNumber"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getInt("bookCount")
                    ));
                }
            ps.close();
        } catch (SQLException e) {
            System.out.println("Students are not available" + e);
        }
        return students;
    }


    public void updateBookCount(int studentID,int newCount){
        String query = "UPDATE student SET bookCount = ? WHERE studentID = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, newCount);
                ps.setInt(2, studentID);
                ps.executeUpdate();
            ps.close();
        }catch(SQLException e){
            System.out.println("Unable to update student books count" + e);
        }
    }

}
