package com.poa_manager.mappers;

import com.poa_manager.dto.CompanyDTO;
import com.poa_manager.entities.Company;

public class CompanyMapper {
    public static CompanyDTO toDTO(Company company) {
        return new CompanyDTO(
                company.getId(),
                company.getName(),
                company.getType()
        );
    }
}
