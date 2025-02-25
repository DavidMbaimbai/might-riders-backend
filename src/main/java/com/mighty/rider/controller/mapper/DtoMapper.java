package com.mighty.rider.controller.mapper;

import com.mighty.rider.dto.DriverDTO;
import com.mighty.rider.dto.RideDTO;
import com.mighty.rider.dto.UserDTO;
import com.mighty.rider.modal.Driver;
import com.mighty.rider.modal.Ride;
import com.mighty.rider.modal.User;
import org.springframework.stereotype.Service;


@Service
public class DtoMapper {
	
	public static DriverDTO toDriverDto(Driver driver){
		
		DriverDTO driverDto=new DriverDTO();
		
		driverDto.setEmail(driver.getEmail());
		driverDto.setId(driver.getId());
		driverDto.setLatitude(driver.getLatitude());
		driverDto.setLongitude(driver.getLongitude());
		driverDto.setMobile(driver.getMobile());
		driverDto.setName(driver.getName());
		driverDto.setRating(driver.getRatig());
		driverDto.setRole(driver.getRole());
		driverDto.setVehicle(driver.getVehicle());
		
		
		return driverDto;
		
	}
	public static UserDTO toUserDto(User user) {
		
		UserDTO userDto=new UserDTO();
		
		userDto.setEmail(user.getEmail());
		userDto.setId(user.getId());
		userDto.setMobile(user.getMobile());
		userDto.setName(user.getFullName());
		
		return userDto;
		
	}
	
	public static RideDTO toRideDto(Ride ride) {
		DriverDTO driverDTO=toDriverDto(ride.getDriver());
		UserDTO userDto=toUserDto(ride.getUser());
		
		RideDTO rideDto=new RideDTO();
		
		rideDto.setDestinationLatitude(ride.getDestinationLatitude());
		rideDto.setDestinationLongitude(ride.getDestinationLongitude());
		rideDto.setDistance(ride.getDistence());
		rideDto.setDriver(driverDTO);
		rideDto.setDuration(ride.getDuration());
		rideDto.setEndTime(ride.getEndTime());
		rideDto.setFare(ride.getFare());
		rideDto.setId(ride.getId());
		rideDto.setPickupLatitude(ride.getPickupLatitude());
		rideDto.setPickupLongitude(ride.getPickupLongitude());
		rideDto.setStartTime(ride.getStartTime());
		rideDto.setStatus(ride.getStatus());
		rideDto.setUser(userDto);
		rideDto.setPickupArea(ride.getPickupArea());
		rideDto.setDestinationArea(ride.getDestinationArea());
		rideDto.setPaymentDetails(ride.getPaymentDetails());
		rideDto.setOtp(ride.getOtp());
		return rideDto;
	}
	

	

}
