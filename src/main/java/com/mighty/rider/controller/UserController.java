package com.mighty.rider.controller;

import java.util.List;

import com.mighty.rider.exception.UserException;
import com.mighty.rider.modal.Ride;
import com.mighty.rider.modal.User;
import com.mighty.rider.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/users")
public class UserController {
	@Autowired
	private UserService userService;
	
	@GetMapping("/{userId}")
	public ResponseEntity<User> findUserByIdHandler(@PathVariable Integer userId) throws UserException, UserException {
		System.out.println("find by user id");
		User createdUser = userService.findUserById(userId);
		
		return new ResponseEntity<User>(createdUser,HttpStatus.ACCEPTED);
		
	}
	
	@GetMapping("/profile")
	public ResponseEntity<User> getReqUserProfileHandler(@RequestHeader("Authorization") String jwt) throws UserException{
		
		User user=userService.getReqUserProfile(jwt);
		
		return new ResponseEntity<User>(user,HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/rides/completed")
	public ResponseEntity<List<Ride>> getcompletedRidesHandler(@RequestHeader("Authorization") String jwt) throws UserException {
		
		User user = userService.getReqUserProfile(jwt);
		
		List<Ride> rides=userService.completedRids(user.getId());
		
		return new ResponseEntity<>(rides,HttpStatus.ACCEPTED);
	}

}
