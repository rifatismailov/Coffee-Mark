package com.example.coffeemark.user;

import android.util.Log;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONObject;

@Entity(tableName = "users")
public class User {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String username;
    private String password;
    private String email;
    private String role;
    private String myImage;


    // Конструктори, геттери і сеттери
    public User(String username, String password, String email, String role, String myImage) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.myImage = myImage;
    }

    // Геттери для всіх полів
    public int getId() {
        return id;  // Буде генеруватися після вставки в базу
    }

    public void setId(int id) {
        this.id = id;
    }

    // Геттери та сеттери
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMyImage() {
        return myImage;
    }

    public void setMyImage(String myImage) {
        this.myImage = myImage;
    }


    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("password", password);
            jsonObject.put("email", email);
            jsonObject.put("role", role);
            jsonObject.put("myImage", myImage);

        } catch (Exception e) {
            Log.e("User", e.toString());
        }
        return jsonObject;
    }
}

