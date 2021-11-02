package com.omniteam.backofisbackend.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer roleId ;

    @Column(name = "role_name")
    private String roleName ;

    @OneToMany(mappedBy = "role",fetch = FetchType.LAZY)
    private List<UserRole> userRoles;


    public Role(String roleName)
    {
        this.roleName=roleName;
    }

    public Role(Integer roleId) {
        this.roleId = roleId;
    }
}
