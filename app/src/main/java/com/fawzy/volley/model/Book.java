package com.fawzy.volley.model;

public  class Book {

    private  int id ;
    private String name , author , created_at , update_at ;

    public Book() {
    }

    public Book(String name, String author) {
        this.name = name;
        this.author = author;
    }

    public Book(int id, String name, String author, String created_at, String update_at) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.created_at = created_at;
        this.update_at = update_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(String update_at) {
        this.update_at = update_at;
    }
}
