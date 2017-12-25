package com.example.maqso.classassignmentsapp;

import android.util.Patterns;

/**
 * Created by maqso on 12/9/2017.
 */

public class Validation {

    public static Boolean isEmailOk(String email) {
        Boolean status = true;
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.length() >= 5 ) {
                status = false;
        }

        return status;
    }

    public static Boolean isPasswordOk(String password) {
        Boolean status = true;
        if (password.length() >= 6) {
            status = false;
        }

        return status;
    }

    public static Boolean isValidUserName(String userName) {
        Boolean status = true;
        if (userName.length() >= 5) {
            status = false;
        }

        return status;
    }
}
