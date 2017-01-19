package com.example.academyreviewandrating.Model;

import java.util.ArrayList;

/**
 * Created by יעקב on 19/01/2017.
 */

public class LecturerUser extends User {

    private ArrayList<CourseDetailsModel> mCourses = new ArrayList<CourseDetailsModel>();;

    public LecturerUser() {

    }

    public LecturerUser(ArrayList<CourseDetailsModel> mCourses) {
        mCourses = new ArrayList<CourseDetailsModel>();
        this.mCourses = mCourses;
    }

    public LecturerUser(String userName, String Institution, String Faculty, String Email, String Phone, String fullName, String privilage) {
        super(userName, Institution, Faculty, Email, Phone, fullName, privilage);
    }

    public ArrayList<CourseDetailsModel> getmCourses() {
        return mCourses;
    }

    public void setmCourses(ArrayList<CourseDetailsModel> mCourses) {
        this.mCourses = mCourses;
    }

}
