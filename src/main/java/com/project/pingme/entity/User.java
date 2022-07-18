package com.project.pingme.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "salt")
    private String salt;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @OneToMany(mappedBy = "host")
    private List<UserContact> hostInstances;

    @OneToMany(mappedBy = "contact")
    private List<UserContact> contactInstances;

    public User() {
    }

    public User(String username,
                String salt,
                String password,
                String firstName,
                String lastName,
                List<UserContact> hostInstances,
                List<UserContact> contactInstances) {
        this.username = username;
        this.salt = salt;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.hostInstances = hostInstances;
        this.contactInstances = contactInstances;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<UserContact> getHostInstances() {
        return hostInstances;
    }

    public void setHostInstances(List<UserContact> hostInstances) {
        this.hostInstances = hostInstances;
    }

    public List<UserContact> getContactInstances() {
        return contactInstances;
    }

    public void setContactInstances(List<UserContact> contactInstances) {
        this.contactInstances = contactInstances;
    }
}
