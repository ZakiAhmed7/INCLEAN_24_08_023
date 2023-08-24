package com.gisfy.inclenJson.PayLoads.SurveyEn;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SurveyFormJsonAnswer {
    String jsonAnswer;
    boolean isSynced = false;

    String baseline;
    String endline;
    String baselinetype;
    String endlinetype;
    int surveyId;

    @PrimaryKey(autoGenerate = true)
    int id;


    public String getJsonAnswer() {
        return jsonAnswer;
    }

    public void setJsonAnswer(String jsonAnswer) {
        this.jsonAnswer = jsonAnswer;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean synced) {
        isSynced = synced;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBaseline() {
        return baseline;
    }

    public void setBaseline(String baseline) {
        this.baseline = baseline;
    }

    public String getEndline() {
        return endline;
    }

    public void setEndline(String endline) {
        this.endline = endline;
    }

    public String getBaselinetype() {
        return baselinetype;
    }

    public void setBaselinetype(String baselinetype) {
        this.baselinetype = baselinetype;
    }

    public String getEndlinetype() {
        return endlinetype;
    }

    public void setEndlinetype(String endlinetype) {
        this.endlinetype = endlinetype;
    }

    public int getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(int surveyId) {
        this.surveyId = surveyId;
    }
}
