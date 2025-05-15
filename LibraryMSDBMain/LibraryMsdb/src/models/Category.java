package models;

import java.io.Serializable;

public class Category implements Serializable {
    private static final long serialVersionUID = 5L;
    private int id;
    private String branch;
    private String description;

    public Category(int id,String branch, String description) {
        this.id = id;
        this.branch = branch;
        this.description = description;
    }

    public int getId(){
        return id;
    }

    public String getBranch() {
        return branch;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return branch + " - " + description;
    }
}
