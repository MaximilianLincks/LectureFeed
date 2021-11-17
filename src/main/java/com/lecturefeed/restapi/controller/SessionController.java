package com.lecturefeed.restapi.controller;

import com.lecturefeed.authentication.jwt.CustomAuthenticationService;
import com.lecturefeed.model.*;
import com.lecturefeed.session.*;
import com.lecturefeed.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Service
@RequiredArgsConstructor
public class SessionController {

    private final CustomAuthenticationService customAuthenticationService;
    private final SessionManager sessionManager;

    @GetMapping("/session/create")
    public Map<String,Object> createNewSession(@RequestParam TokenModel token) {

        if (UserRole.ADMINISTRATOR.getRole().equals(TokenUtils.getTokenValue(customAuthenticationService,"role",token))) {
           Session session = sessionManager.createSession();

            Map<String,Object> sessionInformation = new HashMap<>();
            sessionInformation.put("id",session.getId());
            sessionInformation.put("sessionCode",session.getSessionCode());
            return sessionInformation;
        }
        throw new BadCredentialsException(String.format("No admin rights for token %s", token.getToken()));
    }
}
