package com.omniteam.backofisbackend.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity(name = "logs")
@Table(name="logs")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Log implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Integer logId;


    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    @Column(name = "log_data")
    private String logData;

    @Column(name = "created_date")
    private LocalDateTime createdDate;


}
