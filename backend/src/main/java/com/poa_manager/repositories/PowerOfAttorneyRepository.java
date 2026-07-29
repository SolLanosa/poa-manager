package com.poa_manager.repositories;
import com.poa_manager.entities.PowerOfAttorney;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PowerOfAttorneyRepository extends JpaRepository<PowerOfAttorney, Long> {
}