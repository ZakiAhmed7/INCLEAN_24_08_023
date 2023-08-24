package com.gisfy.inclenJson.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import inclenJson.R;

import com.gisfy.inclenJson.Adapters.AnswerAdapter;
import com.gisfy.inclenJson.Entity.Answer;
import com.gisfy.inclenJson.RoomDB.AppDatabase;
import com.gisfy.inclenJson.Utiles.Constants;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class ViewAnswers extends AppCompatActivity {

    public static final int REQUEST_CODE_GET_JSON = 52;
    public static Answer answer=null;
    public static int itemPosition = -1;
    private String projectName,surveyId,surveyName;
    private RecyclerView recyclerView;
    private List<Answer> list;
    private AnswerAdapter adapter;
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_answers);
        initToolBar();
        fetchAnswers();

    }

    public void fetchAnswers(){
        database = AppDatabase.getObInstance(this);
        list = database.answerDao().findAllAnswers(surveyId);
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new AnswerAdapter(this,list);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GET_JSON && resultCode == RESULT_OK) {
            if (answer!=null && data!=null && data.hasExtra("json") && data.getStringExtra("json") != null) {
                Log.i("received Json",data.getStringExtra("json"));
                answer.setJsonData(data.getStringExtra("json"));
                answer.setPartialSurvey(data.hasExtra(Constants.PARTIAL_SURVEY_INTENT));
                answer.setSynced(false);
                database.answerDao().insert(answer);
                Toast.makeText(ViewAnswers.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                if (itemPosition != -1){
                    list.set(itemPosition,answer);
                    adapter.notifyItemChanged(itemPosition);
                }
            }
        }
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        projectName = getIntent().getStringExtra(Constants.PROJECT_NAME);
        surveyId = getIntent().getStringExtra(Constants.SURVEY_ID);
        surveyName = getIntent().getStringExtra(Constants.SURVEY_NAME);

        if (projectName != null)
            getSupportActionBar().setTitle(projectName);
        else
            getSupportActionBar().setTitle("N/A Project");
    }
}