package com.gisfy.inclenJson.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import inclenJson.R;

import com.gisfy.inclenJson.RoomDB.AppDatabase;
import com.gisfy.inclenJson.Synchronise.QuestionSync;
import com.gisfy.inclenJson.Synchronise.ResponseCallBack;
import com.gisfy.inclenJson.Utiles.Constants;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;

public class ProjectOverView extends AppCompatActivity {

    @BindView(R.id.syncEntry)
    LinearLayout syncEntry;
    @BindView(R.id.editEntry)
    LinearLayout editEntry;
    @BindView(R.id.addEntry)
    LinearLayout addEntry;
    @BindView(R.id.helpEntry)
    LinearLayout helpEntry;
    @BindView(R.id.syncsurveyno)
    TextView syncsurveyno;
    @BindView(R.id.surveyno)
    TextView surveyno;
    @BindView(R.id.partialsurveyno)
    TextView partialsurveyno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_over_view);
        ButterKnife.bind(this);

        AppDatabase database = AppDatabase.getObInstance(this);

        partialsurveyno.setText(""+database.answerDao().countOfPartialSurvey(true,getIntent().getStringExtra(Constants.SURVEY_ID)));

        syncsurveyno.setText(""+database.answerDao().countOfSynced(true,getIntent().getStringExtra(Constants.SURVEY_ID)));

        surveyno.setText(""+database.answerDao().countOfPartialSurvey(false,getIntent().getStringExtra(Constants.SURVEY_ID)));

        new QuestionSync(ProjectOverView.this, getIntent().getStringExtra(Constants.SURVEY_ID),getIntent().getStringExtra(Constants.PROJECT_NAME), new ResponseCallBack() {
            @Override
            public void callBack(String response) {
                if (!response.contains("loaded")){
                    Toast.makeText(ProjectOverView.this, "Unable to get Questionnaire for the selected project", Toast.LENGTH_SHORT).show();
                }
            }
        });



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

        if (getIntent().hasExtra(Constants.PROJECT_NAME))
            getSupportActionBar().setTitle(getIntent().getStringExtra(Constants.PROJECT_NAME));
        else
            getSupportActionBar().setTitle("N/A Project");

        getLocationPermission();
        syncEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ViewAnswers.class);
                intent.putExtra(Constants.PROJECT_NAME, getIntent().getStringExtra(Constants.PROJECT_NAME));
                intent.putExtra(Constants.SURVEY_ID, getIntent().getStringExtra(Constants.SURVEY_ID));
                intent.putExtra(Constants.SURVEY_NAME, getIntent().getStringExtra(Constants.SURVEY_NAME));
                startActivity(intent);
            }
        });
        editEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ViewAnswers.class);
                intent.putExtra(Constants.PROJECT_NAME, getIntent().getStringExtra(Constants.PROJECT_NAME));
                intent.putExtra(Constants.SURVEY_ID, getIntent().getStringExtra(Constants.SURVEY_ID));
                intent.putExtra(Constants.SURVEY_NAME, getIntent().getStringExtra(Constants.SURVEY_NAME));
                startActivity(intent);
            }
        });

        addEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProjectOverView.this,MapTabActivity.class);
                intent.putExtra(Constants.PROJECT_NAME, getIntent().getStringExtra(Constants.PROJECT_NAME));
                intent.putExtra(Constants.SURVEY_ID, getIntent().getStringExtra(Constants.SURVEY_ID));
                intent.putExtra(Constants.SURVEY_NAME, getIntent().getStringExtra(Constants.SURVEY_NAME));
                startActivity(intent);
            }
        });

        helpEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void getLocationPermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
//                Toast.makeText(MainDashBoard.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(ProjectOverView.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }


        };
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}