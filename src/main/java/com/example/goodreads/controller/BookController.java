package com.example.goodreads.controller;

import com.example.goodreads.model.Book;
import com.example.goodreads.service.BookService;
import com.example.goodreads.service.CommentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/user/books")
public class BookController {
    private final BookService bookService;

    @Autowired
    private CommentService commentService;

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

    @GetMapping("/{id}")
    public String getBookById(@PathVariable("id") int id, Model model) {
        Book book = bookService.findBookById(id);
        model.addAttribute("book", book);
        model.addAttribute("comments", book.getComments());
        return "book-details";
    }

    @PostMapping("/addComment")
    public String addComment(@RequestParam("bookId") Long bookId,
                             @RequestParam("content") String content,
                             @RequestParam("rating") int rating,
                             Model model) {

        if (content == null || content.isEmpty()) {
            model.addAttribute("error", "Content of comment cannot be empty");
            return "redirect:/user/books/" + bookId;
        }
        if (content.length() > 50) {
            model.addAttribute("error", "Content of comment must not exceed 50 characters");
            return "redirect:/user/books/" + bookId;
        }
        if (rating < 1 || rating > 5) {
            model.addAttribute("error", "Rating must be between 1 and 5");
            return "redirect:/user/books/" + bookId;
        }
        commentService.addComment(bookId, content, rating);

        return "redirect:/user/books/" + bookId;
    }


}