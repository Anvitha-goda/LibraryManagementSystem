package models;

public class Loan {
    private int id;
    private  int studentId;
    private  int bookId;
    private  String loanDate;
    private String returnDate;
    private String bookTitle;
    
    public Loan(int id,int studentId, int bookId,String bookTitle, String loanDate, String returnDate) {
        this.id = id;
        this.studentId = studentId;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
    }
    
    public Loan(int id,int bookID, int studentId, String loanDate, String bookTitle) {
        this.id = id;
        this.bookId = bookID;
        this.studentId = studentId;
        this.loanDate = loanDate;
        this.bookTitle = bookTitle;
    }

    public int getId() { return id; }
    public int getStudentId() { return studentId; }
    public int getBookId() { return bookId; }
    public String getLoanDate() { return loanDate; }
    public String getReturnDate() { return returnDate; }
    public String getBookTitle() { return bookTitle; }
    

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public String toString() {
        return ("id=" + id + ", bookId=" + bookId + ", studentId=" + studentId + ", loanDate=" + loanDate + " ,Booktitle= "+ bookTitle );
    }
    
}
