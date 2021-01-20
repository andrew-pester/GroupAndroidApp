package com.tc_4.carbon_counter.models;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Tip {
    public enum Category{
        WATER,
        CARBON,
        ENERGY,
        GARBAGE

    }
    public enum Status{
        PENDING,
        APPROVED,
        DENIED,
        EDITING
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //Long id;
  
    @Column(name="id")
    long id;

    @Column(name="title")
    String title;

    @Column(name="working_title")
    String workingTitle;

    @Column(name="category")
    Category category;

    @Column(name="status")
    Status status;

    @Column(name="working_category")
    Category workingCategory;
    
    @Column(name="body")
    String body;

    @Column(name="working_body")
    String workingBody;
    /**
     * Default constructor for Tip
     */
    public Tip(){
       
    }
    public long getId(){
        return id;
    }
    public String getTitle(){
        return title;
    }
    public String getBody(){
        return body;
    }
    public Category getCategory(){
        return category;
    }
    public String getWorkingTitle(){
        return workingTitle;
    }
    public String getWorkingBody(){
        return workingBody;
    }
    public Category getWorkingCategory(){
        return workingCategory;
    }
    public Status getStatus(){
        return status;
    }
    public void setTitle(String newTitle){
        status = Status.EDITING;
        workingTitle = newTitle;
    }
    public void setBody(String newBody){
        status = Status.EDITING;
        workingBody = newBody;
    }
    public void setCategory(Category newCategory){
        status = Status.EDITING;
        workingCategory = newCategory;
    }
    public void setStatus(Status newStatus){
        //iff status == approved then should set the working stuff to be the main variables for the tip
        if(newStatus == Status.APPROVED){
            status = newStatus;
            title = workingTitle;
            body = workingBody;
            category = workingCategory;
        }
        if(newStatus == Status.DENIED){
            status = newStatus;
            workingTitle = title;
            workingBody = body;
            workingCategory = category;
        }
        
        
    }

    
}
