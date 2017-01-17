package com.example.academyreviewandrating.Model;

import java.io.Serializable;

/**
 * Created by יעקב on 06/01/2017.
 */

public class CourseDetailsModel implements Serializable {

    private String room;
    private String syllabus;
    private String time;
    private String course_name;
    private float credits;
    private float code_course;

    public CourseDetailsModel(){

    }

    public CourseDetailsModel(String room, String syllabus, String time, float credits, float code_course) {
        this.room = room;
        this.syllabus = syllabus;
        this.time = time;
        this.credits = credits;
        this.code_course = code_course;
        this.course_name = "";
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getSyllabus() {
        return syllabus;
    }

    public void setSyllabus(String syllabus) {
        this.syllabus = syllabus;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public float getCode_course() {
        return code_course;
    }

    public void setCode_course(float code_course) {
        this.code_course = code_course;
    }

    public float getCredits() {
        return credits;
    }

    public void setCredits(float credits) {
        this.credits = credits;
    }

}
