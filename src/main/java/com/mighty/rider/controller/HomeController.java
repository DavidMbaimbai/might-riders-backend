package com.mighty.rider.controller;

import com.mighty.rider.config.JwtUtil;
import com.mighty.rider.exception.UserException;
import com.mighty.rider.modal.Driver;
import com.mighty.rider.modal.User;
import com.mighty.rider.repository.DriverRepository;
import com.mighty.rider.repository.UserRepository;
import com.mighty.rider.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class HomeController {
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private DriverRepository driverRepository;
	
	@GetMapping({"/api","/"})
	public ResponseEntity<MessageResponse> homeController(){
		MessageResponse res=new MessageResponse("welcome to cab booking api");
		
		return new ResponseEntity<MessageResponse>(res,HttpStatus.ACCEPTED);
		
	}
	
	@GetMapping("/api/profile")
	public ResponseEntity<?> userProfileHandler(@RequestHeader("Authorization") String jwt) throws UserException{
		
		
		
		String email=jwtUtil.getEmailFromToken(jwt);
		
		if(email==null) {
			throw new BadCredentialsException("invalid token recived");
		}
		
		Driver driver=driverRepository.findByEmail(email);
		
		if(driver!=null) {
			return new ResponseEntity<Driver>(driver,HttpStatus.ACCEPTED);
		}
		
		User user=userRepository.findByEmail(email);
		
		if(user!=null) {
			System.out.println("user - "+user.getEmail());
			return new ResponseEntity<User>(user,HttpStatus.ACCEPTED);
		}
		
		throw new UserException("user not found with email "+email);
	}

}
