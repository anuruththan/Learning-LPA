package com.example.learning_jpa.enums;

import java.util.EnumSet;

public enum Roles {
    EMPLOYEE,
    VENDOR;

    public static boolean isValidRole(Roles role) {
        return role != null && EnumSet.allOf(Roles.class).contains(role);
    }
}