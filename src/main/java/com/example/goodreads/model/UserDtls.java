package com.example.goodreads.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import jakarta.validation.constraints.Size;
import java.util.List;

@Data
@Entity
public class UserDtls {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Full name cannot be empty")
    @Size(max = 50, message = "Full name must not exceed 50 characters")
    private String fullName;

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;


    private String password;

    @NotEmpty(message = "Address cannot be empty")
    @Size(max = 100, message = "Address must not exceed 100 characters")
    private String address;

    private String role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UsersBook> usersBooks;
}
