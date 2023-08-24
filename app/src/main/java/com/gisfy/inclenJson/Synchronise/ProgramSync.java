package com.gisfy.inclenJson.Synchronise;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.gisfy.inclenJson.PayLoads.Project;
import com.gisfy.inclenJson.PayLoads.ProjectEn.ProjectData;
import com.gisfy.inclenJson.PayLoads.ProjectEn.ProjectResponse;
import com.gisfy.inclenJson.RoomDB.AppDatabase;
import com.gisfy.inclenJson.Utiles.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import inclenJson.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProgramSync {

    private Context context;
    private ResponseCallBack callBack;

    public ProgramSync(Context context, ResponseCallBack callBack) {
        this.context = context;
        this.callBack = callBack;
        new LoadTask().execute();
    }

    private class LoadTask extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            SessionManager sessionManager = new SessionManager(context);
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            String URL = context.getResources().getString(R.string.getProjects) + (sessionManager.getLoginPayLoad().getData().getName().getId());
            Log.d("ID_gen: ", sessionManager.getLoginPayLoad().getData().getName().getId()+"");
            Request request = new Request.Builder()
                    .url(URL)
                    .method("GET", null)
                    .addHeader("Authorization", "Bearer " + sessionManager.getLoginPayLoad().getData().getToken())
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();
                Log.i("PROGRAM SYNC URL", URL);
                Log.i("PROGRAM SYNC REQUEST", sessionManager.getLoginPayLoad().getData().getToken() + "");
                Log.i("PROGRAM SYNC RESPONSE", responseBody + "");
                JSONArray array = new JSONArray(responseBody);
                if (array.length() > 0) {
                    Type listType = new TypeToken<ProjectData>(){}.getType();
                    List<ProjectData> project = new ArrayList<>();

                    for (int i = 0; i < array.length(); i++) {

                        ProjectData projectData = new Gson().fromJson(array.getJSONObject(i).getJSONObject("project_data").toString(),listType);
                        project.add(projectData);

                    }

                    new SessionManager(context).setProjectResponse(project);

                    return "data";

                } else {
                    return "ddd";
                }


            } catch (Exception e) {
                e.printStackTrace();
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            callBack.callBack(s);
            Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }
}
