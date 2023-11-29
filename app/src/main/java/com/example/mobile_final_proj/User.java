package com.example.mobile_final_proj;

public class User {
    private String email;
    private String password; // Placeholder, avoid storing passwords in plain text

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }


}
