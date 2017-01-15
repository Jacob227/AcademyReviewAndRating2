package com.example.academyreviewandrating.Model;

import android.widget.RatingBar;

/**
 * Created by יעקב on 15/01/2017.
 */

public class rating_academy_model {

    private int academy_difficulty, students_char, social_life, faculty_secretary, student_union;
    private String few_words, date, rank_name;
    private Boolean Anonymous;

    public  rating_academy_model() {

    }

    public rating_academy_model(int academy_diff, int students_char, int social_life,
                                int faculty_secretary, int stud_union, String few_words,
                                Boolean anonymous, String date )
    {
        this.academy_difficulty = academy_diff;
        this.students_char = students_char;
        this.social_life = social_life;
        this.faculty_secretary = faculty_secretary;
        this.student_union = stud_union;
        this.few_words = few_words;
        this.rank_name = rank_name;
        this.date  = date;
        rank_name = "";
        Anonymous = anonymous;
    }

    public int getAcademy_difficulty() {
        return academy_difficulty;
    }

    public void setAcademy_difficulty(int academy_diff) {
        this.academy_difficulty = academy_diff;
    }

    public int getStudents_char() {
        return students_char;
    }

    public void setStudents_char(int students_char) {
        this.students_char = students_char;
    }

    public int getSocial_life() {
        return social_life;
    }

    public void setSocial_life(int social_life) {
        this.social_life = social_life;
    }

    public int getFaculty_secretary() {
        return faculty_secretary;
    }

    public void setFaculty_secretary(int faculty_secretary) {
        this.faculty_secretary = faculty_secretary;
    }

    public int getStudent_union() {
        return student_union;
    }

    public void setStudent_union(int stud_union) {
        this.student_union = stud_union;
    }

    public String getFew_words() {
        return few_words;
    }

    public void setFew_words(String few_words) {
        this.few_words = few_words;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRank_name() {
        return rank_name;
    }

    public void setRank_name(String rank_name) {
        this.rank_name = rank_name;
    }

    public Boolean getAnonymous() {
        return Anonymous;
    }

    public void setAnonymous(Boolean anonymous) {
        Anonymous = anonymous;
    }

}
