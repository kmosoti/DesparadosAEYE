package com.example.desparadosaeye.data;

public class loginModel {
    private String name;
    private static String email;
    private static String password;
    private int id;
    public boolean emailConfirmed;
    //Constructors

    public loginModel(String email, String password) {
        this.email = email;
        this.password = password;
    }

    //toString function
    @Override
    public String toString() {
        return "loginModel{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public static String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
