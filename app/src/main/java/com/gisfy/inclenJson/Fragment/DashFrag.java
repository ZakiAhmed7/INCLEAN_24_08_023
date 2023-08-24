package com.gisfy.inclenJson.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.gisfy.inclenJson.Activities.MapTabActivity;
import com.gisfy.inclenJson.Activities.SurveyFormActivity;
import com.gisfy.inclenJson.Activities.SyncSurveyFormActivity;
import com.gisfy.inclenJson.Adapters.ProjectsAdapter;
import com.gisfy.inclenJson.Entity.Program;

import butterknife.ButterKnife;
import butterknife.OnClick;
import inclenJson.R;

import com.gisfy.inclenJson.PayLoads.ProjectEn.ProjectData;
import com.gisfy.inclenJson.PayLoads.ProjectEn.ProjectResponse;
import com.gisfy.inclenJson.PayLoads.ProjectEn.ProjectSurvey;
import com.gisfy.inclenJson.RoomDB.AppDatabase;
import com.gisfy.inclenJson.RoomDB.Dao.ProjectDao;
import com.gisfy.inclenJson.Synchronise.ProgramSync;
import com.gisfy.inclenJson.Synchronise.ResponseCallBack;
import com.gisfy.inclenJson.Utiles.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class DashFrag extends Fragment {
    ProjectsAdapter projectsAdapter;
    private ProjectDao projectDao;
    private AppDatabase appDatabase;
    private List<ProjectData> programList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_dash, container, false);
         ButterKnife.bind(this,view);
        appDatabase = AppDatabase.getObInstance(this.getContext());
        programList=new ArrayList<>();
       try {
      loadProjectData(view);

      }catch (Exception e){}
        return view;
    }


    private void loadProjectData(View view) {
        projectsAdapter=new ProjectsAdapter(DashFrag.this, programList, new ProjectsAdapter.ProjectClickEvents() {
            @Override
            public void onClickProject(ProjectData model) {
                try {
                    openSurveySelectDialog(model);
                }catch (Exception e){}

            }
        });
        RecyclerView recyclerView=view.findViewById(R.id.campaignRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(projectsAdapter);

        if (isNetworkConnected()) {
            new ProgramSync(getContext(), new ResponseCallBack() {
                @Override
                public void callBack(String response) {
                    programList.addAll(new SessionManager(getContext()).getProjectResponse());
                    projectsAdapter.notifyDataSetChanged();
                }
            });
        }else{
            programList.addAll(new SessionManager(getContext()).getProjectResponse());
            projectsAdapter.notifyDataSetChanged();
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void openSurveySelectDialog(ProjectData model){
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_select_survey);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

        Spinner surveySpinner = dialog.findViewById(R.id.spinner);
        Button confirmBtn = dialog.findViewById(R.id.confirm);
        Button cancelBtn = dialog.findViewById(R.id.cancel);
        ProjectSurvey projectSurvey = new ProjectSurvey();
        projectSurvey.setSurveyname("Select Survey");
        List<ProjectSurvey> projectSurveyList = new ArrayList<>();
        projectSurveyList.add(projectSurvey);
        projectSurveyList.addAll(model.getProject_survey());
        if(projectSurveyList.isEmpty()){
            Toast.makeText(getContext(),"No survey avilable",Toast.LENGTH_LONG).show();
        }else{
            ArrayAdapter<ProjectSurvey> arrayAdapter = new ArrayAdapter<ProjectSurvey>(getContext(),R.layout.support_simple_spinner_dropdown_item,projectSurveyList){
                @Override
                public boolean isEnabled(int position) {
                    return position!=0;
                }
            };
            surveySpinner.setAdapter(arrayAdapter);
        }


        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((ProjectSurvey)surveySpinner.getSelectedItem()).getId() == 0){
                    Toast.makeText(getContext(),"Select any survey",Toast.LENGTH_LONG).show();
                    return;
                }
//                else if(((ProjectSurvey)surveySpinner.getContext()=null){
//
//                    Toast.makeText(getContext(),"No survey avilable",Toast.LENGTH_LONG).show();
//                    return;
//                }
               ProjectSurvey projectSurvey1 =  ((ProjectSurvey)surveySpinner.getSelectedItem());

                Intent intent = new Intent(getContext(), MapTabActivity.class);
                intent.putExtra("surveyID",projectSurvey1.getId());
                startActivity(intent);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            dialog.dismiss();
            }
        });

        dialog.show();
    }

    @OnClick(R.id.sync_screen)
    public void onClickSyncScreen(){
        Intent intent = new Intent(getContext(), SyncSurveyFormActivity.class);
        startActivity(intent);
    }
}