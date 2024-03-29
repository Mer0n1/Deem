package com.example.administrative_service.models;

import jakarta.persistence.Column;
import jakarta.persistence.Transient;
import lombok.Data;

@Data
public class Account {
    private Long id;
    private String username;
    private String password;

    private String name;
    private String surname;
    private String fathername;
    private String role;
    private int score;
    /*private Group group;*/
    private Long group_id;
}
