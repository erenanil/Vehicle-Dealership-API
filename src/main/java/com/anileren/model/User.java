package com.anileren.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.anileren.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity implements UserDetails{
    // Oluşturmuş olduğum classta username ve password değerleri olduğundan bunu spring security'e entegre edebilmek için UserDetails Interfaceini implemente ediyorum.

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_"+role.name())).collect(Collectors.toList());
    }


    @Override
    public String getPassword() 
    { return password; }

    @Override
    public String getUsername() 
    { return username; }

    @Override
    public boolean isAccountNonExpired() 
    { return true; }

    @Override
    public boolean isAccountNonLocked() 
    { return true; }

    @Override
    public boolean isCredentialsNonExpired() 
    { return true; }

    @Override
    public boolean isEnabled() 
    { return true; }
}
   

