package com.gisfy.inclenJson.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gisfy.inclenJson.PayLoads.SurveyEn.SurveyFormDatum;
import com.gisfy.inclenJson.PayLoads.SurveyEn.SurveyFormJsonAnswer;
import com.gisfy.inclenJson.RoomDB.AppDatabase;
import com.gisfy.inclenJson.RoomDB.Dao.SurveyDao;
import com.gisfy.inclenJson.Synchronise.GetSurveyForms;
import com.gisfy.inclenJson.Synchronise.ResponseCallBack;
import com.gisfy.inclenJson.Synchronise.SyncSurveyForms;
import com.gisfy.inclenJson.Utiles.SessionManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import inclenJson.R;
import okhttp3.OkHttpClient;

public class SyncSurveyFormActivity extends AppCompatActivity {

    @BindView(R.id.surveyno)
    TextView totalSurvey;
    public JSONObject jsonObject=new JSONObject();
    public JSONArray jsonArray = new JSONArray();
    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;
    public Double lati;
    public Double longi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_survey_form);
        ButterKnife.bind(this);

        totalSurvey.setText(AppDatabase.getObInstance(SyncSurveyFormActivity.this).surveyDao().getSurveyToBeSyncCount()+"");


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        getLastLocation();
    }
    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkPermissions()) {

            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        SyncSurveyFormActivity obj = new SyncSurveyFormActivity();
                        Location location = task.getResult();

                        if (location == null) {
                            requestNewLocationData();
                        } else {
                          lati = location.getLatitude();
                          longi = location.getLongitude();
                            Log.d("LOCATION!",String.valueOf(lati));
                        }
                    }
                });

            }else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        }else {
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        com.google.android.gms.location.LocationRequest mLocationRequest = new com.google.android.gms.location.LocationRequest();

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient

        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());

    }
    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();

            lati = mLastLocation.getLatitude();
            longi = mLastLocation.getLongitude();

        }
    };
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;


    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }

    @OnClick(R.id.back)
    public void onClickBack(){
        onBackPressed();
    }

    @OnClick(R.id.sync_now)
    public void onClickSyncNow(){


        JSONObject surveyDetails = new JSONObject();
        RequestQueue queue = Volley.newRequestQueue(SyncSurveyFormActivity.this);
        SessionManager sessionManager = new SessionManager(SyncSurveyFormActivity.this);
//        OkHttpClient client = new OkHttpClient().newBuilder()
//                .build();
        String URL = SyncSurveyFormActivity.this.getResources().getString(R.string.sync_url);

        for (int i = 0; i < AppDatabase.getObInstance(SyncSurveyFormActivity.this).surveyDao().getSurveyToBeSyncCount(); i++) {
            int finalI = i;
            Type listType = new TypeToken<List<SurveyFormDatum>>() {
            }.getType();
            List<SurveyFormJsonAnswer> surveyFormJsonAnswerList = AppDatabase.getObInstance(SyncSurveyFormActivity.this).surveyDao().getSurveyFormAnswers();
            SurveyFormJsonAnswer item = surveyFormJsonAnswerList.get(finalI);
        try {


                List<SurveyFormDatum> list = new Gson().fromJson(item.getJsonAnswer(), listType);

                for (int io = 0; io < list.size(); io++) {
                    surveyDetails.put(list.get(io).getColumn().toLowerCase(Locale.ROOT), list.get(io).getUserAnswer());
                }
                    String userID = String.valueOf(sessionManager.getLoginPayLoad().getData().getName().getId());
                    String typeId = String.valueOf(item.getId());

                surveyDetails.put("survey_id",item.getSurveyId() + "");

                surveyDetails.put("userid", userID);
                surveyDetails.put("latlong", lati +"-"+longi+"");
                surveyDetails.put("type", typeId);


                jsonArray.put(surveyDetails);
                jsonObject.put("surveyDetails", jsonArray);
                Log.d("REQUSTTT", jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

// Create the request object
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("RESPPSUCC",response.toString());
                        Log.d("REQUSTTTSUCC",jsonObject.toString());

                        Toast.makeText(SyncSurveyFormActivity.this,"Form synced successfully", Toast.LENGTH_SHORT).show();


                        AppDatabase db = AppDatabase.getObInstance(SyncSurveyFormActivity.this);
                         db.surveyDao().deleteAll();
                        totalSurvey.setText("0");

                           // db.surveyDao().deleteAll();

                        // Handle successful response

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error response
                Log.d("REQUSTTTFAIL",jsonObject.toString());
                Toast.makeText(SyncSurveyFormActivity.this,"something get wrong!", Toast.LENGTH_SHORT).show();
                AppDatabase db = AppDatabase.getObInstance(SyncSurveyFormActivity.this);
                db.surveyDao().deleteAll();
                totalSurvey.setText("0");


            }
        }) {
            // Add header to the request
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization",  "Bearer" + " " +sessionManager.getLoginPayLoad().getData().getToken());
                return headers;
            }
        };

// Add the request to the RequestQueue
        queue.add(jsonObjectRequest);


    }
    }
}
