package com.example.goodreads.controller;

import com.example.goodreads.model.Book;
import com.example.goodreads.service.BookService;
import com.example.goodreads.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/books")
public class AdminBookController {
    private BookService bookService;

    @Autowired
    private CommentService commentService;

    public AdminBookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping()
    public String listAllBooks(Model model) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "book-list-admin";
    }

    @GetMapping("/{id}")
    public String getBookById(@PathVariable("id") int id, Model model) {
        Book book = bookService.findBookById(id);
        model.addAttribute("book", book);
        model.addAttribute("comments", book.getComments());
        return "book-details-admin";
    }

    @GetMapping("/edit/{id}")
    public String editBookForm(@PathVariable("id") int id, Model model) {
        Book book = bookService.findBookById(id);
        model.addAttribute("book", book);
        return "book-edit-form";
    }

    @PostMapping("/edit/{id}")
    public String saveEditedBook(@PathVariable("id") int id, @ModelAttribute("book") Book book, Model model) {
        if (book.getTitle().length() < 5 || book.getTitle().length() > 100) {
            model.addAttribute("titleError", "Title must be between 5 and 100 characters.");
        }
        if (book.getAuthor().length() < 5 || book.getAuthor().length() > 100) {
            model.addAttribute("authorError", "Author must be between 5 and 100 characters.");
        }
        if (book.getDescription() != null && (book.getDescription().length() < 10 || book.getDescription().length() > 500)) {
            model.addAttribute("descriptionError", "Description must be between 10 and 500 characters.");
        }

        if (model.containsAttribute("titleError") || model.containsAttribute("authorError") || model.containsAttribute("descriptionError")) {
            return "book-edit-form";
        }
        bookService.updateBook(id, book);
        return "redirect:/admin/books";
    }

    @PostMapping("/deleteComment")
    public String deleteComment(@RequestParam("commentId") Long commentId,
                                @RequestParam("bookId") Long bookId,
                                Model model) {
        try {
            commentService.deleteComment(commentId);
        } catch (Exception e) {
            model.addAttribute("error", "Error deleting the comment.");
            return "redirect:/admin/books/" + bookId;
        }

        return "redirect:/admin/books/" + bookId;
    }



}