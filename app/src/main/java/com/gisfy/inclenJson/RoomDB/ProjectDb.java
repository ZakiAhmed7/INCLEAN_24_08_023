package com.gisfy.inclenJson.RoomDB;

import com.google.gson.annotations.SerializedName;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ProjectDb {
    @PrimaryKey( autoGenerate = true )
    int ProjectId;
    @SerializedName("ProjectName")
    @ColumnInfo(name = "ProjectName")
    String ProjectName;
    @ColumnInfo(name = "Date")
    String Date;

    public ProjectDb() {
    }

    public ProjectDb(int projectId, String projectName, String date) {
        ProjectId = projectId;
        ProjectName = projectName;
        Date = date;
    }

    public ProjectDb(String projectName, String date) {
        ProjectName = projectName;
        Date = date;
    }

    public String getProjectName() {
        return ProjectName;
    }

    public int getProjectId() {
        return ProjectId;
    }

    public void setProjectId(int projectId) {
        ProjectId = projectId;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}

