package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestParam;
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
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;

import com.example.demo.model.Book;
import com.example.demo.model.CartItem;
import com.example.demo.model.ERole;
import com.example.demo.model.Order;
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
@RequestMapping("/api/order")
public class OrderController {
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

    @PostMapping("/create")
    public ResponseEntity<?> addOrder(@RequestParam Long bookId,
            @RequestParam Integer quantity, @RequestParam Boolean isPaperback) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long idAuthenticated = userDetails.getId();
        Book book;
        try {
            book = bookRepository.findById(bookId).get();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: Bookid does not exist!");
        }

        if (book.getStatus().equals("unavailable")) {
            return ResponseEntity.badRequest().body("Error: Book is currently unavailable.");
        }
        if (isPaperback && !book.getHasPaperback()) {
            return ResponseEntity.badRequest().body("Error: Book currently have no physical copies available.");
        }
        Order order = new Order(bookId, idAuthenticated, isPaperback, "pending payment", LocalDate.now(),
                LocalTime.now());
        orderRepository.save(order);

        return ResponseEntity.ok(new MessageResponse("Order is confirmed."));
    }

    @PostMapping("/updateStatus")
    public ResponseEntity<?> updateOrderStatus(@RequestParam Long orderId, @RequestParam String status) {
        if (!orderRepository.existsById(orderId)) {
            return ResponseEntity.badRequest().body("Error: order does not exist.");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long idAuthenticated = userDetails.getId();

        Order order = orderRepository.findOneById(orderId);
        if (idAuthenticated != order.getUserId()) {
            return ResponseEntity.badRequest().body("Error: Unauthorized ");
        }
        order.setStatus(status);
        orderRepository.save(order);
        return ResponseEntity.ok(new MessageResponse("order status updated"));
    }

}
