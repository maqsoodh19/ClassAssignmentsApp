package com.example.maqso.classassignmentsapp.PojoClasses;

import com.example.maqso.classassignmentsapp.Models.Assignments;

/**
 * Created by maqso on 12/18/2017.
 */

public class MyEvents {

    // communication between assignment recycyler to assignment details view fragment
    public static class AssignmentInfo {
        Assignments assignments;

        public AssignmentInfo(Assignments assignment) {
            this.assignments = assignment;
        }

        public Assignments getAssignments() {
            return assignments;
        }
    }

    public static class DeviceToken{
        String dToken;

        public DeviceToken(String dToken) {
            this.dToken = dToken;
        }

        public String getdToken() {
            return dToken;
        }
    }

    public static class GroupAccessEvent {
        Boolean status;
        int id;
        String name;

        public GroupAccessEvent(Boolean status, int id, String name) {
            this.status = status;
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Boolean getStatus() {
            return status;
        }
    }
}
