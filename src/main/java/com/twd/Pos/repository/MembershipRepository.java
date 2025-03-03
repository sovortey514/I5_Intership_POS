package com.twd.Pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.twd.Pos.entity.Membership;

@Repository
public interface MembershipRepository extends JpaRepository <Membership, Long>{

    Membership findByMembershipId(String membershipId);
    
}
