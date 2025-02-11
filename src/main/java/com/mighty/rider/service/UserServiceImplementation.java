package com.mighty.rider.service;

import java.util.List;
import java.util.Optional;

import com.mighty.rider.config.JwtUtil;
import com.mighty.rider.exception.UserException;
import com.mighty.rider.modal.Ride;
import com.mighty.rider.modal.User;
import com.mighty.rider.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;





@Service
public class UserServiceImplementation implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtUtil jwtUtil;

	@Override
	public User createUser(User user) throws UserException {
		
		User emailExist = findUserByEmail(user.getEmail());
		
		if(emailExist!=null)throw new UserException("Email Already Used With Another Account");
		

		
		return userRepository.save(user);
		
	}

	@Override
	public User findUserById(Integer userId) throws UserException {
		
		Optional<User> opt=userRepository.findById(userId);
		
		if(opt.isPresent()) {
			return opt.get();
		}
		throw new UserException("user not found with id "+userId);
	}

	@Override
	public User findUserByEmail(String email) throws UserException {
		
		User user=userRepository.findByEmail(email);
		
		if(user!=null) {
			return user;
		}
		throw new UserException("user not found with email "+email);
	}

	@Override
	public User getReqUserProfile(String token) throws UserException {
		
		String email = jwtUtil.getEmailFromToken(token);
		User user = userRepository.findByEmail(email);
		 
		if(user!=null) {
			return user;
		}
		
		throw new UserException("invalid token...");
		
	}

	@Override
	public User findUserByToken(String token) throws UserException {
		String email=jwtUtil.getEmailFromToken(token);
		if(email==null) {
			throw new BadCredentialsException("invalid token recived");
		}
		User user=userRepository.findByEmail(email);
		
		if(user!=null) {
			return user;
		}
		throw new UserException("user not found with email "+email);
	}

	@Override
	public List<Ride> completedRids(Integer userId) throws UserException {
		List <Ride> completedRides=userRepository.getCompletedRides(userId);
		return completedRides;
	}

}
