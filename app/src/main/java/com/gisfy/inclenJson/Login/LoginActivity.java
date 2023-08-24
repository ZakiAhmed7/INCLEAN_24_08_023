package com.gisfy.inclenJson.Login;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gisfy.inclenJson.Activities.HomeActivityDashboard;
import com.gisfy.inclenJson.PayLoads.LoginResponse;
import inclenJson.R;
import com.gisfy.inclenJson.Utiles.SessionManager;
import com.google.gson.Gson;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.logininclen)
    TextView loginButton;

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkConnected()) {
                    if (username.getText().length()< 0 || username.getText().toString().equals("")) {
                        Toast.makeText(LoginActivity.this, "Please enter valid Login ID",
                                Toast.LENGTH_SHORT).show();
                    } else if (password.getText().length()< 0 || password.getText().toString().equals("")) {
                        Toast.makeText(LoginActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                    } else {
                        new GetLogin(username.getText().toString(), password.getText().toString()).execute();
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private class GetLogin extends AsyncTask<String,String,String> {
        ProgressDialog dialog ;
        String userName, passWord;
        private GetLogin(String userName, String passWord){
            this.userName = userName;
            this.passWord = passWord;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setMessage("Logging in..");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("username",userName)
                    .addFormDataPart("password",passWord)
                    .build();
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url("http://13.235.206.126/api/login")
                    .method("POST", body)
//                    .addHeader("Authorization", "Bearer " +" 158|tybewq7nq1vwzdTVjZsI6wfJUaYGkO1GkRcSZI8R")
                    .build();
            try {
                okhttp3.Response response = client.newCall(request).execute();
                String responseBody = response.body().string();
                Log.i("responseBody",responseBody+"");
                if(response.code()==200){
//                LoginResponse u=new Gson().fromJson(responseBody, LoginResponse.class);
//                Log.i("LoginResponseee",u.getData().getName().getId()+"");

//          if(u.isSuccess()){
                    new SessionManager(LoginActivity.this).createALoginSession(responseBody);
                    return "Success";
                }else{
                    return "Unable to connect.";
                }

//                    return "Unable to connect. status:"+u.getMessage();
            } catch (IOException e) { e.printStackTrace();
                return e.getMessage();
            }
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            if (s.equals("Success")){
                Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this,HomeActivityDashboard.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else
                Toast.makeText(LoginActivity.this, "Login Failed Message:"+s, Toast.LENGTH_SHORT).show();
        }
    }
}
