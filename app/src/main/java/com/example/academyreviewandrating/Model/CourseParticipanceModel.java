package com.example.academyreviewandrating.Model;

/**
 * Created by יעקב on 08/01/2017.
 */

public class CourseParticipanceModel {

    private boolean course_part;
    private String email;
    private String userName;
    private String user_id;

    public CourseParticipanceModel(){

    }

    public CourseParticipanceModel(boolean course_part, String email, String userName, String Uid) {
        this.course_part = course_part;
        this.email = email;
        this.userName = userName;
        this.user_id = Uid;
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
