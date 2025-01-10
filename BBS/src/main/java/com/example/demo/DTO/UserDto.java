package com.example.demo.DTO;

import com.example.demo.Domain.User;
import com.example.demo.Domain.UserRole;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Getter
@Data
@NoArgsConstructor
public class UserDto {
    private String username;
    private String name;
    private String password;
    private String email;
    private UserRole userRole = UserRole.valueOf("USER");

    @Builder
    public UserDto(String username, String name, String password, String email, UserRole userRole) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.email = email;
        this.userRole = userRole;
    }

    public UserDto(Optional<User> findUser) {
        this.username = findUser.get().getUsername();
        this.name = findUser.get().getName();
        this.email = findUser.get().getEmail();
        this.userRole = findUser.get().getUserRole();
    }
}