package com.gisfy.inclenJson.Synchronise;

import android.app.ProgressDialog;
import android.app.job.JobScheduler;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.gisfy.inclenJson.PayLoads.Project;
import com.gisfy.inclenJson.PayLoads.ProjectEn.ProjectData;
import com.gisfy.inclenJson.PayLoads.ProjectEn.ProjectResponse;
import com.gisfy.inclenJson.PayLoads.SurveyEn.SurveyFormDatum;
import com.gisfy.inclenJson.PayLoads.SurveyEn.SurveyResponse;
import com.gisfy.inclenJson.RoomDB.AppDatabase;
import com.gisfy.inclenJson.Utiles.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.IDN;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import inclenJson.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetSurveyForms {

    private Context context;
    private ResponseCallBack callBack;
    private int surveyID;

    public GetSurveyForms(Context context, ResponseCallBack callBack, int surveyID) {
        this.context = context;
        this.callBack = callBack;
        this.surveyID = surveyID;
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
            String URL = context.getResources().getString(R.string.getQuesltions) + (surveyID);
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


                JsonArray jsonArray = new JsonObject().getAsJsonArray("survey_form_data");
                JSONObject jsonObject = new JSONObject(responseBody);

                JSONArray jsonArray1 = jsonObject.getJSONArray("survey_form_data");

                List<SurveyFormDatum> formDatumList = new ArrayList<>();
                String ans = null;
                for (int i = 0; i < jsonArray1.length(); i++) {

                    JSONObject jsonObject1 = jsonArray1.getJSONObject(i);

                    SurveyFormDatum surveyFormDatum = new SurveyFormDatum();

                    if (jsonObject1.get("answer") instanceof String) {
                        surveyFormDatum.setSurveyID(surveyID);
                        surveyFormDatum.setAnswer(jsonObject1.getString("answer"));
                        surveyFormDatum.setColumn(jsonObject1.getString("column"));
                        surveyFormDatum.setQuestion(jsonObject1.getString("question"));
                        surveyFormDatum.setQuestion_type(jsonObject1.getString("question_type"));
                        surveyFormDatum.setRequired(jsonObject1.getBoolean("required"));

                    } else if (jsonObject1.get("answer") instanceof JSONObject) {
                        surveyFormDatum.setSurveyID(surveyID);
                        surveyFormDatum.setAnswer(jsonObject1.getJSONObject("answer").toString());
                        surveyFormDatum.setColumn(jsonObject1.getString("column"));
                        surveyFormDatum.setQuestion(jsonObject1.getString("question"));
                        surveyFormDatum.setQuestion_type(jsonObject1.getString("question_type"));
                        surveyFormDatum.setRequired(jsonObject1.getBoolean("required"));
                    }
                    else if (jsonObject1.get("answer").equals(ans)) {
                        surveyFormDatum.setSurveyID(surveyID);
                        surveyFormDatum.setAnswer("");
                        surveyFormDatum.setColumn(jsonObject1.getString("column"));
                        surveyFormDatum.setQuestion(jsonObject1.getString("question"));
                        surveyFormDatum.setQuestion_type(jsonObject1.getString("question_type"));
                        surveyFormDatum.setRequired(jsonObject1.getBoolean("required"));
                    }
                   formDatumList.add(surveyFormDatum);
                }
                if(formDatumList.size()>0){

                    AppDatabase.getObInstance(context).surveyDao().deleteById(surveyID);
                    AppDatabase.getObInstance(context).surveyDao().insertAll(formDatumList);


                    return "data";
                }
                else{
                    return "empty";
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
