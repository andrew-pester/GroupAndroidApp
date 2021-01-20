package com.tc_4.carbon_counter.models;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    long id;

    @Column(name="title")
    String title;

    @Column(name="image_title")
    String imageTitle;

    @Column(name="link")
    String link;
    
    @Column(name="body")
    String body;

    @Column(name="date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")    
    LocalDate date;

    public long getId(){
        return id;
    }
    public String getTitle(){
        return title;
    }
    public String getImageTitle(){
        return imageTitle;
    }
    public String getLink(){
        return link;
    }
    public String getBody(){
        return body;
    }
    public LocalDate getDate(){
        return date;
    }

    public void setTitle(String newTitle){
        title = newTitle;
    }
    public void setImageTitle(String imageTitle){
        this.imageTitle = imageTitle;
    }
    public void setLink(String link){
        this.link = link;
    }
    public void setBody(String newBody){
        body = newBody;
    }    
    public void setDate(LocalDate date){
        this.date = date;
    }
}
