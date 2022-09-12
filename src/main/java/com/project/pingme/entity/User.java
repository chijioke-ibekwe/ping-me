package com.project.pingme.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@SuperBuilder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
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
}
