package com.example.plug_android;

import org.json.JSONStringer;

public class User {
    private String displayName;
    private String email;
    private String phone;
    private String photoURL;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class);
    }

    public User(String displayName, String email, String phone, String photoURL, String uid) {
        this.displayName = displayName;
        this.email = email;
        this.phone = phone;
        this.photoURL = photoURL;
    }

    public String ToJSON() {
        String jsonString = "";
        jsonString = jsonString + "{ 'displayName' : " + displayName + ", 'email' : " + email + ", 'phone' : " + phone + ", 'photoURL' : " + photoURL + " }";

        return jsonString;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }
}
