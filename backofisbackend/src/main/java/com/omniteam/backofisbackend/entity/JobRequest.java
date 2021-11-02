package com.omniteam.backofisbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobRequest extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name  ="jobRequest_id")
    private Integer jobRequestId;

    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;

    private String filePath;



}
