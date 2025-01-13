package com.example.goodreads.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class UsersBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserDtls user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Enumerated(EnumType.STRING)
    private Status status = Status.UNREAD; // Domyślny status to WANT_READ

    public enum Status {
        READED,
        WANT_READ,
        UNREAD
    }
}
