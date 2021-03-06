package com.lecturefeed.model.survey;

import com.lecturefeed.entity.model.survey.Survey;
import com.lecturefeed.manager.SurveyManager;
import com.lecturefeed.socket.controller.service.SurveyService;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@RequiredArgsConstructor
public class SurveyTimer extends Thread{
    private final int sessionId;
    private final int surveyId;
    private final SurveyService surveyService;
    private final SurveyManager surveyManager;

    @Override
    public void run() {
        try {
            Survey survey = surveyManager.getSurveyById(surveyId);
            sleep(survey.getTemplate().getDuration() * 1000L);
            survey = surveyManager.getSurveyById(surveyId);
            survey.setTimestamp(new Date().getTime());
            surveyManager.updateSurvey(survey);
            surveyService.onClose(sessionId, survey.getId());
            surveyService.onResult(sessionId, survey);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        surveyManager.closeSurvey(sessionId);
    }
}
