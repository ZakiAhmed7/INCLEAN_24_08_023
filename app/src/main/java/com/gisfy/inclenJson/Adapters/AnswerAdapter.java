package com.gisfy.inclenJson.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gisfy.inclenJson.Activities.ViewAnswers;
import com.gisfy.inclenJson.Entity.Answer;
import com.gisfy.inclenJson.RoomDB.AppDatabase;
import com.gisfy.inclenJson.Utiles.Constants;
import com.gisfy.inclenJson.Utiles.FormParser;
import com.gisfy.inclenJson.Utiles.SessionManager;
import com.vijay.jsonwizard.activities.JsonFormActivity;

import inclenJson.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.google.android.material.internal.ContextUtils.getActivity;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.MyViewHolder>{

    Context context;
    private List<Answer> list;
    public AnswerAdapter(Context context, List<Answer> list) {
        this.context = context;
        this.list = list;
    }

    @NotNull
    @Override
    public AnswerAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.projectdatadesign, parent ,false );
        return new AnswerAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AnswerAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Answer entity = list.get(position);
        holder.imageButtonLayout.setVisibility(View.VISIBLE);
        holder.serialNo.setText(String.valueOf(position+1));
        holder.projectName.setText(entity.getProjectName());
        holder.createdAt.setText(entity.getLineType()+" | "+entity.getPlotType()+" | "+entity.getCreatedDate());

        if (entity.isPartialSurvey())
            holder.partialSurvey.setVisibility(View.VISIBLE);
        else
            holder.partialSurvey.setVisibility(View.GONE);

        if (entity.isSynced())
            holder.sync.setImageResource(R.drawable.ic_baseline_cloud_done);
        else
            holder.sync.setImageResource(R.drawable.ic_cloud_sync_enabled);

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewAnswers.answer = entity;
                ViewAnswers.itemPosition = position;
                Intent intent = new Intent(context, JsonFormActivity.class);
                Log.i("Send Json", entity.getJsonData());
                intent.putExtra("json", entity.getJsonData());
                getActivityByContext(context).startActivityForResult(intent, ViewAnswers.REQUEST_CODE_GET_JSON);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle(entity.getProjectName());
                alertDialog.setMessage("Do you really want to delete the item");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            AppDatabase.getObInstance(context).answerDao().delete(entity);
                            list.remove(position);
                            notifyItemChanged(position);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });

        holder.sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (entity.isSynced() && entity.isPartialSurvey()){
                    Toast.makeText(context, "This entry is synced or partially completed", Toast.LENGTH_SHORT).show();
                    return;
                }
                try{
                    JSONObject object = new JSONObject(entity.getJsonData());
                    JSONArray fieldsArray = object.getJSONObject("step1").getJSONArray("fields");
                    MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

                    Log.i("Plotted",entity.getPlottedData());

                    if(entity.getLineType().equals(Constants.BASE_LINE)){
                        builder.addFormDataPart("baselinetype",entity.getPlotType());
                        builder.addFormDataPart("baseline",entity.getPlottedData());
                    }else{
                        builder.addFormDataPart("endline",entity.getPlottedData());
                        builder.addFormDataPart("endlinetype",entity.getPlotType());
                    }

                    builder.addFormDataPart("surveyid",entity.getSurveyId());
                    builder.addFormDataPart("userid", String.valueOf(new SessionManager(context).getLoginPayLoad().getData().getName().getId()));

                    for (int i=0 ; i< fieldsArray.length() ; i++){
                        JSONObject fieldObject = fieldsArray.getJSONObject(i);
                        if (fieldObject.getString("type").equals("check_box")){
                            JSONArray optionArray = fieldObject.getJSONArray("options");
                            StringBuilder valueBuilder = new StringBuilder();
                            for (int j=0 ; j< optionArray.length() ; j++){
                                JSONObject optionObject = optionArray.getJSONObject(j);
                                if (optionObject.has("value") && optionObject.getString("value").equals("true")){
                                    valueBuilder.append(optionObject.getString("key")).append(",");
                                }
                            }
                            builder.addFormDataPart(fieldObject.getString("dbColumn"),valueBuilder.toString());
                        }else{
                            String value = fieldObject.has("value")?fieldObject.getString("value"):"";
                            builder.addFormDataPart(fieldObject.getString("dbColumn"),value);
                        }
                    }
                    for (MultipartBody.Part part:builder.build().parts()){
                        Log.i("object ", part.headers().names().toString());
                    }

                    new SyncData(builder,entity,position).execute();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Unable to parse the answers", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView projectName , createdAt ,serialNo;
        private LinearLayout imageButtonLayout;
        private ImageButton sync,edit,delete,partialSurvey;
        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            projectName = itemView.findViewById(R.id.projectName);
            createdAt = itemView.findViewById(R.id.createdAt);
            serialNo = itemView.findViewById(R.id.serialNo);

            partialSurvey = itemView.findViewById(R.id.partialSurvey);
            imageButtonLayout = itemView.findViewById(R.id.imageButtonLayout);
            sync = itemView.findViewById(R.id.sync);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);

        }
    }

    @SuppressLint("RestrictedApi")
    public Activity getActivityByContext(Context context){
        if(context == null){
            return null;
        }
        else if((context instanceof ContextWrapper) && (context instanceof Activity)){
            return (Activity) context;
        }
        else if(context instanceof ContextWrapper){
            return getActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }

    public class SyncData extends AsyncTask<String,String,String>{

        private MultipartBody.Builder builder;
        private Answer entity;
        private int pos;
        private ProgressDialog dialog;

        public SyncData(MultipartBody.Builder builder,Answer entity,int pos){
            this.builder=builder;
            this.entity=entity;
            this.pos=pos;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(context);
            dialog.setMessage("Loading...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            String token = new SessionManager(context).getLoginPayLoad().getData().getToken();
            Request request = new Request.Builder()
                    .url(context.getResources().getString(R.string.sync_url))
                    .method("POST", builder.build())
                    .addHeader("Authorization", "Bearer "+token)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();
                Log.i("responseBody" , responseBody);
                if (responseBody.contains("success")){
                    entity.setSynced(true);
                    AppDatabase.getObInstance(context).answerDao().update(entity);
                    list.set(pos,entity);
                    return "Synced";
                }else{
                    Log.i("DEBUGGING RES",responseBody);
                    return "No Proper data received";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "error: "+ e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("DEBUGGING RES",s);
            notifyItemChanged(pos);
            dialog.dismiss();
            Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
        }
    }

}
