package com.example.demo.Controller;

import com.example.demo.DTO.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api")
public class BoardController {
    @GetMapping("/boards")
    public ResponseEntity<?> getAllBoard(@RequestParam(required = false) String title,
                                         @RequestParam(required = false) String writer,
                                         @RequestParam(defaultValue = "1") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok("");
    }

    @PostMapping("/boards/new")
    public ResponseEntity<?> createBoard(@RequestBody UserDto userDto) throws IllegalAccessException {
        return ResponseEntity.ok("");
    }

    @GetMapping("/boards/{id}")
    public ResponseEntity<?> findUserById(@PathVariable("username") String username){
        return ResponseEntity.ok("");
    }

    @PatchMapping("/boards/{id}")
    public ResponseEntity<?> updateUserInfoById(@PathVariable("username") String username){
        return ResponseEntity.ok("");
    }

    @DeleteMapping("/boards/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable("username") String username){
        return ResponseEntity.ok("");
    }
}
