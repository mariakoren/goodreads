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
import java.util.stream.Collectors;

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

        List<Book> allBooks = bookRepository.findAll();
        List<UsersBook> usersBooks = usersBookRepository.findByUser(user);

        Map<Long, String> bookStatuses = new HashMap<>();
        for (Book book : allBooks) {
            UsersBook userBook = usersBooks.stream()
                    .filter(ub -> ub.getBook().getId().equals(book.getId()))
                    .findFirst()
                    .orElse(null);

            if (userBook != null) {
                bookStatuses.put(book.getId(), userBook.getStatus().toString());
            } else {
                bookStatuses.put(book.getId(), "UNREAD");
            }
        }


        if (!statusFilter.equals("ALL")) {
            allBooks = allBooks.stream()
                    .filter(book -> bookStatuses.get(book.getId()).equals(statusFilter))
                    .collect(Collectors.toList());
        }

        model.addAttribute("allBooks", allBooks);
        model.addAttribute("bookStatuses", bookStatuses);
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

    @GetMapping("/statusCounts")
    public String getBooksStatusCounts(Model model) {
        List<Book> allBooks = bookRepository.findAll();
        Map<String, Map<String, Integer>> booksStatusCounts = new HashMap<>();

        for (Book book : allBooks) {
            int readedCount = usersBookRepository.countByBookIdAndStatus(book.getId(), UsersBook.Status.READED);
            int wantReadCount = usersBookRepository.countByBookIdAndStatus(book.getId(), UsersBook.Status.WANT_READ);

            Map<String, Integer> statusCounts = new HashMap<>();
            statusCounts.put("READED", readedCount);
            statusCounts.put("WANT_READ", wantReadCount);

            booksStatusCounts.put(book.getTitle(), statusCounts);
        }
        model.addAttribute("booksStatusCounts", booksStatusCounts);
        return "book-status-counts";
    }

    @GetMapping("/userStatusCounts")
    public String getUserStatusCounts(Model model) {
        List<UserDtls> allUsers = userDtlsRepository.findAll();
        List<Book> allBooks = bookRepository.findAll();
        int totalBooksInDatabase = allBooks.size();

        int totalReadedCount = usersBookRepository.countByStatus(UsersBook.Status.READED);
        int totalWantReadCount = usersBookRepository.countByStatus(UsersBook.Status.WANT_READ);

        double totalReadedPercentage = totalBooksInDatabase > 0 ? (totalReadedCount / (double) totalBooksInDatabase) * 100 : 0;
        double totalWantReadPercentage = totalBooksInDatabase > 0 ? (totalWantReadCount / (double) totalBooksInDatabase) * 100 : 0;

        Map<String, Map<String, Object>> userStatusCounts = new HashMap<>();

        for (UserDtls user : allUsers) {
            int readedCount = usersBookRepository.countByUserIdAndStatus(user.getId(), UsersBook.Status.READED);
            int wantReadCount = usersBookRepository.countByUserIdAndStatus(user.getId(), UsersBook.Status.WANT_READ);

            double readedPercentage = totalBooksInDatabase > 0 ? (readedCount / (double) totalBooksInDatabase) * 100 : 0;
            double wantReadPercentage = totalBooksInDatabase > 0 ? (wantReadCount / (double) totalBooksInDatabase) * 100 : 0;

            Map<String, Object> statusCounts = new HashMap<>();
            statusCounts.put("READED_COUNT", readedCount);
            statusCounts.put("WANT_READ_COUNT", wantReadCount);
            statusCounts.put("READED_PERCENTAGE", readedPercentage);
            statusCounts.put("WANT_READ_PERCENTAGE", wantReadPercentage);

            userStatusCounts.put(user.getEmail(), statusCounts);
        }

        model.addAttribute("totalReadedCount", totalReadedCount);
        model.addAttribute("totalWantReadCount", totalWantReadCount);
        model.addAttribute("totalReadedPercentage", totalReadedPercentage);
        model.addAttribute("totalWantReadPercentage", totalWantReadPercentage);

        model.addAttribute("userStatusCounts", userStatusCounts);
        return "user-status-counts";
    }
}
