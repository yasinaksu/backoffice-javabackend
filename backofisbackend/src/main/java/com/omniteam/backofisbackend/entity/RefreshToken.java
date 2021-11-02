package com.omniteam.backofisbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name="refresh_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id")
    private Integer refreshTokenId;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Column(nullable = false, unique = true,length = 400)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;
}
