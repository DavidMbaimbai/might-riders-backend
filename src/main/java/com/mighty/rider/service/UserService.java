package com.mighty.rider.service;

import java.util.List;

import com.mighty.rider.exception.UserException;
import com.mighty.rider.modal.Ride;
import com.mighty.rider.modal.User;
import org.springframework.stereotype.Service;





public interface UserService {
	
	public User createUser(User user) throws UserException;
	
	public User getReqUserProfile(String token) throws UserException;
	
	public User findUserById(Integer Id) throws UserException;
	
	public User findUserByEmail(String email) throws UserException;
	
	public User findUserByToken(String token) throws UserException;
	
	public List<Ride> completedRids(Integer userId) throws UserException;
	

}
