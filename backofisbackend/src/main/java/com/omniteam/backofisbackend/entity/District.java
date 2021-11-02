package com.omniteam.backofisbackend.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="districts")
@Data
@NoArgsConstructor
@AllArgsConstructor()
public class District  extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "district_id")
    private Integer districtId;

    @Column(name = "district_name")
    private String districtName;


    @JoinColumn(name="country_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Country country;


    @JoinColumn(name="city_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private City city;

    @OneToMany(mappedBy = "district",cascade = CascadeType.ALL)
    private List<CustomerContact> customerContacts;

    @OneToMany(mappedBy = "district",cascade = CascadeType.ALL)
    private List<User> users;


}
