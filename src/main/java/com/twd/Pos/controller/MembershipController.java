package com.twd.Pos.controller;

import com.twd.Pos.entity.Membership;
import com.twd.Pos.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class MembershipController {

    @Autowired
    private MembershipService membershipService;

    @GetMapping("/getallmemberships")
    public List<Membership> getAllMemberships() {
        return membershipService.getAllMemberships();
    }

    @GetMapping("/getmembershipById/{id}")
    public Membership getMembershipById(@PathVariable Long id) {
        return membershipService.getMembershipById(id);
    }

    @PostMapping("/createmembership")
    public Membership createMembership(@RequestBody Membership membership) {
        return membershipService.createMembership(membership);
    }

    @PutMapping("/updatemembership/{id}")
    public Membership updateMembership(@PathVariable Long id, @RequestBody Membership membership) {
        return membershipService.updateMembership(id, membership);
    }

    @DeleteMapping("/deletemembership/{id}")
    public String deleteMembership(@PathVariable Long id) {
        membershipService.deleteMembership(id);
        return "Membership deleted successfully";
    }
}
