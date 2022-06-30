package fr.gopartner.liquibase.domain.car;

import fr.gopartner.liquibase.core.client.Person;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "cars")
@Data
public class Car implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "model")
    private String model;

    @Column(name = "power")
    private int power;

    @Column(name = "personId")
    private int personId;

    @Transient
    private Person person;

    @Override
    public String toString() {
        return
                 id +
                "," + model +
                "," + power +
                "," + personId + "\r\n";
    }
}
