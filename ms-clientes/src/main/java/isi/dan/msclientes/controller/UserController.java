package isi.dan.msclientes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import isi.dan.msclientes.aop.LogExecutionTime;
import isi.dan.msclientes.dto.CreateUserDTO;
import isi.dan.msclientes.exception.ClienteNotFoundException;
import isi.dan.msclientes.exception.InvalidCredentialsException;
import isi.dan.msclientes.exception.UserNotFoundException;
import isi.dan.msclientes.model.User;
import isi.dan.msclientes.servicios.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    @LogExecutionTime
    public ResponseEntity<User> logIn(@RequestParam String email, @RequestParam String password)
            throws InvalidCredentialsException, UserNotFoundException {
        return ResponseEntity.ok(userService.authenticate(email, password));
    }

    @PostMapping("/register")
    @LogExecutionTime
    public ResponseEntity<User> register(@RequestBody @Validated CreateUserDTO user) throws ClienteNotFoundException {
        return ResponseEntity.ok(userService.save(user));
    }

    @PostMapping("/logout")
    @LogExecutionTime
    public ResponseEntity<User> logOut() {
        // TODO
        return ResponseEntity.ok(null);
    }
}
