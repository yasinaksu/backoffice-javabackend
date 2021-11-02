package com.omniteam.backofisbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "form_controls")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormControl implements Serializable {
    @Id
    @Column(name = "form_control_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer formControlId;

    @Column(name = "control_type")
    private String controlType;

    @Column(name = "control_name")
    private String controlName;

    @Column(name = "control_values")
    private String controlValues;

    @Column(name = "control_validators")
    private String controlValidators;

    @Column(name = "place_holder")
    private String placeHolder;

    @Column(name = "hint")
    private String hint;

    @Column(name = "label")
    private String label;

    @Column(name = "width")
    private String width;

    @Column(name = "sorter")
    private String sorter;

    @JoinColumn(name="form_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Form form;

}
