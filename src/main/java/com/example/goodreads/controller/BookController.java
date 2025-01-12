package com.example.goodreads.controller;

import com.example.goodreads.model.Book;
import com.example.goodreads.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/")
    public String listAllBooks(Model model) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "book-list";
    }

    @GetMapping("/search")
    public String searchBooksByTitle(@RequestParam("title") String title, Model model) {
        List<Book> books = bookService.findBooksByTitle(title);
        model.addAttribute("books", books);
        return "book-list";
    }
}
