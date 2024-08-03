package com.shopping.portal.controller;
import com.shopping.portal.model.EndUser;
import com.shopping.portal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserAdminController {

    private final UserService userService;

    @Autowired
    public UserAdminController(UserService userService) {
        this.userService = userService;
    }

    // Get all users by role
    @GetMapping
    public ResponseEntity<List<EndUser>> getAllUsers(@RequestParam("role") String role) {
        List<EndUser> users = userService.getAllUsers(role);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // Get all admin users
    @GetMapping("/admins")
    public ResponseEntity<List<EndUser>> getAllAdminUsers() {
        List<EndUser> adminUsers = userService.getAllAdminUsers("ADMIN");
        return new ResponseEntity<>(adminUsers, HttpStatus.OK);
    }

    // Get a user by ID
    @GetMapping("/{id}")
    public ResponseEntity<EndUser> getUserById(@PathVariable("id") long id) {
        EndUser user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // Update user details
    @PutMapping("/{id}")
    public ResponseEntity<EndUser> updateUserDetails(@PathVariable("id") long id, @RequestBody EndUser user) {
        user.setId(id); // Ensure the ID in the path matches the user object
        EndUser updatedUser = userService.updateUserDetails(user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
}
