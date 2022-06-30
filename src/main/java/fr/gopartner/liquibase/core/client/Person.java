package fr.gopartner.liquibase.core.client;

import lombok.Data;

@Data
public class Person {


    private int id;


    private String firstname;


    private String lastname;


    private int age;

    private University university;
}
