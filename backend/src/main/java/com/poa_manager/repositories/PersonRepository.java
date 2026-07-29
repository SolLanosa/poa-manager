package com.poa_manager.repositories;
import com.poa_manager.entities.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Page<Person> findByCompanies_Company_Id(Long companyId, Pageable pageable);
}