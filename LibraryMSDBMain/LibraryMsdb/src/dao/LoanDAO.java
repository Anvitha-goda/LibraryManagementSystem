package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Loan;

public class LoanDAO {
  Connection con;

    public LoanDAO(Connection con) {
        this.con = con;
    }

    public List<Loan> getLoansByStudentId(int studentId){
        List<Loan> loans = new ArrayList<>();
        String query = "SELECT l.loanId,l.studentId,l.bookID,b.bookTitle,l.loanDateTime,l.returnDate FROM loan as l join book as b on l.bookId = b.bookId where studentId = ?";
         
        try (PreparedStatement ps = con.prepareStatement(query)){
                ps.setInt(1,studentId);
                ResultSet rs = ps.executeQuery();
                int loan_id,book_id;
                String loanDateTime,bookTitle,returnDate;
                while(rs.next()){
                    loan_id = rs.getInt(1);
                    book_id = rs.getInt(3);
                    bookTitle = rs.getString(4);
                    loanDateTime = rs.getString(5);
                    if(rs.getString(6) == null) returnDate = null;
                    else returnDate = rs.getString(6);
                    loans.add(new Loan(loan_id,studentId,book_id,bookTitle,loanDateTime,returnDate));
                }
            ps.close();
        } catch (SQLException e) {
            System.out.println("Loans are not available" + e);
        }
        return loans;
    } 

    public void insertLoanToDB(int studentId,int bookId){
        String query = "INSERT INTO loan (studentId,bookId) values (?,?)";
        try (PreparedStatement ps = con.prepareStatement(query)){
                ps.setInt(1,studentId);
                ps.setInt(2,bookId);
                ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println("loan insertion not possible" + e);
        }
    }

    public void updateReturnDateByBookIdandStudentId(int bookId,int studentId){
        String query = "UPDATE loan SET returnDate = current_timestamp() where bookId = ? and studentId = ? ";
        try (PreparedStatement ps = con.prepareStatement(query)){
                ps.setInt(1,bookId);
                ps.setInt(2,studentId);
                ps.executeUpdate();
            ps.close();
        }catch(SQLException e){
            System.out.println("Return date not updated." + e);
        }
    }

    public ArrayList<Loan> getLoanofStudent(int studentId){
        ArrayList<Loan> loanlist=new ArrayList<>();
        String query = "Select l.loanId,l.bookID,l.studentId, l.loanDateTime, b.bookTitle from loan as l join book as b on l.bookId = b.bookId where l.studentId=? and l.returnDate is null ";
        try (PreparedStatement ps = con.prepareStatement(query)){
                ps.setInt(1,studentId);
                ResultSet rs= ps.executeQuery();
                int loan_id,book_id;
                String datetime,bookTitle;
                while(rs.next()){
                    loan_id=rs.getInt(1);
                    book_id = rs.getInt(2);
                    datetime=rs.getString(4);
                    bookTitle=rs.getString(5);
                    loanlist.add(new Loan(loan_id,book_id, studentId, datetime, bookTitle) );
                }
            
            ps.close();
            return loanlist;
        }catch(SQLException e){
            System.out.println("Return date not updated." + e);
        }
        return loanlist;
    }

}
