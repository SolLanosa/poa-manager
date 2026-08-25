package com.poa_manager.responses;

import com.poa_manager.dto.MemberDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MembersResponse {
  private List<MemberDTO> members;
  private Long count;
}
