package com.example.academyreviewandrating.Model;

/**
 * Created by יעקב on 03/12/2016.
 */

public class User {
    private String userName,Institution,Faculty,Email,Phone,fullName,privilage;

    public User(){

    }

    public User(String userName,String Institution,String Faculty,String Email,String Phone,String fullName,String privilage){
        this.userName = userName;
        this.Institution = Institution;
        this.Faculty = Faculty;
        this.Email = Email;
        this.Phone = Phone;
        this.fullName = fullName;
        this.privilage = privilage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getInstitution() {
        return Institution;
    }

    public void setInstitution(String institution) {
        Institution = institution;
    }

    public String getFaculty() {
        return Faculty;
    }

    public void setFaculty(String faculty) {
        Faculty = faculty;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    public String isPrivilage() {
        return privilage;
    }

    public void setPrivilage(String privilage) {
        this.privilage = privilage;
    }

}
