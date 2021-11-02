package com.omniteam.backofisbackend.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="cities")
@Data
@NoArgsConstructor
@AllArgsConstructor()
public class City extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "city_id")
    private Integer cityId;

    @Column(name = "city_name")
    private String cityName;

    @JoinColumn(name="country_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Country country;

    @OneToMany(mappedBy = "city",cascade = CascadeType.ALL)
    private List<CustomerContact> customerContacts;

    @OneToMany(mappedBy = "city",cascade = CascadeType.ALL)
    private List<District> districts;

    @OneToMany(mappedBy = "city",cascade = CascadeType.ALL)
    private List<User> users;
}
