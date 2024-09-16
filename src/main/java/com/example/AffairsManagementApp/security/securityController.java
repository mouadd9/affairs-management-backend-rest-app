package com.example.AffairsManagementApp.security;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
@AllArgsConstructor
@RequestMapping("/api/auth")
public class securityController {

    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginDetails loginDetails){

        // this authentication manager uses the User repository to check if the user with these credentials exist
        Authentication authentication = authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(loginDetails.getUsername(), loginDetails.getPassword()));

        Instant instant = Instant.now();

        String scope = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));

        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(instant)
                .expiresAt(instant.plus(8, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim("scope",scope )
                .build();

       /* JwtEncoderParameters jwtEncoderParameters =
                JwtEncoderParameters.from(
                        JwsHeader.with(MacAlgorithm.HS512).build(),
                        jwtClaimsSet
                );*/

        String jwt = this.jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();

        return Map.of("access-token", jwt);
    }

// overview
    // this Post "/login" endpoint receives a http request that has a username and a password
    // then it authenticate the user
    // if the user exists it will generate a token for him,
    // we specify some token claims like roles that predefined for existing users
    // then we encode the jwt using a hash algo
    // then we return the token as string
}