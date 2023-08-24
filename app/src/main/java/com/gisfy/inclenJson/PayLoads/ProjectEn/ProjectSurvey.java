package com.gisfy.inclenJson.PayLoads.ProjectEn;

public class ProjectSurvey {

    public int id;
    public String surveyname;
    public String description;
    public int surveytype_id;
    public String created_at;
    public String updated_at;
    public int right_id;
    public int order;

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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getRight_id() {
        return right_id;
    }

    public void setRight_id(int right_id) {
        this.right_id = right_id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return surveyname;
    }
}
