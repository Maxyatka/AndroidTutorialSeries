package com.project.firebasetutorial;

public class User {

    private String id;
    private String email;
    private String userName;
    private String status;
    private String profileImage;

    public User(String id, String email, String userName, String status, String profileImage) {
        this.id = id;
        this.email = email;
        this.userName = userName;
        this.status = status;
        this.profileImage = profileImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
