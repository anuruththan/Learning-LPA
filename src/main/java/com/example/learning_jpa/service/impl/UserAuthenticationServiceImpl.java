package com.example.learning_jpa.service.impl;

import com.example.learning_jpa.dto.GeneralResponseDto;
import com.example.learning_jpa.dto.request.UserLoginDto;
import com.example.learning_jpa.dto.request.UserSignUp;
import com.example.learning_jpa.entity.User;
import com.example.learning_jpa.enums.Roles;
import com.example.learning_jpa.repository.UserAuthRepository;
import com.example.learning_jpa.service.UserAuthenticationService;
import com.example.learning_jpa.service.result.AuthResult;
import com.example.learning_jpa.util.AccessJwtUtil;
import com.example.learning_jpa.util.HashUtil;
import com.example.learning_jpa.util.RefreshJwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

    @Autowired
    private AccessJwtUtil accessJwtUtil;

    @Autowired
    private RefreshJwtUtil refreshJwtUtil;

    @Autowired
    private UserAuthRepository userAuthRepository;

    @Autowired
    private HashUtil hashUtil;

    public GeneralResponseDto generalResponse;

    @Override
    public AuthResult signUp(UserSignUp userSignUp) {

        generalResponse = new GeneralResponseDto();

        if (!Roles.isValidRole(userSignUp.getRoles())) {
            generalResponse.setData("Invalid Role");
            generalResponse.setMsg("Enter Valid Roles");
            return new AuthResult(generalResponse, null, null, null, null);
        }

        if (userAuthRepository.existsByEmail(userSignUp.getEmail())) {
            generalResponse.setData("Already Exists Email");
            generalResponse.setMsg("Enter Valid New Email");
            return new AuthResult(generalResponse, null, null, null, null);
        }
        try {
            User user = new User();
            user.setFirstName(userSignUp.getFirstName());
            user.setLastName(userSignUp.getLastName());
            user.setEmail(userSignUp.getEmail());
            user.setPassword(hashUtil.hashPwd(userSignUp.getPassword()));
            user.setMobileNumber(userSignUp.getMobileNumber());
            user.setRoles(userSignUp.getRoles());

            generalResponse.setData("User has been created");
            generalResponse.setRes(true);
            generalResponse.setStatusCode(200);
            generalResponse.setMsg("User has been created");
            userAuthRepository.save(user);

            String accessToken = accessJwtUtil.generateToken(userSignUp.getEmail(), userSignUp.getRoles());
            String refreshToken  = refreshJwtUtil.generateToken(userSignUp.getEmail(), userSignUp.getRoles());

            return new AuthResult(generalResponse, user.getEmail(), userSignUp.getRoles(), accessToken, refreshToken);
        } catch (Exception e) {
            generalResponse.setMsg(e.getMessage());
            generalResponse.setStatusCode(500);
            return new AuthResult(generalResponse, null, null, null, null);
        }
    }

    @Override
    public AuthResult login(UserLoginDto userLoginDto) {

        generalResponse = new GeneralResponseDto();

        try {

            User user = userAuthRepository.findByEmail(userLoginDto.getEmail()).orElse(null);

            if (user == null) {
                generalResponse.setData("Invalid Email");
                generalResponse.setRes(false);
                generalResponse.setMsg("Email not found. Please check and try again.");
                generalResponse.setStatusCode(404);
                return new AuthResult(generalResponse, null, null, null, null);
            }

            if (!user.getPassword().equals(hashUtil.hashPwd(userLoginDto.getPassword()))) {
                generalResponse.setData("Invalid Password");
                generalResponse.setRes(false);
                generalResponse.setMsg("Incorrect password. Please try again.");
                generalResponse.setStatusCode(401);
                return new AuthResult(generalResponse, null, null, null, null);
            }


            generalResponse.setData("User login successful");
            generalResponse.setRes(true);
            generalResponse.setMsg("Login Successful");
            generalResponse.setStatusCode(200);

            String accessToken = accessJwtUtil.generateToken(userLoginDto.getEmail(), user.getRoles());
            String refreshToken  = refreshJwtUtil.generateToken(userLoginDto.getEmail(), user.getRoles());

            return new AuthResult(generalResponse, user.getEmail(), user.getRoles(), accessToken, refreshToken);

        } catch (Exception e) {
            generalResponse.setMsg(e.getMessage());
            generalResponse.setStatusCode(500);
            return new AuthResult(generalResponse, null, null, null, null);
        }

    }

    @Override
    public AuthResult refreshToken(String accessToken,String refreshToken){
        generalResponse = new GeneralResponseDto();
        try {

            if(!refreshJwtUtil.validateToken(refreshToken)){
                generalResponse.setData("Invalid Token");
                generalResponse.setStatusCode(401);
                generalResponse.setMsg("Refresh Token has been expired");
                return new AuthResult(generalResponse, null, null, null, null);
            }


            // Validate Refresh Token
            String email = refreshJwtUtil.extractEmail(refreshToken);
            Roles role = Roles.valueOf(refreshJwtUtil.extractRole(refreshToken));

            if (email != null) {
                // Generate New Tokens
                String newAccessToken = accessJwtUtil.generateToken(email, role);
                String newRefreshToken = refreshJwtUtil.generateToken(email, role);

                generalResponse.setRes(true);
                generalResponse.setStatusCode(200);
                generalResponse.setMsg("Refresh token generated successfully.");

                return new AuthResult(generalResponse, email, role, newAccessToken, newRefreshToken);
            }
        } catch (ExpiredJwtException e) {
            generalResponse.setRes(false);
            generalResponse.setMsg("Refresh token expired. Please log in again.");
            generalResponse.setStatusCode(401);
        } catch (Exception e) {
            generalResponse.setRes(false);
            generalResponse.setMsg("Invalid refresh token.");
            generalResponse.setStatusCode(401);
        }
        return new AuthResult(generalResponse, null, null, null, null);
    }


}
