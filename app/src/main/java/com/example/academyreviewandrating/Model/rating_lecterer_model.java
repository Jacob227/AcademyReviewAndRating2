package com.example.academyreviewandrating.Model;

import java.io.Serializable;

/**
 * Created by יעקב on 31/12/2016.
 */

public class rating_lecterer_model implements Serializable {

    private int Ability_to_teach,Attitude_lecturer_student, Course_level, Teacher_interesting;
    private String Few_words, Date, rank_name; // Year_Semester;
    private Boolean Anonymous;

    public rating_lecterer_model(){

    }

    public rating_lecterer_model(int ability_to_teach, int attitude_lecturer_student, int course_level, int teacher_interesting, String few_words, String date, Boolean anonymous) { //String semester) {
        Ability_to_teach = ability_to_teach;
        Attitude_lecturer_student = attitude_lecturer_student;
        Course_level = course_level;
        Teacher_interesting = teacher_interesting;
        Few_words = few_words;
        Date = date;
        this.Anonymous = anonymous;
        rank_name = "";
        //Year_Semester = semester;
    }

    /*
    public String getSemester() {
        return Year_Semester;
    }

    public void setSemester(String semester) {
        Year_Semester = semester;
    }
*/

    public Boolean getAnonymous() {
        return Anonymous;
    }

    public void setAnonymous(Boolean anonymous) {
        Anonymous = anonymous;
    }
    public String get_rank_name(){
        return rank_name;
    }

    public void set_rank_name(String rankName){
        rank_name = rankName;
    }

    public int getAbility_to_teach() {
        return Ability_to_teach;
    }

    public void setAbility_to_teach(int ability_to_teach) {
        Ability_to_teach = ability_to_teach;
    }

    public int getAttitude_lecturer_student() {
        return Attitude_lecturer_student;
    }

    public void setAttitude_lecturer_student(int attitude_lecturer_student) {
        Attitude_lecturer_student = attitude_lecturer_student;
    }

    public int getCourse_level() {
        return Course_level;
    }

    public void setCourse_level(int course_level) {
        Course_level = course_level;
    }

    public int getTeacher_interesting() {
        return Teacher_interesting;
    }

    public void setTeacher_interesting(int teacher_interesting) {
        Teacher_interesting = teacher_interesting;
    }

    public String getFew_words() {
        return Few_words;
    }

    public void setFew_words(String few_words) {
        Few_words = few_words;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

}
