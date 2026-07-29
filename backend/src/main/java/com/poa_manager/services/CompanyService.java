package com.poa_manager.services;

import com.poa_manager.dto.CompanyDTO;
import com.poa_manager.entities.Company;
import com.poa_manager.mappers.CompanyMapper;
import com.poa_manager.repositories.CompanyRepository;
import com.poa_manager.responses.CompaniesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompaniesResponse getCompanies(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        Page<Company> companies = companyRepository.findAll(
                pageable
        );
        Long count = companies.getTotalElements();
        List<CompanyDTO> companiesDTO = companies.getContent().stream().map(CompanyMapper::toDTO).toList();

        return new CompaniesResponse(companiesDTO, count);
    }
}

