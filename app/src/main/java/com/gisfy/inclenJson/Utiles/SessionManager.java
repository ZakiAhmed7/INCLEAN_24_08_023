package com.gisfy.inclenJson.Utiles;

import android.content.Context;
import android.content.SharedPreferences;

import com.gisfy.inclenJson.PayLoads.LoginResponse;
import com.gisfy.inclenJson.PayLoads.ProjectEn.ProjectData;
import com.gisfy.inclenJson.PayLoads.ProjectEn.ProjectResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.internal.Utils;

public class SessionManager {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private final String PREF_NAME = "UserPref";


    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean("IsLoggedIn", false);
    }

    public void createALoginSession(String response) {
        editor.putBoolean("IsLoggedIn", true).commit();
        editor.putString("response", response).commit();

    }

    public LoginResponse getLoginPayLoad() {
        return new Gson().fromJson(sharedPreferences.getString("response", ""), LoginResponse.class);
    }

    public long getId() {
        return sharedPreferences.getLong("Id", -1);
    }

    public String getUserRoleid() {
        return sharedPreferences.getString("UserRoleid", "");
    }

    public String getFirstName() {
        return sharedPreferences.getString("FirstName", "");
    }


    public void endSession() {
        editor.clear();
        editor.apply();
    }


    public static final String PREF_PROJECT_RESPONSE = "pref_active_project";

    private static List<ProjectData> projectResponse;


    public List<ProjectData> getProjectResponse() {
        if (projectResponse != null) {
            //Timber.d("%s - Returned active user", TAG);
            return projectResponse;
        } else {
            String json = sharedPreferences.getString(PREF_PROJECT_RESPONSE, "");
            if (json.isEmpty() || "null".equals(json)) {
                //Timber.d("%s - Returned null", TAG);
                return null;
            } else {
                Type listType = new TypeToken<Collection<ProjectData>>(){}.getType();

                projectResponse = new Gson().fromJson(json, listType);
                //Timber.d("%s - Returned active user from memory: %s", TAG, activeUser.toString());
                return projectResponse;
            }
        }
    }

    public void setProjectResponse(List<ProjectData> projectResponse) {

        SessionManager.projectResponse = projectResponse;

        String json = new Gson().toJson(SessionManager.projectResponse);
        editor.putString(PREF_PROJECT_RESPONSE, json);
        editor.apply();

    }




}
