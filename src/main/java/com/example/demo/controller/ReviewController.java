package com.example.demo.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Book;
import com.example.demo.model.CartItem;
import com.example.demo.model.ERole;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.model.Review;

import com.example.demo.payload.request.LoginRequest;
import com.example.demo.payload.request.SignupRequest;
import com.example.demo.payload.response.JwtResponse;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.security.jwt.JwtUtils;
import com.example.demo.security.service.UserDetailsImpl;

// endpoints here should be mainly used by retailers.
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/review")
public class ReviewController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/add")
    public ResponseEntity<?> addReview(@RequestParam Long bookId,
            @RequestParam String reviewContent, @RequestParam Integer upvotes, @RequestParam Integer rating) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long idAuthenticated = userDetails.getId();

        if (!orderRepository.existsByUserIdAndBookId(idAuthenticated, bookId)) {
            return ResponseEntity.badRequest().body("Error: unauthorized.");
        }
        Review review = new Review(
                idAuthenticated, rating, upvotes);
        reviewRepository.save(review);

        StringBuilder msg = new StringBuilder();
        msg.append("Review added.");
        return ResponseEntity.ok(new MessageResponse(msg.toString()));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteBook(@RequestParam Long reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            return ResponseEntity.badRequest().body("Error: review does not exist.");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long idAuthenticated = userDetails.getId();

        Review review = reviewRepository.findOneById(reviewId);
        if (idAuthenticated != review.getUserId()) {
            return ResponseEntity.badRequest().body("Error: Unauthorized ");
        }
        reviewRepository.deleteById(reviewId);
        return ResponseEntity.ok(new MessageResponse("review is deleted!"));
    }

    @PostMapping("/upvote")
    public ResponseEntity<?> updateBook(@RequestParam Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            return ResponseEntity.badRequest().body("Error: book does not exist.");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long idAuthenticated = userDetails.getId();

        Review review = reviewRepository.findOneById(bookId);
        review.setUpvotes(review.getUpvotes() + 1);

        return ResponseEntity.ok(new MessageResponse("upvote successful."));
    }
}