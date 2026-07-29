package com.poa_manager.repositories;
import com.poa_manager.entities.CompanyMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CompanyMemberRepository extends PagingAndSortingRepository<CompanyMember, Long>, JpaRepository<CompanyMember, Long> {
}