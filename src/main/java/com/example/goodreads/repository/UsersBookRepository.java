package com.example.goodreads.repository;

import com.example.goodreads.model.Book;
import com.example.goodreads.model.UsersBook;
import com.example.goodreads.model.UserDtls;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsersBookRepository extends JpaRepository<UsersBook, Long> {
    List<UsersBook> findByUserAndStatus(UserDtls user, UsersBook.Status status);

    Optional<UsersBook> findByUserAndBook(UserDtls user, Book book);
    
    List<UsersBook> findByUser(UserDtls user);
}
