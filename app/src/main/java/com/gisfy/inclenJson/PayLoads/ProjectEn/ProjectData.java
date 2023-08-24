package com.gisfy.inclenJson.PayLoads.ProjectEn;

import java.util.ArrayList;

public class ProjectData {

    public int id;
    public String projectname;
    public String startdate;
    public String enddate;
    public int category_id;
    public String description;
    public ArrayList<ProjectSurvey> project_survey;
    public ArrayList<ProjectUser> project_user;
    public ProjectCategories project_categories;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<ProjectSurvey> getProject_survey() {
        return project_survey;
    }

    public void setProject_survey(ArrayList<ProjectSurvey> project_survey) {
        this.project_survey = project_survey;
    }

    public ArrayList<ProjectUser> getProject_user() {
        return project_user;
    }

    public void setProject_user(ArrayList<ProjectUser> project_user) {
        this.project_user = project_user;
    }

    public ProjectCategories getProject_categories() {
        return project_categories;
    }

    public void setProject_categories(ProjectCategories project_categories) {
        this.project_categories = project_categories;
    }
}
