package com.twd.Pos.service.impl;

import com.twd.Pos.entity.Membership;
import com.twd.Pos.repository.MembershipRepository;
import com.twd.Pos.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MembershipServiceImpl implements MembershipService {

    @Autowired
    private MembershipRepository membershipRepository;

    @Override
    public List<Membership> getAllMemberships() {
        try {
            return membershipRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving memberships: " + e.getMessage());
        }
    }

    @Override
    public Membership getMembershipById(Long id) {
        try {
            return membershipRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Membership not found with ID: " + id));
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving membership: " + e.getMessage());
        }
    }

    @Override
    public Membership createMembership(Membership membership) {
        try {
            return membershipRepository.save(membership);
        } catch (Exception e) {
            throw new RuntimeException("Error creating membership: " + e.getMessage());
        }
    }

    @Override
    public Membership updateMembership(Long id, Membership membership) {
        try {
            Optional<Membership> existingMembership = membershipRepository.findById(id);
            if (existingMembership.isPresent()) {
                Membership updatedMembership = existingMembership.get();
                updatedMembership.setMembershipId(membership.getMembershipId());
                updatedMembership.setName(membership.getName());
                updatedMembership.setMembershipType(membership.getMembershipType());
                updatedMembership.setBalance(membership.getBalance());
                updatedMembership.setGender(membership.getGender());
                return membershipRepository.save(updatedMembership);
            } else {
                throw new RuntimeException("Membership not found with ID: " + id);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error updating membership: " + e.getMessage());
        }
    }

    @Override
    public void deleteMembership(Long id) {
        try {
            if (!membershipRepository.existsById(id)) {
                throw new RuntimeException("Membership not found with ID: " + id);
            }
            membershipRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting membership: " + e.getMessage());
        }
    }
}
