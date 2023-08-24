package com.gisfy.inclenJson.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.se.omapi.Session;
import android.widget.Toast;

import com.gisfy.inclenJson.Adapters.SurveyFormRecyclerAdapter;
import com.gisfy.inclenJson.PayLoads.SurveyEn.SurveyFormDatum;
import com.gisfy.inclenJson.PayLoads.SurveyEn.SurveyFormJsonAnswer;
import com.gisfy.inclenJson.RoomDB.AppDatabase;
import com.gisfy.inclenJson.Synchronise.ResponseCallBack;
import com.gisfy.inclenJson.Synchronise.GetSurveyForms;
import com.gisfy.inclenJson.Utiles.SessionManager;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import inclenJson.R;

public class SurveyFormActivity extends AppCompatActivity {

    @BindView(R.id.recycler)
    RecyclerView recyclerView;


    SurveyFormRecyclerAdapter surveyFormRecyclerAdapter;
    int surveyId = 0;

    boolean isBaseline = false;
    boolean isEndline = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_form);
        ButterKnife.bind(this);

        if (getIntent().getExtras() != null) {
            surveyId = getIntent().getExtras().getInt("surveyID");

            if (getIntent().getExtras().getString("baseline_json") != null) {
                isBaseline = true;

            }

            if (getIntent().getExtras().getString("endline_json") != null) {
                isEndline = true;

            }

        }

        setRecyclerView();
        updateForm();
    }

    private void updateForm() {
        new GetSurveyForms(SurveyFormActivity.this, new ResponseCallBack() {
            @Override
            public void callBack(String response) {
                if (response.equals("data")) {

                    List<SurveyFormDatum> list = AppDatabase.getObInstance(getApplicationContext()).surveyDao().getAll(surveyId);

                    surveyFormRecyclerAdapter.updateList(list);
                }
            }
        }, surveyId);
    }

    private void setRecyclerView() {
        surveyFormRecyclerAdapter = new SurveyFormRecyclerAdapter(SurveyFormActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        recyclerView.setAdapter(surveyFormRecyclerAdapter);

    }

    @OnClick(R.id.back)
    public void onClickBack() {
        onBackPressed();
    }

    @OnClick(R.id.submit)
    public void onClickSubmit() {
        SessionManager sessionManager = new SessionManager(SurveyFormActivity.this);
        List<SurveyFormDatum> list = AppDatabase.getObInstance(SurveyFormActivity.this).surveyDao().getAll(surveyId);

        for (int i = 0; i < list.size(); i++) {

            if (list.get(i).getUserAnswer() == null) {
                Toast.makeText(getApplicationContext(), "Question no: " + (i + 1) + " answer is empty", Toast.LENGTH_LONG).show();
                return;
            }
        }

        String baseline = "";
        String endline = "";
        String baseLineType = "";
        String endLineType = "";


            baseline = sessionManager.getSharedPreferences().getString("baseline_json","");
            baseLineType = sessionManager.getSharedPreferences().getString("plotType","");



            endline = sessionManager.getSharedPreferences().getString("endline_json","");
            endLineType = sessionManager.getSharedPreferences().getString("plotType","");


        SurveyFormJsonAnswer surveyFormJsonAnswer = new SurveyFormJsonAnswer();

        surveyFormJsonAnswer.setJsonAnswer(new Gson().toJson(list));
        surveyFormJsonAnswer.setSynced(false);
        surveyFormJsonAnswer.setBaseline(baseline);
        surveyFormJsonAnswer.setEndline(endline);
        surveyFormJsonAnswer.setBaselinetype(baseLineType);
        surveyFormJsonAnswer.setEndlinetype(endLineType);
        surveyFormJsonAnswer.setSurveyId(surveyId);

        AppDatabase.getObInstance(SurveyFormActivity.this).surveyDao().insertJsonAnswersForSync(surveyFormJsonAnswer);


        Intent intent = new Intent(SurveyFormActivity.this, HomeActivityDashboard.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        Toast.makeText(getApplicationContext(), "Survey Form Saved Successfully", Toast.LENGTH_LONG).show();

    }
}