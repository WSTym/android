package com.pdm.sagaz;

public class User {
    private String uuid, username, profileUri;

    public User() {
    }

    public User(String uuid, String username, String profileUri) {
        this.uuid = uuid;
        this.username = username;
        this.profileUri = profileUri;
    }

    public String getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public String getProfileUri() {
        return profileUri;
    }
}
