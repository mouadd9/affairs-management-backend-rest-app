package com.example.AffairsManagementApp.security;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig  {

   //------------ we inject the customised UserDetailsService (that has the loadUserByUsername method)
    public CustomUserDetailsService customUserDetailsService;
    private final RsaKeyProperties rsaKeys;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService, RsaKeyProperties rsaKeys){
        this.customUserDetailsService = customUserDetailsService;
        this.rsaKeys = rsaKeys;
    }


    @Bean
    public PasswordEncoder passwordEncoder(){ // 2
        return new BCryptPasswordEncoder();
    }


    //------------ this object will be used to authenticate the user
    //------------ it uses the customUserDetailsService to load the user, and it uses the password encryption algorithm to check the password
    //------------ this class is used in the auth controller (we use the method authenticate provided by this class)
    @Bean
    public AuthenticationManager authenticationManager(){

        PasswordEncoder passwordEncoder = passwordEncoder();

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);

        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);

        return new ProviderManager(daoAuthenticationProvider);

    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { // 3
        http
                .sessionManagement(sm->
                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // each request must provide its own authentication token
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                //.httpBasic(Customizer.withDefaults())
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
                .oauth2ResourceServer(oa-> oa.jwt(Customizer.withDefaults())) /*This part ensures that the JWT token is validated. This involves checking the token’s signature, expiration, and claims.*/
                .authorizeHttpRequests(ar-> ar.requestMatchers("/api/auth/**").permitAll()
                                              .requestMatchers("/h2-console/**").permitAll()
                                              .anyRequest().authenticated());/*This step ensures that the request is authenticated. The filter checks if the JWT token is valid and if it has been authenticated by the OAuth2ResourceServer configuration.*/
        return http.build();
    }


    // symmetric algorithm
    /*
    @Bean // 4
    JwtEncoder jwtEncoder(){

        String secretKey = "9fueyrutjghfbu879uhdjfheukqhnbch0jhdbcndbdhrkshfbcng0345tytrui56";

        return new NimbusJwtEncoder(new ImmutableSecret<>(secretKey.getBytes()));

    }

    @Bean // 5
    JwtDecoder jwtDecoder(){

        String secretKey = "9fueyrutjghfbu879uhdjfheukqhnbch0jhdbcndbdhrkshfbcng0345tytrui56";

        SecretKeySpec secretKeySpec =  new SecretKeySpec(secretKey.getBytes(), "RSA");

        return NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS512).build();
    }


     */


    // asymmetric algorithm
    @Bean
    JwtDecoder jwtDecoder(){
        return NimbusJwtDecoder.withPublicKey(rsaKeys.publicKey()).build();
    }

    @Bean
    JwtEncoder jwtEncoder(){
        JWK jwk = new RSAKey.Builder(rsaKeys.publicKey()).privateKey(rsaKeys.privateKey()).build();
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSource);


    }


    // handling requests coming from browsers with CORS
    @Bean
    CorsConfigurationSource corsConfigurationSource() {


        /*Cross-Origin Resource Sharing (CORS) is a security
         feature that allows browser-based requests using AJAX.
         CORS helps keep web interactions secure while allowing
         necessary communication between different websites.*/


        CorsConfiguration corsConfiguration = new CorsConfiguration(); // a class provided by Spring that holds the CORS configuration settings
        // we will allow all origins to access our apis
        corsConfiguration.addAllowedOrigin("*");
        // This allows all HTTP methods (GET, POST, PUT, DELETE, etc.) from any origin to be executed.
        corsConfiguration.addAllowedMethod("*");
        // This allows any HTTP header to be included in the request from the client.
        corsConfiguration.addAllowedHeader("*");

        // corsConfiguration.setExposedHeaders(List.of("x-auth-token"));
         UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }




}

