package com.spring.bank.enums;

public enum Permission {
    USERS_READ("read"),
    USERS_WRITE("write");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
