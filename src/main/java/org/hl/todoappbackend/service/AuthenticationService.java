package org.hl.todoappbackend.service;

import org.hl.todoappbackend.dto.AuthenticationCredentialsRequest;
import org.hl.todoappbackend.dto.AuthenticationResponse;
import org.hl.todoappbackend.entity.Authority;
import org.hl.todoappbackend.entity.User;
import org.hl.todoappbackend.repository.AuthorityRepository;
import org.hl.todoappbackend.repository.UserRepository;
import org.hl.todoappbackend.security.jwt.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationService.class);

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(JwtUtil jwtUtil,
                                 UserRepository userRepository,
                                 AuthorityRepository authorityRepository,
                                 PasswordEncoder passwordEncoder,
                                 AuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }


    public AuthenticationResponse registerUser(AuthenticationCredentialsRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Authority authority = findRoleUser();
        authority.getUsers().add(user);
        user.getAuthorities().add(authority);
        userRepository.save(user);

        String jwtToken = jwtUtil.generateToken(user);
        return new AuthenticationResponse(user.getUsername(), jwtToken);
    }

    public AuthenticationResponse login(AuthenticationCredentialsRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findById(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid credentials"));
        String jwtToken = jwtUtil.generateToken(user);
        return new AuthenticationResponse(user.getUsername(), jwtToken);
    }

    private Authority findRoleUser() {
        Optional<Authority> roleUser = authorityRepository.findById("ROLE_USER");
        if(roleUser.isPresent()) {
            return roleUser.get();
        }
        LOG.debug("There is no ROLE_USER in database! Add one to solve problem.");
        throw new RuntimeException("There is no ROLE_USER");
    }

}
