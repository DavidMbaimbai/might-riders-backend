package com.mighty.rider.controller;


import com.mighty.rider.config.JwtUtil;
import com.mighty.rider.exception.UserException;
import com.mighty.rider.modal.Driver;
import com.mighty.rider.modal.User;
import com.mighty.rider.repository.DriverRepository;
import com.mighty.rider.repository.UserRepository;
import com.mighty.rider.request.DriversSignupRequest;
import com.mighty.rider.request.LoginRequest;
import com.mighty.rider.request.SignupRequest;
import com.mighty.rider.response.JwtResponse;
import com.mighty.rider.ride.domain.UserRole;
import com.mighty.rider.service.CustomUserDetailsService;
import com.mighty.rider.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private DriverRepository driverRepository;
	
	@Autowired
	private DriverService driverService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	
	
	@PostMapping("/user/signup")
	public ResponseEntity<JwtResponse> signupHandler(@RequestBody SignupRequest signupRequest)throws UserException, AuthenticationException{

		
        User user = userRepository.findByEmail(signupRequest.getEmail());

        JwtResponse jwtResponse=new JwtResponse();
        
		
		if(user!=null) {
			throw new UserException("User Already Exist With Email "+signupRequest.getEmail());
		}
        
		String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

        // Create new user account
        User createdUser = new User();
        createdUser.setEmail(signupRequest.getEmail());
        createdUser.setPassword(encodedPassword);
        createdUser.setFullName(signupRequest.getFullName());
        createdUser.setMobile(signupRequest.getMobile());
        createdUser.setRole(UserRole.USER);
        
        User savedUser=userRepository.save(createdUser);
        
        Authentication authentication=new UsernamePasswordAuthenticationToken(savedUser.getEmail(), savedUser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // Generate JWT token
        String jwt = jwtUtil.generateJwtToken(authentication);
        
        jwtResponse.setJwt(jwt);
        jwtResponse.setAuthenticated(true);
        jwtResponse.setError(false);
        jwtResponse.setErrorDetails(null);
        jwtResponse.setType(UserRole.USER);
        jwtResponse.setMessage("Account Created Successfully: "+savedUser.getFullName());
        
        return new ResponseEntity<JwtResponse>(jwtResponse,
        		HttpStatus.ACCEPTED);
		
	}
	
	@PostMapping("/driver/signup")
	public ResponseEntity<JwtResponse> driverSignupHandler(@RequestBody DriversSignupRequest driverSignupRequest){
		
		Driver driver = driverRepository.findByEmail(driverSignupRequest.getEmail());

        JwtResponse jwtResponse=new JwtResponse();
			
		if(driver!=null) {
			
			jwtResponse.setAuthenticated(false);
		    jwtResponse.setErrorDetails("email already used with another account");
		    jwtResponse.setError(true);
		        
			return new ResponseEntity<JwtResponse>(jwtResponse,HttpStatus.BAD_REQUEST);
		}
		
		
		
		
		Driver createdDriver=driverService.registerDriver(driverSignupRequest);
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(createdDriver.getEmail(), createdDriver.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
		String jwt = jwtUtil.generateJwtToken(authentication);
        
        jwtResponse.setJwt(jwt);
        jwtResponse.setAuthenticated(true);
        jwtResponse.setError(false);
        jwtResponse.setErrorDetails(null);
        jwtResponse.setType(UserRole.DRIVER);
        jwtResponse.setMessage("Account Created Successfully: "+createdDriver.getName());
        
        
		return new ResponseEntity<JwtResponse>(jwtResponse,HttpStatus.ACCEPTED);
	}
	
	
	@PostMapping("/signin")
    public ResponseEntity<JwtResponse> signin(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        
        Authentication authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        String jwt = jwtUtil.generateJwtToken(authentication);

        JwtResponse jwtResponse=new JwtResponse();
        
        jwtResponse.setJwt(jwt);
        jwtResponse.setAuthenticated(true);
        jwtResponse.setError(false);
        jwtResponse.setErrorDetails(null);
        jwtResponse.setMessage("Account Created Successfully: ");

        return new ResponseEntity<JwtResponse>(jwtResponse,HttpStatus.OK);
    }
	
	private Authentication authenticate(String username, String password) {
		System.out.println("sign in userDetails - "+password);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        System.out.println("sign in userDetails after loaduser- "+userDetails);
        
        if (userDetails == null) {
        	System.out.println("sign in userDetails - null " + userDetails);
            throw new BadCredentialsException("Invalid username or password");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
        	System.out.println("sign in userDetails - password not match " + userDetails);
            throw new BadCredentialsException("Invalid username or password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
	
	
	


}
