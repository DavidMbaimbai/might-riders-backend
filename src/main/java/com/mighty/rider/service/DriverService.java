package com.mighty.rider.service;

import java.util.List;

import com.mighty.rider.exception.DriverException;
import com.mighty.rider.modal.Driver;
import com.mighty.rider.modal.Ride;
import com.mighty.rider.request.DriversSignupRequest;



public interface DriverService {
	
	public Driver registerDriver(DriversSignupRequest driverSignupRequest);
	
	public List<Driver> getAvailableDrivers(double pickupLatitude,
			double picupLongitude,double radius, Ride ride);
	
	public Driver findNearestDriver(List<Driver> availableDrivers, 
			double picupLatitude, double picupLongitude);
	
	public Driver getReqDriverProfile(String jwt) throws DriverException;
	
	public Ride getDriversCurrentRide(Integer driverId) throws DriverException;
	
	public List<Ride> getAllocatedRides(Integer driverId) throws DriverException;
	
	public Driver findDriverById(Integer driverId) throws DriverException;
	
	public List<Ride> completedRids(Integer driverId) throws DriverException;
	
	
	



}
