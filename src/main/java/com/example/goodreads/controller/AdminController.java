package com.example.goodreads.controller;

import com.example.goodreads.model.Book;
import com.example.goodreads.service.BookService;
import com.example.goodreads.service.UserService;
import com.example.goodreads.service.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final BookService bookService;
    private final UserServiceImpl userService;

    public AdminController(BookService bookService, UserServiceImpl userService) {
        this.bookService = bookService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(){
        return "admin-home";
    }

    @GetMapping("/add-book")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "add-book";
    }

    @PostMapping("/add-book")
    public String addBook(@ModelAttribute Book book, Model model) {
        // Walidacja długości pól
        if (book.getTitle().length() < 5 || book.getTitle().length() > 100) {
            model.addAttribute("titleError", "Title must be between 5 and 100 characters.");
        }
        if (book.getAuthor().length() < 5 || book.getAuthor().length() > 100) {
            model.addAttribute("authorError", "Author must be between 5 and 100 characters.");
        }
        if (book.getDescription().length() < 10 || book.getDescription().length() > 500) {
            model.addAttribute("descriptionError", "Description must be between 10 and 500 characters.");
        }

        if (model.containsAttribute("titleError") || model.containsAttribute("authorError") || model.containsAttribute("descriptionError")) {
            return "add-book";
        }
        bookService.saveBook(book);
        return "redirect:/admin/";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
//        model.addAttribute("users", userService.findAllUsers());
        model.addAttribute("users", userService.findUsersByRole("ROLE_USER"));
        return "users";

    }

    @PostMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable Integer id) {
        userService.deleteUserById(id);
        return "redirect:/admin/users";
    }

//    @GetMapping("/books")
//    public String listAllBooks(Model model) {
//        List<Book> books = bookService.getAllBooks();
//        model.addAttribute("books", books);
//        return "book-list-admin";
//    }

    @PostMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable Long id, Model model) {
        try {
            bookService.deleteBookById(id);
            model.addAttribute("message", "Book with ID " + id + " and its comments have been deleted.");
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/admin/books";
    }



}
