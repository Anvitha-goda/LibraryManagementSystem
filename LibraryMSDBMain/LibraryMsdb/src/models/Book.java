package models;

import java.io.Serializable;

public class Book implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String title;
    private String year;
    private int authorId;
    private int copies;
    private int categoryId;

    public Book(int id, int authorId, int categoryId, String title, String year, int copies) {
        this.id = id;
        this.categoryId = categoryId;
        this.title = title;
        this.year = year;
        this.authorId = authorId;
        this.copies = copies;
    }

    // Getters
    public int getId() {
        return id;
    }
    
    public int getCategoryId(){
        return categoryId;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public int getAuthorId() {
        return authorId;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }
    
    // public boolean borrowCopy() {
    //     if (copies > 0) {
    //         copies--;
    //         return true;
    //     }
    //     return false;
    // }

    public void borrowCopy(){ 
            copies--;
    }

    public void returnCopy() {
        copies++;
    }

    @Override
    public String toString() {
        return id + ": " + title + " (" + year + "),Category ID: " + categoryId +", Author ID: " + authorId + ", Copies: " + copies;
    }
}
