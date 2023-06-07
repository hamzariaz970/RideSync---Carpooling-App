package com.example.ridesync.Classes;

public abstract class Person {
    private String name;

    public Person(){
        name = "NULL";
    }

    public Person(String name){
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
