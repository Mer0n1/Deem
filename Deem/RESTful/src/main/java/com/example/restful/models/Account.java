package com.example.restful.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {

    private Long id;
    private String username;
    private String password;
    private String name;
    private String surname;
    private String fathername;
    private Integer score;
    private Long group_id;
    private Long id_club;
    private Group group;

    private Image imageIcon;

    @JsonIgnore
    private Boolean isThereImageIcon;

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Integer getScore() {
        return score;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getFathername() {
        return fathername;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setFathername(String fathername) {
        this.fathername = fathername;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(Long group_id) {
        this.group_id = group_id;
    }

    public Image getImageIcon() {
        return imageIcon;
    }

    public void setImageIcon(Image imageIcon) {
        this.imageIcon = imageIcon;
    }

    public Long getId_club() {
        return id_club;
    }

    public void setId_club(Long id_club) {
        this.id_club = id_club;
    }

    public Boolean isThereImageIcon() {
        return isThereImageIcon;
    }

    public void setThereImageIcon(Boolean thereImageIcon) {
        isThereImageIcon = thereImageIcon;
    }
}
