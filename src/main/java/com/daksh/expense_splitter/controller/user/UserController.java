package com.daksh.expense_splitter.controller.user;

import com.daksh.expense_splitter.dto.user.CreateUserDto;
import com.daksh.expense_splitter.dto.user.Setpassword;
import com.daksh.expense_splitter.dto.user.UpdateUserDto;
import com.daksh.expense_splitter.dto.user.UserDto;
import com.daksh.expense_splitter.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
 private final UserService service;
    @GetMapping
    public ResponseEntity<List<UserDto>>getAllUsers(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String id){
        return  ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid  @RequestBody CreateUserDto createUserDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createNewUser(createUserDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id){
        service.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateFullUser(@PathVariable String id,@Valid @RequestBody UpdateUserDto updateUserDto){
        return  ResponseEntity.ok(service.updateUser(id, updateUserDto));
    }
    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> updatePartialUser(@PathVariable String id, @RequestBody Map<String, Object> updates){
            return  ResponseEntity.ok(service.updatePartialUser(id,updates));
    }

    @PostMapping("/{id}/set-password")
    public ResponseEntity<String> setPassword(@PathVariable String id, @RequestBody Setpassword setpassword){
        service.setPasswordById(id,setpassword);
        return ResponseEntity.ok("password set successfully");
    }


}
