package com.priyanshu.hospitalManagement.service;

import com.priyanshu.hospitalManagement.dto.LoginRequestDto;
import com.priyanshu.hospitalManagement.dto.LoginResponseDto;
import com.priyanshu.hospitalManagement.dto.SignupResponseDto;
import com.priyanshu.hospitalManagement.entity.User;
import com.priyanshu.hospitalManagement.repository.UserRepository;
import com.priyanshu.hospitalManagement.security.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public LoginResponseDto login(LoginRequestDto loginRequestDto)
    {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword())
        );
        User user = (User) authentication.getPrincipal();
        String token = authUtil.generateAccessToken(user);
        return new LoginResponseDto(token, user.getId());
    }


    public SignupResponseDto signup(LoginRequestDto signupRequestDto) throws IllegalArgumentException {
       Optional<User> user = userRepository.findByUsername(signupRequestDto.getUsername().describeConstable().orElse(null)
        );
        if(user.isPresent()) {
            throw new IllegalArgumentException("User already exits");
        }
        user = Optional.of(userRepository.save(User.builder()
                .username(signupRequestDto.getUsername())
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .build()
        ));
        return new SignupResponseDto(user.get().getId(), user.get().getUsername());
    }
}
