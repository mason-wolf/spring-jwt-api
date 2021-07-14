package com.fitnessapp.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fitnessapp.dao.RoleRepository;
import com.fitnessapp.dao.UserRepository;
import com.fitnessapp.model.AuthResponse;
import com.fitnessapp.model.CustomUserDetails;
import com.fitnessapp.model.NewUser;
import com.fitnessapp.model.Role;
import com.fitnessapp.model.Roles;
import com.fitnessapp.model.User;
import com.fitnessapp.security.JwtTokenUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	UserRepository userRepository;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	PasswordEncoder encoder;
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	JwtTokenUtil jwtTokenUtil;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody User user) {

		System.out.println("Attempted login");
	
		// Authenticate the request
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = jwtTokenUtil.generateJwtToken(authentication);
		System.out.println("Assigned token: " + token);
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		
		// Retrieve the roles for this user
		List<String> roles = userDetails.getAuthorities().stream()
									 .map(auth -> auth.getAuthority())
									 .collect(Collectors.toList());

		AuthResponse authResponse = new AuthResponse();
		authResponse.setToken(token);
		authResponse.setRoles(roles);
		authResponse.setUserId(userDetails.getId());
		return ResponseEntity.ok(authResponse);
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@Valid @RequestBody NewUser newUser) {

		// Check if user already exists
		if(userRepository.existsByUserName(newUser.getUserName())){
			return ResponseEntity.badRequest().body("Username already exists");
		}

		// Check if email is already in use
		if(userRepository.existsByEmail(newUser.getEmail())){
			return ResponseEntity.badRequest().body("Email already in use");
		}

		User user = new User();
		Set<Role> roles = new HashSet<>();
		user.setUserName(newUser.getUserName());
		user.setEmail(newUser.getEmail());
		user.setPassword(encoder.encode(newUser.getPassword()));

		String[] roleArr = newUser.getRoles();
		
		if(roleArr == null) {
			roles.add(roleRepository.findByRoleName(Roles.ROLE_USER).get());
		}

		for(String role: roleArr) {
			switch(role.toLowerCase()) {
				case "admin":
					roles.add(roleRepository.findByRoleName(Roles.ROLE_ADMIN).get());
					break;
				case "user":
					roles.add(roleRepository.findByRoleName(Roles.ROLE_USER).get());
					break;	
				default:
					return ResponseEntity.badRequest().body("Role not found");
			}
		}
		user.setRoles(roles);
		userRepository.save(user);

		return ResponseEntity.ok("User registered successfully");
	}
}
