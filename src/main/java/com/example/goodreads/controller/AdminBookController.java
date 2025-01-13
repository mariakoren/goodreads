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