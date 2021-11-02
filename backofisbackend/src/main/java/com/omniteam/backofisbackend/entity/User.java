package com.omniteam.backofisbackend.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId ;

    @Column(name = "first_name")
    private String firstName;


    @Column(name="last_name")
    private String lastName;

    @Column(name = "title")
    private String title;


    @Column(name="email")
    private String email;

    @Column(name = "password")
    private String password;


    @Column(name="phone_number")
    private String phoneNumber;

    @Column(name="address")
    private String address;


    @JoinColumn(name = "country_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Country country;///@OneToOneda yapılabilir***

    @JoinColumn(name = "city_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private City city;///@OneToOneda yapılabilir***


    @JoinColumn(name = "district_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private District district;///@OneToOneda yapılabilir***


    @OneToMany(mappedBy = "user")
    private List<Log> logs;


    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
    private List<UserRole> userRoles;

    public User(Integer userId) {
        this.userId = userId;
    }
}


