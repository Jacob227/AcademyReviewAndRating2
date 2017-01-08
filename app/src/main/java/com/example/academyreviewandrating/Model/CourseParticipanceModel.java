package com.example.academyreviewandrating.Model;

/**
 * Created by יעקב on 08/01/2017.
 */

public class CourseParticipanceModel {

    private boolean course_part;
    private String email, userName;

    public CourseParticipanceModel(){

    }

    public CourseParticipanceModel(boolean course_part, String email, String userName) {
        this.course_part = course_part;
        this.email = email;
        this.userName = userName;
    }

    public boolean getCourse_part() {
        return course_part;
    }

    public void setCourse_part(boolean course_part) {
        this.course_part = course_part;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
