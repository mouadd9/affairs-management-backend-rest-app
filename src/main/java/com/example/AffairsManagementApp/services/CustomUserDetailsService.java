/* package com.example.AffairsManagementApp.services;

import com.example.AffairsManagementApp.entities.AppUser;
import com.example.AffairsManagementApp.repositories.Userrepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;


@Service// this class will help us load users by their username and then extract their roles
public class CustomUserDetailsService implements UserDetailsService {
    private final Userrepository userrepository;
    public CustomUserDetailsService(Userrepository userrepository) {
        this.userrepository = userrepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AppUser utilisateur = userrepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("AppUser not found: " + username));

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + utilisateur.getRole());
        return new org.springframework.security.core.userdetails.AppUser(utilisateur.getUsername(), utilisateur.getPassword(), Collections.singletonList(authority));
    }
}

 */
