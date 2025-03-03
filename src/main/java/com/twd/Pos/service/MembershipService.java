package com.twd.Pos.service;

import java.util.List;

import com.twd.Pos.entity.Membership;

public interface MembershipService {

    List<Membership> getAllMemberships();
    Membership getMembershipById(Long id);
    Membership createMembership(Membership membership);
    Membership updateMembership(Long id, Membership membership);
    void deleteMembership(Long id);

}
