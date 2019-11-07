package com.bankfinance.management.controllers;

import com.bankfinance.management.entities.Role;
import com.bankfinance.management.repositories.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class RolesController {
    @Autowired
    RolesRepository rolesRepository;

    @PostMapping("/roles")
    public ResponseEntity<Role> createRole(@RequestBody Role role){
        if(role != null) {
            Role result = rolesRepository.save(role);
            if(result != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(result);
            }
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles(){
        List<Role> result = rolesRepository.findAll();
        if(result != null) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/roles/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id){
        Optional<Role> role = rolesRepository.findById(id);
        if (role.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(role.get());
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/roles")
    public ResponseEntity<Object> updateRoleById(@RequestBody Role role){
        if(role != null) {
            if (rolesRepository.existsById(role.getId())){
                Role result = rolesRepository.save(role);
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/roles/{id}")
    public ResponseEntity<Object> deleteRoleById(@PathVariable Long id){
        if (rolesRepository.existsById(id)){
            rolesRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
