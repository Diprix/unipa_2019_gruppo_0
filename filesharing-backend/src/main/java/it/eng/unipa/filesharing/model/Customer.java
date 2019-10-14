package it.eng.unipa.filesharing.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class Customer {

    /*@Id*/
    @Id
    private UUID uuid;

    private String name;
    private String surname;
    private String vendor;

    private String state;

    public Customer() {
    }

    public Customer(UUID uuid, String name, String surname, String vendor, String state) {
        this.uuid = uuid;
        this.name = name;
        this.surname = surname;
        this.vendor = vendor;
        this.state = state;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "uuid=" + uuid +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", vendor='" + vendor + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
