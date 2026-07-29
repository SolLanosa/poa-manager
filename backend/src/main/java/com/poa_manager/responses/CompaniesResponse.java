package com.poa_manager.responses;
import com.poa_manager.dto.CompanyDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CompaniesResponse  {
    private List<CompanyDTO> companies;
    private Long count;
}
