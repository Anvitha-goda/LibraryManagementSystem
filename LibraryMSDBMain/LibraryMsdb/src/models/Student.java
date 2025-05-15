package models;

import java.io.Serializable;

public class Student implements Serializable {
    private static final long serialVersionUID = 4L;
    private int id;
    private long phone;
    private String name,email,address;
    private int bookCount = 0;

    public Student( int id, String name, long phone, String email, String address, int bookCount) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.bookCount = bookCount;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public long getPhone() { return phone; }
    public String getEmail() { return email; }  
    public String getAddress() { return address; }
    public int getBorrowedBooksCount() { return bookCount; }

    //  Method to borrow a book
    public void borrowBook() {
        bookCount++;
    }

    //  Method to return a book
    public void returnBook() {
        bookCount--;
    }

    @Override
    public String toString() {
        return "Student{id='" + id +"', name ='" + name + "', phone ='" + phone + "', email ='" + email + "', address ='" + address + "'}";
    }
}
