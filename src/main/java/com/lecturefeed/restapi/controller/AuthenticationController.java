package com.lecturefeed.restapi.controller;

import com.lecturefeed.authentication.jwt.CustomAuthenticationService;
import com.lecturefeed.model.ParticipantAuthRequestModel;
import com.lecturefeed.model.TokenModel;
import com.lecturefeed.model.UserRole;
import com.lecturefeed.session.Participant;
import com.lecturefeed.session.SessionManager;
import com.lecturefeed.utils.TokenUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Getter
    private final SessionManager sessionManager;

    @Getter
    private final CustomAuthenticationService customAuthenticationService;

    @GetMapping("/admin")
    public TokenModel adminAuth() {
        //create and return token
        return TokenUtils.createAdminToken(customAuthenticationService);

    }

    @PostMapping("/participant")
    public Object participantAuth(@RequestBody ParticipantAuthRequestModel authRequestModel) {
        if(!sessionManager.isCorrectSessionCode(authRequestModel.getSessionId(),authRequestModel.getSessionCode())) return null;

        //create token
        TokenModel tokenModel = TokenUtils.createParticipantToken(customAuthenticationService,sessionManager,authRequestModel.getNickname(), UserRole.PARTICIPANT, authRequestModel.getSessionId());

        //Add new Participant to session
        sessionManager.getSession(authRequestModel.getSessionId()).
                ifPresent(s->s.addParticipant(new Participant(tokenModel.getUserId(), authRequestModel.getNickname())));
        return tokenModel;
    }
}
