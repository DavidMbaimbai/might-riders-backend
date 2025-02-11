package com.mighty.rider.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mighty.rider.modal.User;
import com.mighty.rider.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



@Service
public class CustomDriverUserDetailsService implements UserDetailsService {

	@Autowired
	private final UserRepository userRepository;
	
	public CustomDriverUserDetailsService(UserRepository userRepository) {
		
		   this.userRepository = userRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username);
        
        if (user==null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
		
}
