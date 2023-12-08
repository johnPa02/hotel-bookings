package com.tdtu.backend.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Set;
@Data
public class UserAdminDto {
    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    @NotEmpty
    private String email;

    private Set<String> roles;

}
