package com.twd.SpringSecurityJWT_Pos.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String username;
    private String email;
    private String password;
    private String role;
    private boolean enabled;
    

    @Lob
    @Column(name = "profile_image")
    private byte[] profileImage;

    @OneToMany(mappedBy = "user")
    private Set<FileData> files; 

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getUsername() {
        return email;
    }

    // public boolean isEnabled() {
    //     return enabled;
    // }

    @Override
    public boolean isEnabled() {
        return true;
    }


    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    public String getName() {
        return name;
    }
    
    public String getrole() {
        return role;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
