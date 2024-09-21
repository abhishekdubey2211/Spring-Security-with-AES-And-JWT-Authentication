package com.shopping.portal.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.shopping.portal.dto.EndUserDTO;
import com.shopping.portal.model.ResponseApi;
import com.shopping.portal.service.UserService;

@RestController
@RequestMapping("/api/v1")
public class EndUserController {

	@Autowired
	private UserService userService;

	@GetMapping
	public String version() {
		return "ShoppingPortal v0.0.1   ||  01/09/2024";
	}

	@PostMapping("/user")
	public ResponseEntity<?> addEndUser(@RequestBody EndUserDTO user) throws Exception {
		List<Object> endUserList = new ArrayList<>();
		EndUserDTO savedUser = userService.registerUser(user);
		endUserList.add(savedUser);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ResponseApi.createResponse(1, "User Registered Sucessfully", endUserList));
	}

	@PutMapping("/user")
	public ResponseEntity<?> editEndUser(@RequestBody EndUserDTO user) throws Exception {
		List<Object> endUserList = new ArrayList<>();
		EndUserDTO savedUser = userService.updateUser(user);
		endUserList.add(savedUser);
		return ResponseEntity.status(HttpStatus.ACCEPTED)
				.body(ResponseApi.createResponse(1, "User Details Upgraded Sucessfully", endUserList));
	}

	@GetMapping("/user/get_users")
	public ResponseEntity<?> getAllEndUser() {
		List<Object> endUserList = userService.getAllActiveUser();
		return ResponseEntity.status(HttpStatus.OK)
				.body(ResponseApi.createResponse(1, "Retrived All User Details Sucessfully ", endUserList));
	}

	@GetMapping("/user/{id}")
	public ResponseEntity<?> getEndUserById(@PathVariable("id") Long id) {
		List<Object> endUserList = new ArrayList<>();
		EndUserDTO retrived = userService.getUserById(id);
		endUserList.add(retrived);
		return ResponseEntity.status(HttpStatus.OK).body(ResponseApi.createResponse(1, "User Details Retrived Sucessfully with userid " + retrived.getUserid(),
				endUserList));
	}

	@DeleteMapping("/user/{id}")
	public ResponseEntity<?> deleteEndUserById(@PathVariable Long id) {
		List<Object> endUserList = userService.disableUser(id);
		return ResponseEntity.status(HttpStatus.OK).body(ResponseApi.createResponse(1, "User with UserId " + id + " Deleted Sucessfully ", endUserList));
	}

}
