package com.shopping.portal.controller;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopping.portal.model.DecryptToken;
import com.shopping.portal.model.EndUser;
import com.shopping.portal.model.LoginRequest;
import com.shopping.portal.model.LoginResponse;
import com.shopping.portal.repository.EnduserRepository;
import com.shopping.portal.service.EndUserDetailService;
import com.shopping.portal.service.UserServiceImpl;
import com.shopping.portal.util.JWTUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/v1")
public class UserController {

	@Autowired
	EnduserRepository enduserRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	private JWTUtil jwtService;

	@Autowired
	EndUserDetailService userDetailService;

	@Autowired
	private UserServiceImpl endUserService;

	@GetMapping("/admin")
	public String version1() {
		return "Hello ADMIN";
	}

	@GetMapping("/user")
	public String version() {
		return "Hello USER";
	}

//	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/authenticate")
	public LoginResponse authenticate(@RequestBody LoginRequest loginform) {
		LoginResponse response = new LoginResponse();
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginform.getUsername(), loginform.getPassword()));
		if (authentication.isAuthenticated()) {
			SecurityContextHolder.getContext().setAuthentication(authentication);
			UserDetails user = userDetailService.loadUserByUsername(loginform.getUsername());
			response.setUserName(user.getUsername());
			response.setJWT_Token(jwtService.generateTokenfromUsername(user));
			response.setRoles(
					user.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList()));
			return response;
		} else {
			throw new UsernameNotFoundException("User Credentials Not Found");
		}
	}

//	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/register/user")
	public EndUser registration(@RequestBody EndUser user) {
		Optional<EndUser> checkuserexists = enduserRepository.findByEmail(user.getEmail());
		if (checkuserexists.isPresent()) {
			throw new UsernameNotFoundException("User Already Exists");
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return enduserRepository.save(user);
	}

//	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/decrypt/")
	public DecryptToken decodeToken(@RequestBody DecryptToken decryptToken) {
		DecryptToken token = new DecryptToken();
		try {
			token.setToken(jwtService.decodeEncryptedkey(decryptToken.getToken()));
//			token.setToken("hello");
			return token;
		} catch (Exception e) {
			e.printStackTrace();
			token.setToken("Invalid Token Failed to decrypt");
			return token;
		}
	}
}
