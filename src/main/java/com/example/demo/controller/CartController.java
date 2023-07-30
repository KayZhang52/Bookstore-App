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
import java.time.LocalDate;

import com.example.demo.model.Book;
import com.example.demo.model.CartItem;

import com.example.demo.payload.response.MessageResponse;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.security.jwt.JwtUtils;
import com.example.demo.security.service.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/add")
    public ResponseEntity<?> addItemToCart(@RequestParam Long bookId,
            @RequestParam Boolean isPaperback) {

        Book book = bookRepository.findOneById(bookId);
        if (book == null) {
            return ResponseEntity.badRequest().body("Error: item does not exist!");
        } else if (!book.getStatus().equals("available")) {
            return ResponseEntity.badRequest().body("Error: Book is currently not available for sell!");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long idAuthenticated = userDetails.getId();
        if (cartRepository.existsByBookIdAndIsPaperbackAndUserId(bookId, isPaperback, idAuthenticated)) {
            return ResponseEntity.badRequest().body("Error: item already in cart!");
        }

        CartItem item = new CartItem(
                idAuthenticated, bookId, isPaperback);
        StringBuilder msg = new StringBuilder();
        msg.append("Item successfully added to cart!: \n");
        cartRepository.save(item);
        return ResponseEntity.ok(new MessageResponse(msg.toString()));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteItemFromCart(@RequestParam Long cartItemId) {
        if (!cartRepository.existsByIdAndIsDeleted(cartItemId, false)) {
            return ResponseEntity.badRequest().body("Error: item does not exists");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long idAuthenticated = userDetails.getId();

        CartItem item = cartRepository.findOneById(cartItemId);
        if (idAuthenticated != item.getUserId()) {
            return ResponseEntity.badRequest().body("Error: Unauthorized ");
        }
        item.setIsDeleted(true);
        cartRepository.save(item);
        return ResponseEntity.ok(new MessageResponse("Item removed from cart!"));
    }
}