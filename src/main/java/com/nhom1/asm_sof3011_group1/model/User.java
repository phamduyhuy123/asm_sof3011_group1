package com.nhom1.asm_sof3011_group1.model;

import javax.persistence.*;
import lombok.*;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class User {

    @Id
    private String id ;
    private String password;
    private String email;
    private String fullName;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(mappedBy = "user")
    List<Favorite> favorites;
    @OneToMany(mappedBy = "user")
    List<Share> shares;
}
