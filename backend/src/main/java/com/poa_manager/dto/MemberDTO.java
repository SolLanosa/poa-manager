package com.poa_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String nationalId;
}
