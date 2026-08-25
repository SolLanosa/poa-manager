package com.poa_manager.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import com.poa_manager.dto.AuthorizationRequestDTO;
import com.poa_manager.entities.AuthorizationRequest;
import com.poa_manager.entities.Company;
import com.poa_manager.entities.Decision;
import com.poa_manager.entities.Person;
import com.poa_manager.entities.PowerOfAttorney;
import com.poa_manager.entities.PowerOfAttorneyFaculty;
import com.poa_manager.entities.PowerOfAttorneyGroup;
import com.poa_manager.entities.PowerOfAttorneySigningRule;
import com.poa_manager.repositories.CompanyRepository;
import com.poa_manager.repositories.PersonRepository;
import com.poa_manager.repositories.PowerOfAttorneyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

  private final CompanyRepository companyRepository;
  private final PersonRepository personRepository;
  private final PowerOfAttorneyRepository powerOfAttorneyRepository;

  public Decision authorize(AuthorizationRequestDTO dto) {
    Company company = companyRepository.findById(dto.getCompanyId())
        .orElseThrow(() -> new IllegalArgumentException("Unknown company " + dto.getCompanyId()));

    List<Person> signers = personRepository.findAllById(dto.getSignersId());
    if (signers.size() != dto.getSignersId().size()) {
      throw new IllegalArgumentException("One or more signer ids do not exist");
    }

    AuthorizationRequest request = new AuthorizationRequest();
    request.setCompany(company);
    request.setSigners(new ArrayList<>(signers));
    request.setAction(dto.getAction());
    request.setObject(dto.getObject());
    request.setItemRef(dto.getItemRef());
    request.setAmount(dto.getAmount());
    request.setCurrency(dto.getCurrency());
    request.setDate(dto.getDate() == null ? LocalDateTime.now() : dto.getDate());

    Decision decision = new Decision();

    KieSession session = KieServices.get().getKieClasspathContainer().newKieSession("poaSession");

    try {
      session.insert(request);
      session.insert(decision);

      for (PowerOfAttorney poa : powerOfAttorneyRepository.findByCompany_Id(company.getId())) {
        session.insert(poa);

        for (PowerOfAttorneyGroup group : poa.getPowerOfAttorneyGroups()) {
          group.getMembers().forEach(session::insert);
        }

        for (PowerOfAttorneyFaculty faculty : poa.getFaculties()) {
          session.insert(faculty);
          faculty.getScopeItems().forEach(session::insert);

          for (PowerOfAttorneySigningRule rule : faculty.getSigningRules()) {
            session.insert(rule);
            rule.getPowerOfAttorneySigningRuleRequirements().forEach(session::insert);
          }
        }
      }
      session.fireAllRules();
    } finally {
      session.dispose();
    }

    return decision;

  }

}
