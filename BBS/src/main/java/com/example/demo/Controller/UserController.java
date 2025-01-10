package com.example.demo.Controller;

import com.example.demo.DTO.UserDto;
import com.example.demo.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api")
public class UserController {

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(@RequestParam(required = false) String name,
                                  @RequestParam(required = false) String email,
                                  @RequestParam(required = false) String username,
                                  @RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok("");
    }

    @PostMapping("/users/new")
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto) throws IllegalAccessException {
        return ResponseEntity.ok("");
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<?> findUserByUserName(@PathVariable("username") String username){
        return ResponseEntity.ok("");
    }

    @PatchMapping("/users/{username}")
    public ResponseEntity<?> updateUserInfoByUserName(@PathVariable("username") String username){
        return ResponseEntity.ok("");
    }

    @DeleteMapping("/users/{username}")
    public ResponseEntity<?> deleteUserByUserName(@PathVariable("username") String username){
        return ResponseEntity.ok("");
    }
}
