package com.example.qred.casestudy.casestudy.models;



import lombok.Data;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class ApplicationUser {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    private String username;

    @NonNull
    private String password;

    @NonNull
    private String role;

    public ApplicationUser() {
    }
    public ApplicationUser(Long id,String username, String encodedPassword,String role) {
        this.id=id;
        this.username = username;
        this.password = encodedPassword;
        this.role=role;
    }
}