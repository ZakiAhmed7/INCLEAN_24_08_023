package com.gisfy.inclenJson.PayLoads.SurveyEn;

import java.util.ArrayList;

public class SurveyResponse {
    public SurveyData survey_data;
    public ArrayList<SurveyFormDatum> survey_form_data;

    public SurveyData getSurvey_data() {
        return survey_data;
    }

    public void setSurvey_data(SurveyData survey_data) {
        this.survey_data = survey_data;
    }

    public ArrayList<SurveyFormDatum> getSurvey_form_data() {
        return survey_form_data;
    }

    public void setSurvey_form_data(ArrayList<SurveyFormDatum> survey_form_data) {
        this.survey_form_data = survey_form_data;
    }
}
