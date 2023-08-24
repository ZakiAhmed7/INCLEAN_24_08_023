package com.gisfy.inclenJson;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import inclenJson.R;
import com.gisfy.inclenJson.Activities.HomeActivityDashboard;
import com.gisfy.inclenJson.Login.LoginActivity;
import com.gisfy.inclenJson.Utiles.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigateToNextScreen();
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("apikey", "0MDRPMMG86YKQ1XJSUSR");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void navigateToNextScreen() {
        SessionManager sessionManager = new SessionManager(this);
        if (sessionManager.isLoggedIn()) {
            Intent intent = new Intent(MainActivity.this,HomeActivityDashboard.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else {
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        finish();
    }
}