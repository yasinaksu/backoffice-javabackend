package com.omniteam.backofisbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="forms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Form implements Serializable {
    @Id
    @Column(name = "form_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer formId ;

    @Column(name = "form_class")
    private String formClass;

    @Column(name = "form_name")
    private String formName;
}
