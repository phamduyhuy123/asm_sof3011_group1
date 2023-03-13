package com.nhom1.asm_sof3011_group1.model;

import com.nhom1.asm_sof3011_group1.security.Role;

import java.util.UUID;

public class User {
    private UUID id;
    private String password;
    private String email;
    private String fullName;
    private Role role;
}
