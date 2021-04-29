package com.example.aightevents;


import android.net.Uri;

import java.io.Serializable;

public class Event implements Serializable {
    private String eventName;
    private String description;
    private String date;
    private String imageLink;
    private String link;
    private String eventType;
    private String college;
    private String imageName;
    private Integer dateOfCreation;
    private Long eventDeadline;
    private String userID;

    public String getEventName(){
        return eventName;
    }

    public void setEventName(String eventName){
        this.eventName = eventName;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getImageLink(){
        return imageLink;
    }

    public void setImageLink(String imageLink){
        this.imageLink = imageLink;
    }

    public String getLink(){
        return link;
    }

    public void setLink(String link){
        this.link = link;
    }

    public String getEventType(){
        return eventType;
    }

    public void setEventType(String eventType){
        this.eventType = eventType;
    }

    public String getCollege(){
        return college;
    }

    public void setCollege(String college){
        this.college = college;
    }

    public String getImageName(){
        return imageName;
    }

    public void setImageName(String imageName){
        this.imageName = imageName;
    }

    public Integer getDateOfCreation(){
        return dateOfCreation;
    }

    public void setDateOfCreation(Integer dateOfCreation){
        this.dateOfCreation = dateOfCreation;
    }

    public Event(){
        //empty constructor
    }

    public Event(String eventName, String description, String date, String imageLink, String link, String eventType, String college, String imageName, Integer dateOfCreation){
        this.eventName = eventName;
        this.description = description;
        this.date = date;
        this.imageLink = imageLink;
        this.link = link;
        this.eventType = eventType;
        this.college = college;
        this.imageName = imageName;
        this.dateOfCreation = dateOfCreation;
    }

    public Long getEventDeadline() {
        return eventDeadline;
    }

    public void setEventDeadline(Long eventDeadline) {
        this.eventDeadline = eventDeadline;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
