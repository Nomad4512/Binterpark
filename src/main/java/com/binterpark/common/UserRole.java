package com.binterpark.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {

    ADMIN("ADMIN"),
    STAFF("STAFF"),
    USER("USER");

    private final String roleName;

    public static UserRole fromRoleName(String roleName) {
        for (UserRole role : UserRole.values()) {
            if (role.roleName.equals(roleName)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + roleName);
    }
}
