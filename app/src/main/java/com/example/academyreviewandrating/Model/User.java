package com.example.academyreviewandrating.Model;

import java.io.Serializable;

/**
 * Created by יעקב on 03/12/2016.
 */

public class User implements Serializable {
    private String userName;
    private String Institution;
    private String Faculty;
    private String Email;
    private String Phone;
    private String fullName;
    private String privilage;
    private Boolean lec_in_system;
    private String started_semester;
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
        this.started_semester = "";
        this.lec_in_system = false;
    }
    //copy constractor
    public User (User user) {
        this(user.getUserName(),user.getInstitution(),user.getFaculty(),
                user.getEmail(),user.getPhone(),user.getFullName(),user.isPrivilage());
    }


    public Boolean getLec_in_system() {
        return lec_in_system;
    }

    public void setLec_in_system(Boolean lec_in_system) {
        this.lec_in_system = lec_in_system;
    }

    public String getStarted_semester() {
        return started_semester;
    }

    public void setStarted_semester(String started_semester) {
        this.started_semester = started_semester;
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
