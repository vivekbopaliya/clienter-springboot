package com.clienter.clienterbackend.controller;

import com.clienter.clienterbackend.config.request.SigninRequest;
import com.clienter.clienterbackend.config.request.SignupRequest;
import com.clienter.clienterbackend.config.response.AuthenticationResponse;
import com.clienter.clienterbackend.entity.UserEntity;
import com.clienter.clienterbackend.service.FileService;
import com.clienter.clienterbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {


    @Autowired
    private UserService service;
    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> signup(@RequestBody SignupRequest request){
        return ResponseEntity.ok(service.signup(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthenticationResponse> signin(@RequestBody SigninRequest request){
        return ResponseEntity.ok(service.signin(request));
    }
}
