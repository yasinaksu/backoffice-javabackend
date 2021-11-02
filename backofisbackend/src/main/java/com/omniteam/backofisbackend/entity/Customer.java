package com.omniteam.backofisbackend.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends  BaseEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "nation_number")
    private String nationNumber;

    @Column(name = "first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @JsonManagedReference
    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
    private List<CustomerContact> customerContacts;

    @JsonManagedReference
    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
    private List<Order> orders;

}
