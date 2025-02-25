package com.mighty.rider.repository;

import java.util.List;
import java.util.Optional;

import com.mighty.rider.modal.Ride;
import com.mighty.rider.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface UserRepository extends JpaRepository<User, Integer> {

	public User findByEmail(String email);
	
	@Query("SELECT R FROM Ride R where R.status=COMPLETED AND R.user.Id=:userId ORDER BY R.endTime DESC")
	public List<Ride> getCompletedRides(@Param("userId")Integer userId);
}
