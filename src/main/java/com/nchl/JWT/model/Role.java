package com.nchl.JWT.model;

public enum Role {
    USER("USER", "ROLE_USER"),
    ADMIN("ADMIN", "ROLE_ADMIN");

    private final String dbValue;
    private final String authority;

    Role(String dbValue, String authority) {
        this.dbValue = dbValue;
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }

    public String getDbValue() {
        return dbValue;
    }

    public static Role fromString(String value) {
        if (value == null) {
            return null;
        }

        // Remove ROLE_ prefix if present
        if (value.startsWith("ROLE_")) {
            value = value.substring(5);
        }

        // Try to match enum name first
        try {
            return Role.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Fallback to dbValue matching
            for (Role role : values()) {
                if (role.dbValue.equalsIgnoreCase(value)) {
                    return role;
                }
            }
            throw new IllegalArgumentException("No enum constant for value: " + value);
        }
    }
}