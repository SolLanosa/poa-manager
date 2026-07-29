package com.poa_manager.repositories;
import com.poa_manager.entity.PowerOfAttorneyGroupMember;
import com.poa_manager.entity.PowerOfAttorneyGroupMemberId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PowerOfAttorneyGroupMemberRepository
        extends JpaRepository<PowerOfAttorneyGroupMember, PowerOfAttorneyGroupMemberId> {
}