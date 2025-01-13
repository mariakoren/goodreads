package com.example.goodreads.controller;

import com.example.goodreads.model.Book;
import com.example.goodreads.model.UsersBook;
import com.example.goodreads.model.UserDtls;
import com.example.goodreads.repository.BookRepository;
import com.example.goodreads.repository.UsersBookRepository;
import com.example.goodreads.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/usersBooks")
public class UsersBookController {

    @Autowired
    private UsersBookRepository usersBookRepository;

    @Autowired
    private UserRepository userDtlsRepository;

    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/list")
    public String getUsersBooks(@RequestParam int userId,
                                @RequestParam(required = false, defaultValue = "ALL") String statusFilter,
                                Model model) {
        UserDtls user = userDtlsRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Book> allBooks = bookRepository.findAll(); // Wszystkie książki
        List<UsersBook> usersBooks = usersBookRepository.findByUser(user); // Wszystkie powiązane UsersBook

        // Mapa książek i ich statusów
        Map<Long, String> bookStatuses = new HashMap<>();
        for (Book book : allBooks) {
            UsersBook userBook = usersBooks.stream()
                    .filter(ub -> ub.getBook().getId().equals(book.getId()))
                    .findFirst()
                    .orElse(null);

            if (userBook != null) {
                bookStatuses.put(book.getId(), userBook.getStatus().toString());
            } else {
                bookStatuses.put(book.getId(), "UNREAD"); // Domyślny status
            }
        }

        model.addAttribute("allBooks", allBooks); // Wszystkie książki
        model.addAttribute("bookStatuses", bookStatuses); // Statusy książek
        model.addAttribute("userId", userId);
        model.addAttribute("statusFilter", statusFilter);
        return "usersBooks-list";
    }

    @PostMapping("/updateStatus")
    public String updateBookStatus(@RequestParam int userId,
                                   @RequestParam Long bookId,
                                   @RequestParam String newStatus) {
        UserDtls user = userDtlsRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        Optional<UsersBook> usersBookOptional = usersBookRepository.findByUserAndBook(user, book);
        UsersBook usersBook = usersBookOptional.orElseGet(() -> {
            UsersBook newUsersBook = new UsersBook();
            newUsersBook.setUser(user);
            newUsersBook.setBook(book);
            return newUsersBook;
        });

        usersBook.setStatus(UsersBook.Status.valueOf(newStatus.toUpperCase()));
        usersBookRepository.save(usersBook);

        return "redirect:/usersBooks/list?userId=" + userId;
    }
}
