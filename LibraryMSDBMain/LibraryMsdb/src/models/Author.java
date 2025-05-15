package models;

import java.io.Serializable;

// Author 
public class Author implements Serializable {
    private static final long serialVersionUID = 2L;
    private  String name;
    private  String birthdate;
    private  String nationality;

    public Author(String name, String birthdate, String nationality) {
        this.name = name;
        this.birthdate = birthdate;
        this.nationality = nationality;
    }

// Getters

    public String getName() {
        return name;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public String getNationality() {
        return nationality;
    }

    @Override
    public String toString() {
        return  name + " (" + nationality + ")";
    }
}
