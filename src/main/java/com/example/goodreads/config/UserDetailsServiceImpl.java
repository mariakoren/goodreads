package com.example.goodreads.config;

import com.example.goodreads.model.UserDtls;
import com.example.goodreads.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepo;

    // Wstrzykiwanie zależności przez konstruktor
    public UserDetailsServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Wyszukiwanie użytkownika po adresie email
        UserDtls user = userRepo.findByEmail(email);
        if (user != null) {
            return new CustomUserDetails(user); // Tworzenie obiektu CustomUserDetails
        }
        // Jeśli użytkownik nie istnieje, rzucamy wyjątek UsernameNotFoundException
        throw new UsernameNotFoundException("User not found");
    }
}
