package com.gisfy.inclenJson.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Answers")
public class Answer {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String jsonData;
    private boolean synced;
    private boolean partialSurvey;
    private String projectName;
    private String surveyName;
    private String surveyId;
    private String lineType;
    private String plotType;
    private String plottedData;
    private String createdDate;


    public boolean isPartialSurvey() {
        return partialSurvey;
    }

    public void setPartialSurvey(boolean partialSurvey) {
        this.partialSurvey = partialSurvey;
    }

    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }

    public String getLineType() {
        return lineType;
    }

    public void setLineType(String lineType) {
        this.lineType = lineType;
    }

    public String getPlotType() {
        return plotType;
    }

    public void setPlotType(String plotType) {
        this.plotType = plotType;
    }

    public String getPlottedData() {
        return plottedData;
    }

    public void setPlottedData(String plottedData) {
        this.plottedData = plottedData;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }
}
