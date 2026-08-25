package com.poa_manager.repositories;

import com.poa_manager.entities.PowerOfAttorney;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PowerOfAttorneyRepository extends JpaRepository<PowerOfAttorney, Long> {
  List<PowerOfAttorney> findByCompany_Id(Long companyId);
}
