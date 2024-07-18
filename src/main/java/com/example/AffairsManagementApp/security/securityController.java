package com.example.AffairsManagementApp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class securityController {
    @Autowired // we will authenticate using this object
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtEncoder jwtEncoder; // this injection will help us to encode our token (sign)
    // this method will get a username and a password to authenticate a user
    // once authenticate the method will return a JSON token
    @PostMapping("/login")
    public Map<String, String> login(String username, String password){
        // now we should use the authentication manager to authenticate by passing the user info
        // this manager will use the userdetailservice to load users by username of they exist
        Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(username, password)
        );
        // now we need to generate the JWT
        // we define the token's claims
        Instant instant = Instant.now();
        // now we should get the authorities of our user
        String scope = authentication.getAuthorities().stream().map(a->a.getAuthority()).collect(Collectors.joining(" "));
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuedAt(instant)
                .expiresAt(instant.plus(10, ChronoUnit.MINUTES))
                .subject(username)
                .claim("scope",scope ) // roles
                .build();

        JwtEncoderParameters jwtEncoderParameters =
                JwtEncoderParameters.from(
                  // the algo we use to encode
                  JwsHeader.with(MacAlgorithm.HS512).build(), // Header
                  jwtClaimsSet // claims
                );
        String jwt = jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
        return Map.of("access-token", jwt);
    }

// overview
    // this Post "/login" endpoint receives a http request that has a username and a password
    // then it authenticate the user
    // of the user exists it will generate a token for him,
    // we specify some token claims like roles that predefined for existing users
    // then we encode the jwt using a hash algo
    // then we return the token as string
}
