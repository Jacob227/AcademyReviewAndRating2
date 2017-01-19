package com.example.academyreviewandrating.Model;

/**
 * Created by יעקב on 03/12/2016.
 */

public class User {
    private String userName,Institution,Faculty,Email,Phone,fullName,privilage;
    private boolean image_exist;


    public User(){

    }

    public User(String userName,String Institution,String Faculty,String Email,String Phone,String fullName,String privilage ){
        this.userName = userName;
        this.Institution = Institution;
        this.Faculty = Faculty;
        this.Email = Email;
        this.Phone = Phone;
        this.fullName = fullName;
        this.privilage = privilage;
        this.image_exist = false;
    }
    //copy constractor
    public User (User user) {
        this(user.getUserName(),user.getInstitution(),user.getFaculty(),
                user.getEmail(),user.getPhone(),user.getFullName(),user.isPrivilage());
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

    public boolean getImage_exist() {
        return image_exist;
    }

    public void setImage_exist(boolean image_exist) {
        this.image_exist = image_exist;
    }
    public String isPrivilage() {
        return privilage;
    }

    public void setPrivilage(String privilage) {
        this.privilage = privilage;
    }


}
