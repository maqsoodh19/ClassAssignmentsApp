package com.example.maqso.classassignmentsapp.PojoClasses;

/**
 * Created by maqso on 12/18/2017.
 */

public class GroupInfo {
    int id;
    String name;
    String access;

    public GroupInfo(int id, String name, String access) {
        this.id = id;
        this.name = name;
        this.access = access;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAccess() {
        return access;
    }
}
