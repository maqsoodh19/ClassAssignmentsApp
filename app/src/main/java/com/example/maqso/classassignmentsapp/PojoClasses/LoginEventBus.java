package com.example.maqso.classassignmentsapp.PojoClasses;

import com.example.maqso.classassignmentsapp.Models.LoginUser;

/**
 * Created by maqso on 12/9/2017.
 */

public class LoginEventBus {
    LoginUser loginUser;

    public LoginEventBus(LoginUser loginUser) {
        this.loginUser = loginUser;
    }
}
