package fr.gopartner.liquibase.core.client;

import lombok.Data;

import java.util.List;

@Data
public class University {

    private int id;

    private String name;

    private String address;

    private List<Person> persons;
}
