package com.omniteam.backofisbackend.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="countries")
@Data
@NoArgsConstructor
@AllArgsConstructor()
public class Country  extends  BaseEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_id")
    private Integer countryId;

    @Column(name = "country_name")
    private String countryName;

    @Column(name = "nation_code")
    private String nationCode;

    @OneToMany(mappedBy = "country",cascade = CascadeType.ALL)
    private List<City> cities;

    @OneToMany(mappedBy = "country",cascade = CascadeType.ALL)
    private List<District> districts;

    @OneToMany(mappedBy = "country",cascade = CascadeType.ALL)
    private List<CustomerContact> customerContacts;


    @OneToMany(mappedBy = "country",cascade = CascadeType.ALL)
    private List<User> users;

}
