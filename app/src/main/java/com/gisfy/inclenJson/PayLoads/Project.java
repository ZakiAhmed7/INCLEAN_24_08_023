package com.gisfy.inclenJson.PayLoads;

import android.os.Parcel;
import android.os.Parcelable;

public class Project implements Parcelable {
    private String projectName;
    private String surveyId;
    private String surveyName;
    private String mapPlotType;
    private String lineType;
    private String plottedData;

    protected Project(Parcel in) {
        projectName = in.readString();
        surveyId = in.readString();
        surveyName = in.readString();
        mapPlotType = in.readString();
        lineType = in.readString();
        plottedData = in.readString();
    }

    public Project(){

    }
    public static final Creator<Project> CREATOR = new Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }
    };

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

    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }

    public String getMapPlotType() {
        return mapPlotType;
    }

    public void setMapPlotType(String mapPlotType) {
        this.mapPlotType = mapPlotType;
    }

    public String getLineType() {
        return lineType;
    }

    public void setLineType(String lineType) {
        this.lineType = lineType;
    }

    public String getPlottedData() {
        return plottedData;
    }

    public void setPlottedData(String plottedData) {
        this.plottedData = plottedData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.surveyId,
                this.surveyName,
                this.mapPlotType,
                this.lineType,
                this.plottedData});
    }
}
