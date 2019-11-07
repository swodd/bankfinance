package com.bankfinance.management.controllers;

import com.bankfinance.management.entities.Role;
import com.bankfinance.management.entities.User;
import com.bankfinance.management.repositories.RolesRepository;
import com.bankfinance.management.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UsersController {

    @Autowired
    UsersRepository usersRepository;
    @Autowired
    RolesRepository rolesRepository;

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user){
        if(user != null) {
            User result = usersRepository.save(user);
            if(result != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(result);
            }
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> result = usersRepository.findAll();
        if(result != null) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        Optional<User> user = usersRepository.findById(id);
        if (user.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(user.get());
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/users")
    public ResponseEntity<Object> updateUserById(@RequestBody User user){
        if(user != null) {
            if (usersRepository.existsById(user.getId())){
                User result = usersRepository.save(user);
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Object> deleteUserById(@PathVariable Long id){
        if (usersRepository.existsById(id)){
            usersRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/users/{userId}/roles/{roleId}")
    public ResponseEntity<User> addRole(@PathVariable Long userId, @PathVariable Long roleId){
        Optional<User> user = usersRepository.findById(userId);
        Optional<Role> role = rolesRepository.findById(roleId);
        if(user.isPresent() && role.isPresent()){
            User userFromDb = user.get();
            userFromDb.getRoles().add(role.get());
            User result = usersRepository.save(userFromDb);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/users/{userId}/roles/{roleId}")
    public ResponseEntity<User> deleteRole(@PathVariable Long userId, @PathVariable Long roleId){
        Optional<User> user = usersRepository.findById(userId);
        Optional<Role> role = rolesRepository.findById(roleId);
        if(user.isPresent() && role.isPresent()){
            User userFromDb = user.get();
            userFromDb.getRoles().remove(role.get());
            User result = usersRepository.save(userFromDb);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
