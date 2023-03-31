package com.nhom1.asm_sof3011_group1.model;

import javax.persistence.*;
import lombok.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String username;

    private String email;

    private String password;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "join_date")
    @Temporal(TemporalType.DATE)
    private Date joinDate;

    // getters and setters
}
