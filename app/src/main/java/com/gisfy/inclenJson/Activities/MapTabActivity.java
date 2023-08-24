package com.gisfy.inclenJson.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import inclenJson.R;

import com.gisfy.inclenJson.Utiles.Constants;
import com.gisfy.inclenJson.Utiles.SessionManager;
import com.google.android.material.tabs.TabLayout;

public class MapTabActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private SharedPreferences sharedPreferences;
    private View baseLineFragment,endLineFragment;
    private String projectName,surveyName;
    public String PLOTSL="";
    public String PLOTSP="";

    public int surveyId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getApplicationContext().getSharedPreferences("my_prefs", MODE_PRIVATE);


        setContentView(R.layout.activity_map_tab);

        initToolBar( );

        tabLayout = findViewById(R.id.tab_layout);
        baseLineFragment = findViewById(R.id.baseLineFragment);
        endLineFragment = findViewById(R.id.endLineFragment);


        if(getIntent().getExtras()!=null){
            surveyId = getIntent().getExtras().getInt("surveyID");
            PLOTSL = String.valueOf(sharedPreferences.getString("PLOTLINE_"+surveyId, ""));
            PLOTSP = String.valueOf(sharedPreferences.getString("PLOTSQURE_"+surveyId, ""));
            Log.d("PTT",PLOTSL);
            Log.d("PTT",PLOTSP);

        }


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition()==1){
                    endLineFragment.setVisibility(View.VISIBLE);
                    baseLineFragment.setVisibility(View.GONE);
                }else{
                    baseLineFragment.setVisibility(View.VISIBLE);
                    endLineFragment.setVisibility(View.GONE);
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
//      private void viewData(){
//        Log.d("PLOTT",PLOTS);
//
//      }
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
//        surveyId = getIntent().getStringExtra(Constants.SURVEY_ID);
        surveyName = getIntent().getStringExtra(Constants.SURVEY_NAME);

        if (projectName != null)
            getSupportActionBar().setTitle(projectName);
        else
            getSupportActionBar().setTitle("N/A Project");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}