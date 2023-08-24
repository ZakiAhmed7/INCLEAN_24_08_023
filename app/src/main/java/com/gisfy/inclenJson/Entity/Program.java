package com.gisfy.inclenJson.Entity;

import com.google.gson.annotations.SerializedName;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Program")
public class Program {
    @PrimaryKey
    @SerializedName("id")
    private int projectGeneratedId;
    @SerializedName("projectname")
    private String projectName;
    @SerializedName("startdate")
    private String startDate;
    @SerializedName("enddate")
    private String endDate;
    @SerializedName("category")
    private String category;
    @SerializedName("survey_id")
    private String surveyId;
    @SerializedName("surveyname")
    private String surveyName;
    @SerializedName("description")
    private String description;

    public Program() {
    }

    public int getProjectGeneratedId() {
        return projectGeneratedId;
    }

    public void setProjectGeneratedId(int projectGeneratedId) {
        this.projectGeneratedId = projectGeneratedId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
