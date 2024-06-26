package com.mighty.rider.service;


import com.mighty.rider.exception.DriverException;
import com.mighty.rider.exception.RideException;
import com.mighty.rider.modal.Driver;
import com.mighty.rider.modal.Ride;
import com.mighty.rider.modal.User;
import com.mighty.rider.request.RideRequest;

public interface RideService {
	
	
	public Ride requestRide(RideRequest rideRequest, User user) throws DriverException;
	
	public Ride createRideRequest(User user, Driver nearesDriver,
			double picupLatitude,double pickupLongitude,
			double destinationLatitude,double destinationLongitude,
			String pickupArea,String destinationArea);
	
	public void acceptRide(Integer rideId) throws RideException;
	
	public void declineRide(Integer rideId, Integer driverId) throws RideException;
	
	public void startRide(Integer rideId,int opt) throws RideException;
	
	public void completeRide(Integer rideId) throws RideException;
	
	public void cancleRide(Integer rideId) throws RideException;
	
	public Ride findRideById(Integer rideId) throws RideException;

}
