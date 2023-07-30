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

import com.example.demo.model.Book;
import com.example.demo.model.Favourite;
import com.example.demo.model.ERole;
import com.example.demo.model.Role;
import com.example.demo.model.User;

import com.example.demo.payload.request.LoginRequest;
import com.example.demo.payload.request.SignupRequest;
import com.example.demo.payload.response.JwtResponse;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.FavouriteRepository;
import com.example.demo.security.jwt.JwtUtils;
import com.example.demo.security.service.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/favourite")
public class FavouriteController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FavouriteRepository favouriteRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/add")
    public ResponseEntity<?> addToFavourite(@RequestParam Long bookId) {

        if (!bookRepository.existsById(bookId)) {
            return ResponseEntity.badRequest().body("Error: book is not available!");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long idAuthenticated = userDetails.getId();
        if (favouriteRepository.existsByBookIdAndUserId(bookId, idAuthenticated)) {
            return ResponseEntity.badRequest().body("Error: item already favourited!");
        }

        Favourite favourite = new Favourite(
                idAuthenticated, bookId);
        StringBuilder msg = new StringBuilder();
        msg.append("Item successfully added to favourite!: \n");
        favouriteRepository.save(favourite);
        return ResponseEntity.ok(new MessageResponse(msg.toString()));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteItemFromCart(@RequestParam Long favouriteId) {
        if (!favouriteRepository.existsById(favouriteId)) {
            return ResponseEntity.badRequest().body("Error: item not favourited.");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long idAuthenticated = userDetails.getId();

        Favourite favourite = favouriteRepository.findOneById(favouriteId);
        if (idAuthenticated != favourite.getUserId()) {
            return ResponseEntity.badRequest().body("Error: Unauthorized ");
        }
        favouriteRepository.deleteById(favouriteId);
        return ResponseEntity.ok(new MessageResponse("Item removed from cart!"));
    }
}