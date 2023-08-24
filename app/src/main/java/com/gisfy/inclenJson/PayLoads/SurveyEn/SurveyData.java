package com.gisfy.inclenJson.PayLoads.SurveyEn;

public class SurveyData {
    public int id;
    public String surveyname;
    public String description;
    public int surveytype_id;
    public SurveyType survey_type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSurveyname() {
        return surveyname;
    }

    public void setSurveyname(String surveyname) {
        this.surveyname = surveyname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSurveytype_id() {
        return surveytype_id;
    }

    public void setSurveytype_id(int surveytype_id) {
        this.surveytype_id = surveytype_id;
    }

    public SurveyType getSurvey_type() {
        return survey_type;
    }

    public void setSurvey_type(SurveyType survey_type) {
        this.survey_type = survey_type;
    }
}
