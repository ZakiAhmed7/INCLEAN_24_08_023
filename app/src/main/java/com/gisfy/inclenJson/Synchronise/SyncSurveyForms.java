package com.gisfy.inclenJson.Synchronise;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.android.volley.toolbox.Volley;
import com.gisfy.inclenJson.PayLoads.SurveyEn.SurveyFormDatum;
import com.gisfy.inclenJson.PayLoads.SurveyEn.SurveyFormJsonAnswer;
import com.gisfy.inclenJson.RoomDB.AppDatabase;
import com.gisfy.inclenJson.Utiles.SessionManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import inclenJson.R;
import okhttp3.OkHttpClient;

public class SyncSurveyForms {

    private Context context;
    private ResponseCallBack callBack;

    public SyncSurveyForms(Context context, ResponseCallBack callBack) {
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
            progressDialog.setMessage("Loading...,It may take sometime");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            SessionManager sessionManager = new SessionManager(context);
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            String URL = context.getResources().getString(R.string.getSurvey);

            RequestQueue queue = Volley.newRequestQueue(context);

            for (int i = 0; i < AppDatabase.getObInstance(context).surveyDao().getSurveyToBeSyncCount(); i++) {


                int finalI = i;
                StringRequest MyStringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("response", response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> body = new HashMap<String, String>();

                        List<SurveyFormJsonAnswer> surveyFormJsonAnswerList = AppDatabase.getObInstance(context).surveyDao().getSurveyFormAnswers();
                        SurveyFormJsonAnswer item = surveyFormJsonAnswerList.get(finalI);


                        body.put("surveyid", item.getSurveyId() + "");
                        body.put("baseline", item.getBaseline());
                        body.put("endline", item.getEndline());
                        body.put("baselinetype", item.getBaselinetype());
                        body.put("endlinetype", item.getEndlinetype());

                        Type listType = new TypeToken<List<SurveyFormDatum>>() {
                        }.getType();
                        List<SurveyFormDatum> list = new Gson().fromJson(item.getJsonAnswer(), listType);

                        for (int i = 0; i < list.size(); i++) {
                            body.put(list.get(i).getColumn().toLowerCase(Locale.ROOT), list.get(i).getUserAnswer());
                        }


                        return body;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();

                        headers.put("Authorization", "Bearer" + " " + sessionManager.getLoginPayLoad().getData().getToken());//App.getToken()
                        return headers;

                    }
                };


                queue.add(MyStringRequest);
            }
            return "";
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
