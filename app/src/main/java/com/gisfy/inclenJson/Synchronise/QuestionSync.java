package com.gisfy.inclenJson.Synchronise;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.gisfy.inclenJson.Entity.Question;
import com.gisfy.inclenJson.PayLoads.Questions.Questionnaire;
import inclenJson.R;
import com.gisfy.inclenJson.RoomDB.AppDatabase;
import com.gisfy.inclenJson.RoomDB.Dao.QuestionDao;
import com.gisfy.inclenJson.Utiles.FormBuilder;
import com.gisfy.inclenJson.Utiles.SessionManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class QuestionSync {

    private Context context;
    private ResponseCallBack callBack;
    private String projectId;
    private QuestionDao dao;
    private String projectName;
    public QuestionSync(Context context,String projectId,String projectName, ResponseCallBack callBack){
        this.context = context;
        this.callBack = callBack;
        this.projectId = projectId;
        this.projectName = projectName;
        dao=AppDatabase.getObInstance(context).questionDao();
        new LoadTask().execute();
    }

    private class LoadTask extends AsyncTask<String,String,String>{

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
            String URL = context.getResources().getString(R.string.getQuesltions)+(projectId);
            Request request = new Request.Builder()
                    .url(URL)
                    .method("GET", null)
                    .addHeader("Authorization", "Bearer "+sessionManager.getLoginPayLoad().getData().getToken())
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();
                Log.i("PROGRAM SYNC URL", URL);
                Log.i("PROGRAM SYNC REQUEST", sessionManager.getLoginPayLoad().getData().getToken()+"");
                Log.i("PROGRAM SYNC RESPONSE", responseBody+"");
                if (responseBody.contains("success") && responseBody.contains("true")) {
                    responseBody=responseBody.replace("{\"success\":true,\"data\":","");
                    responseBody=responseBody.replace(",\"message\":\"Posts fetched.\"}","");
                    List<Questionnaire> posts = (List<Questionnaire>) new Gson().fromJson(responseBody,
                            new TypeToken<List<Questionnaire>>() {
                            }.getType());
                    FormBuilder builder = new FormBuilder(context);
                    for (Questionnaire questionnaire:posts){
                        builder.addData(questionnaire);
                    }
                    Question question = new Question();
                    question.setSurveyID(projectId);
                    question.setJsonData(builder.getForm(projectName).toString());
                    AppDatabase.getObInstance(context).questionDao().insert(question);
                    return "Data loaded to local";
                }else
                    return "Failed to load data";

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
