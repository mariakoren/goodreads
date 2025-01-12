package com.example.goodreads.repository;

import com.example.goodreads.model.UserDtls;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserDtls, Integer> {

    public boolean existsByEmail(String email);

    public UserDtls findByEmail(String email);

    public void deleteById(Integer id);

    List<UserDtls> findByRole(String role);


}
