package com.example.demo.controller;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.servlet.ServletContext;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import com.example.demo.model.Book;
import com.example.demo.model.CartItem;
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
import com.example.demo.repository.CartRepository;
import com.example.demo.security.jwt.JwtUtils;
import com.example.demo.security.service.UserDetailsImpl;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/book")
public class BookController {
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

    @Autowired
    ServletContext context;

    @Value("${images.coverDirectory}")
    private String imgDirectory;

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @PostMapping("/add")
    @PreAuthorize("hasRole('CREATOR')")
    public ResponseEntity<?> addBookToCreator(@RequestParam String title,
            @RequestParam Boolean hasPaperback, @RequestParam String status) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long idAuthenticated = userDetails.getId();

        Book book = new Book(
                title, idAuthenticated, hasPaperback, status);
        bookRepository.save(book);

        StringBuilder msg = new StringBuilder();
        msg.append("Book is now available for buyers!: \n");
        return ResponseEntity.ok(new MessageResponse(msg.toString()));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteBook(@RequestParam Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            return ResponseEntity.badRequest().body("Error: book does not exist.");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long idAuthenticated = userDetails.getId();

        Book book = bookRepository.findOneById(bookId);
        if (idAuthenticated != book.getUserId()) {
            return ResponseEntity.badRequest().body("Error: Unauthorized ");
        }
        book.setStatus("removed");
        bookRepository.save(book);
        return ResponseEntity.ok(new MessageResponse("Book is removed!"));
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateBook(@RequestParam Long bookId, @RequestParam String title,
            @RequestParam Long autherUid,
            @RequestParam Boolean hasPaperback, @RequestParam String status) {
        if (!bookRepository.existsById(bookId)) {
            return ResponseEntity.badRequest().body("Error: book does not exist.");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long idAuthenticated = userDetails.getId();

        Book book = bookRepository.findOneById(bookId);
        if (book == null) {
            return ResponseEntity.badRequest().body("Error: Item not found.");
        }
        if (idAuthenticated != book.getUserId()) {
            return ResponseEntity.badRequest().body("Error: Unauthorized.");
        }
        book.setStatus(status);
        book.setTitle(title);
        book.setUserId(idAuthenticated);
        book.setHasPaperback(hasPaperback);
        bookRepository.save(book);
        return ResponseEntity.ok(new MessageResponse("Book details are updated."));
    }

    @GetMapping("/getAll")
    public List<Book> getAllBook() {
        List<Book> books = bookRepository.findAll();
        return books;
    }

    @GetMapping("/image")
    public ResponseEntity<FileSystemResource> getImage(Long bookId) {
        Book book = bookRepository.findOneById(bookId);

        String fileName = book.getImgLocation();
        String imgPath = imgDirectory + fileName;
        FileSystemResource fileResource = new FileSystemResource(imgPath);

        logger.info("image path: " + imgPath);
        // Check if the file exists
        if (fileResource.exists()) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // Set the appropriate content type

            // Return the file as a response with appropriate headers
            return new ResponseEntity<>(fileResource, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}