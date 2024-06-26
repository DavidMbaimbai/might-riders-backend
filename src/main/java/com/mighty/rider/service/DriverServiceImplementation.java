package com.mighty.rider.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mighty.rider.config.JwtUtil;
import com.mighty.rider.exception.DriverException;
import com.mighty.rider.modal.Driver;
import com.mighty.rider.modal.License;
import com.mighty.rider.modal.Ride;
import com.mighty.rider.modal.Vehicle;
import com.mighty.rider.repository.DriverRepository;
import com.mighty.rider.repository.LicenseRepository;
import com.mighty.rider.repository.RideRepository;
import com.mighty.rider.repository.VehicleRepository;
import com.mighty.rider.request.DriversSignupRequest;
import com.mighty.rider.ride.domain.RideStatus;
import com.mighty.rider.ride.domain.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;





@Service
public class DriverServiceImplementation implements DriverService {
	
	@Autowired
	private DriverRepository driverRepository;
	
	@Autowired
	private Calculaters distenceCalculator;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private VehicleRepository vehicleRepository;
	
	@Autowired
	private LicenseRepository licenseRepository;
	
	@Autowired
	private RideRepository rideRepository;

	@Override
	public List<Driver> getAvailableDrivers(double pickupLatitude, double picupLongitude, double radius, Ride ride) {
		List<Driver> allDrivers=driverRepository.findAll();
		
		List<Driver> availableDriver=new ArrayList<>();
		
		
		for(Driver driver:allDrivers) {
			
			if(driver.getCurrentRide()!=null && driver.getCurrentRide().getStatus()!= RideStatus.COMPLETED
					) {
				
				continue;
			}
			if(ride.getDeclinedDrivers().contains(driver.getId())) {
				System.out.println("its containes");
				continue;
			}

			double driverLatitude=driver.getLatitude();
			double driverLongitude=driver.getLongitude();
			
			
			
			double distence=distenceCalculator.calculateDistance(driverLatitude,driverLongitude, pickupLatitude, picupLongitude);
			
//			if(distence<=radius) {
				availableDriver.add(driver);
//			}
		}
		
		return availableDriver;
	}

	@Override
	public Driver findNearestDriver(List<Driver> availableDrivers, double picupLatitude, double picupLongitude) {
		
		double min=Double.MAX_VALUE;;
		Driver nearestDriver = null;
		
//		List<Driver> drivers=new ArrayList<>();
//		double minAuto
		
		for(Driver driver : availableDrivers) {
			double driverLatitude=driver.getLatitude();
			double driverLongitude=driver.getLongitude();
			
			double distence=distenceCalculator.calculateDistance(picupLatitude, picupLongitude, driverLatitude,driverLongitude);
			
			if(min>distence) {
				min=distence;
				nearestDriver=driver;
			}
		}
		
		return nearestDriver;
	}

	@Override
	public Driver registerDriver(DriversSignupRequest driversSignupRequest) {

		License license=driversSignupRequest.getLicense();
		Vehicle vehicle=driversSignupRequest.getVehicle();

		License createdLicense=new License();
		
		createdLicense.setLicenseState(license.getLicenseState());
		createdLicense.setLicenseNumber(license.getLicenseNumber());
		createdLicense.setLicenseExpirationDate(license.getLicenseExpirationDate());
		createdLicense.setId(license.getId());
		
		License savedLicense=licenseRepository.save(createdLicense);
		
		Vehicle createdVehicle = new Vehicle();
		
		createdVehicle.setCapacity(vehicle.getCapacity());
		createdVehicle.setColor(vehicle.getColor());
		createdVehicle.setId(vehicle.getId());
		createdVehicle.setLicensePlate(vehicle.getLicensePlate());
		createdVehicle.setMake(vehicle.getMake());
		createdVehicle.setModel(vehicle.getModel());
		createdVehicle.setYear(vehicle.getYear());
		
		Vehicle savedVehicle = vehicleRepository.save(createdVehicle);
		
		Driver driver = new Driver();
		
		String encodedPassword = passwordEncoder.encode(driversSignupRequest.getPassword());
		
		driver.setEmail(driversSignupRequest.getEmail());
		driver.setName(driversSignupRequest.getName());
		driver.setMobile(driversSignupRequest.getMobile());
		driver.setPassword(encodedPassword);
		driver.setLicense(savedLicense);
		driver.setVehicle(savedVehicle);
		driver.setRole(UserRole.DRIVER) ;
		
		driver.setLatitude(driversSignupRequest.getLatitude());
		driver.setLongitude(driversSignupRequest.getLongitude());
		
		
		Driver createdDriver = driverRepository.save(driver);
		
		savedLicense.setDriver(createdDriver);
		savedVehicle.setDriver(createdDriver);
		
		licenseRepository.save(savedLicense);
		vehicleRepository.save(savedVehicle);
		
		return createdDriver;
			
	}

	@Override
	public Driver getReqDriverProfile(String jwt) throws DriverException {
		String email=jwtUtil.getEmailFromToken(jwt);
		Driver driver= driverRepository.findByEmail(email);
		if(driver==null) {
			throw new DriverException("driver not exist with email " + email);
		}
		
		return driver;
		
	}

	@Override
	public Ride getDriversCurrentRide(Integer driverId) throws DriverException {
		Driver driver = findDriverById(driverId);
		return driver.getCurrentRide();
	}

	@Override
	public List<Ride> getAllocatedRides(Integer driverId) throws DriverException {
		List<Ride> allocatedRides=driverRepository.getAllocatedRides(driverId);
		return allocatedRides;
	}

	@Override
	public Driver findDriverById(Integer driverId) throws DriverException {
		Optional<Driver> opt=driverRepository.findById(driverId);
		if(opt.isPresent()) {
			return opt.get();
		}
		throw new DriverException("driver not exist with id "+driverId);
	}

	@Override
	public List<Ride> completedRids(Integer driverId) throws DriverException {
		List <Ride> completedRides=driverRepository.getCompletedRides(driverId);
		return completedRides;
	}

	

}
