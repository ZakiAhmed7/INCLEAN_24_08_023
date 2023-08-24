
package com.gisfy.inclenJson.Adapters;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gisfy.inclenJson.Activities.ProjectOverView;
import com.gisfy.inclenJson.Fragment.DashFrag;
import inclenJson.R;

import com.gisfy.inclenJson.PayLoads.ProjectEn.ProjectData;
import com.gisfy.inclenJson.PayLoads.ProjectEn.ProjectResponse;
import com.gisfy.inclenJson.RoomDB.Dao.ProjectDao;
import com.gisfy.inclenJson.Utiles.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.MyViewHolder> {
    DashFrag context;
    private List<ProjectData> list;

    ProjectClickEvents events;

    public ProjectsAdapter(DashFrag context, List<ProjectData> list,ProjectClickEvents events) {
        this.context = context;
        this.list = list;
        this.events = events;
    }
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context.getContext()).inflate(R.layout.projectdatadesign, parent ,false );
        return new MyViewHolder(view);
    }

    public interface ProjectClickEvents{
        void onClickProject(ProjectData model);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        ProjectData model=list.get(position);
        holder.projectName.setText(model.getProjectname());
        holder.createdAt.setText(model.getStartdate());
        holder.serialNo.setText(String.valueOf(position+1));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                events.onClickProject(model);

//                Intent intent = new Intent(v.getContext(), ProjectOverView.class);
//                intent.putExtra(Constants.PROJECT_NAME, model.getProjectname());
//                intent.putExtra(Constants.SURVEY_ID, model.getProject_data().getProject_survey());
//                intent.putExtra(Constants.SURVEY_NAME, model.getSurveyName());

//                context.startActivity(intent);
            }
        });


    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView projectName , createdAt ,serialNo;
        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            projectName = itemView.findViewById(R.id.projectName);
            createdAt = itemView.findViewById(R.id.createdAt);
            serialNo = itemView.findViewById(R.id.serialNo);

        }
    }
}