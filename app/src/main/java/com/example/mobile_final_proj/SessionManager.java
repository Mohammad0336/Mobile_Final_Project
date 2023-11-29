package com.example.mobile_final_proj;

public class SessionManager {
    private static SessionManager instance;
    private String currentUserEmail;
    private String currentUserPassword;


    private SessionManager() {
        // private constructor to enforce singleton pattern
    }

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setCurrentUserEmail(String email) {
        this.currentUserEmail = email;
    }

    public String getCurrentUserEmail() {
        return currentUserEmail;
    }


    public void setCurrentUserPassword(String password) {
        this.currentUserEmail = password;
    }

    public String getCurrentUserPassword() {
        return currentUserPassword;
    }
}
