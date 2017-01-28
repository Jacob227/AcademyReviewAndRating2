package com.example.academyreviewandrating.Model;

import java.util.ArrayList;

/**
 * Created by יעקב on 19/01/2017.
 */

/**
 * Lecturer class model
 * Information on lecturer
 */
public class LecturerUser extends User {

    public LecturerUser() {

    }

    /**
     * Constructor
     * @param mCourses
     */
    public LecturerUser(ArrayList<CourseDetailsModel> mCourses) {
        mCourses = new ArrayList<CourseDetailsModel>();
    }

    public LecturerUser(String userName, String Institution, String Faculty, String Email, String Phone, String fullName, String privilage) {
        super(userName, Institution, Faculty, Email, Phone, fullName, privilage);
    }


}
