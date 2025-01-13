package com.example.goodreads.service;

import com.example.goodreads.model.Book;
import com.example.goodreads.model.Comment;
import com.example.goodreads.repository.BookRepository;
import com.example.goodreads.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BookRepository bookRepository;

    public List<Comment> findCommentsByBookId(int bookId) {
        return commentRepository.findByBookId(bookId);
    }

    public void saveComment(Comment comment) {
        commentRepository.save(comment);
    }

    public void addComment(Long bookId, String content, int rating) {
        // Pobranie książki
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        // Utworzenie i zapisanie komentarza
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setRating(rating);
        comment.setBook(book);

        commentRepository.save(comment);
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}

