package com.example.AffairsManagementApp.security;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    @Bean
    public UserDetailsService userDetailsService(){
        PasswordEncoder passwordEncoder = passwordEncoder(); // we use a password encoder
        List<UserDetails> users = new ArrayList<>();
        UserDetails user1 = User.withUsername("mouad")
                                .password(passwordEncoder.encode("123"))
                                .authorities("ADMIN")
                                .build();
        UserDetails user2 = User.withUsername("sarah")
                                .password(passwordEncoder.encode("123"))
                                .authorities("ADMIN")
                                .build();
        users.add(user1);
        users.add(user2);
        return new InMemoryUserDetailsManager(users);
        // "InMemoryUserDetailsManager" implements "UserDetailsService"
        // "UserDetailsService" interface has a method called loadUserByUsername(String username)
        // this method is implemented in "InMemoryUserDetailsManager" and it fetches for a user by its username
        // "InMemoryUserDetailsManager" has a method called "createUser" too
        // "InMemoryUserDetailsManager"'s constructor takes in an array of type "UserDetails"
        // each element in this array is put into "createUser" and added to memory
        // "loadUserByUsername(String username)" loads a user from the database or memory
    } // now these users are available for authentication

    @Bean
    public PasswordEncoder passwordEncoder(){ // 2
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception { // 3

        return httpSecurity
                .sessionManagement(sm->
                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // each request must provide its own authentication token
                .csrf(AbstractHttpConfigurer::disable)
                //.httpBasic(Customizer.withDefaults())
                .oauth2ResourceServer(oa->
                        oa.jwt(Customizer.withDefaults())) /*This part ensures that the JWT token is validated. This involves checking the token’s signature, expiration, and claims.*/
                .authorizeHttpRequests(ar->
                        ar.requestMatchers("/auth/login/**").permitAll())
                .authorizeHttpRequests(ar->
                        ar.anyRequest().authenticated()) /*This step ensures that the request is authenticated. The filter checks if the JWT token is valid and if it has been authenticated by the OAuth2ResourceServer configuration.*/
                .build();
    }

    @Bean // 4
    JwtEncoder jwtEncoder(){
        String secretKey = "9fueyrutjghfbu879uhdjfheukqhnbch0jhdbcndbdhrkshfbcng0345tytrui56";
        return new NimbusJwtEncoder(new ImmutableSecret<>(secretKey.getBytes()));
    } // this encoder will be used to generate a token we only need to pass claims "JwtClaimsSet claims" and then encode them using the secret key
    @Bean // 5
    JwtDecoder jwtDecoder(){
        String secretKey = "9fueyrutjghfbu879uhdjfheukqhnbch0jhdbcndbdhrkshfbcng0345tytrui56"; // the same key is needed to decode the token
        SecretKeySpec secretKeySpec =  new SecretKeySpec(secretKey.getBytes(), "RSA");
        return NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS512).build(); // the decoded uses macalgorith HS512
    }
    @Bean
    // first we inject an object of type UserDetailsService
    // because the userdetailsservice is an interface used to retrieve users by username
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        // here we set the password encoder
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        // here we set from where we should load registered users
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return new ProviderManager(daoAuthenticationProvider);
    }
}
