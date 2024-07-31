package com.example.AffairsManagementApp.security;

import com.example.AffairsManagementApp.entities.AppUser;
import com.example.AffairsManagementApp.entities.Role;
import com.example.AffairsManagementApp.repositories.Userrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;


@Service// this class will help us load users by their username and then extract their roles
public class CustomUserDetailsService implements UserDetailsService {

    private final Userrepository userrepository;

    @Autowired
    public CustomUserDetailsService(Userrepository userrepository) {
        this.userrepository = userrepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AppUser user = userrepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("AppUser not found: " + username));
        Collection<GrantedAuthority> grantedAuthorities = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toSet());
        return new User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }


}


