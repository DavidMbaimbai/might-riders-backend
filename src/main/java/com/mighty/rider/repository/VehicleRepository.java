package com.mighty.rider.repository;

import com.mighty.rider.modal.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;



public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {

}
