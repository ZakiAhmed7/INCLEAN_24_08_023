package com.gisfy.inclenJson.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import inclenJson.R;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gisfy.inclenJson.Activities.MapTabActivity;
import com.gisfy.inclenJson.Activities.SurveyFormActivity;
import com.gisfy.inclenJson.Entity.Answer;
import com.gisfy.inclenJson.Entity.Question;
import com.gisfy.inclenJson.RoomDB.AppDatabase;
import com.gisfy.inclenJson.Utiles.Constants;
import com.gisfy.inclenJson.Utiles.SessionManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.vijay.jsonwizard.activities.JsonFormActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class BaselineSurvey extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = MapFragment.class.getSimpleName();
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private GoogleMap mGoogleMap;
    private com.google.android.gms.maps.MapView mMapView;
    private LinearLayout drawLine, drawPolygon;
    private TextView submit;
    private static final int REQUEST_CODE_GET_JSON = 1;
    Polyline polyline = null;
    private List<LatLng> latLngPoints = new ArrayList<>();
    private List<Marker> markerList = new ArrayList<>();
    private Polygon polygon = null;
    private final int REQUEST_CHECK_GPS = 100;

    private String PLOT_TYPE = "";
    private String PLOTTED_DATA = "";
    private String projectName, surveyId, surveyName;
    private Set<String> PLOTSDATA = new HashSet<>();
    MapTabActivity mapTabActivity;
    private boolean plotD=false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        projectName = getActivity().getIntent().getStringExtra(Constants.PROJECT_NAME);
        surveyId = getActivity().getIntent().getStringExtra(Constants.SURVEY_ID);
        surveyName = getActivity().getIntent().getStringExtra(Constants.SURVEY_NAME);

        mapTabActivity = (MapTabActivity) getActivity();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1 * 1000);

        Log.d(TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_baseline_survey, container, false);
        mMapView = view.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        PolygonOptions polygonOptions = new PolygonOptions();
        mMapView.onResume();


        drawLine = view.findViewById(R.id.drawLine);
        submit = view.findViewById(R.id.submit);
        drawPolygon = view.findViewById(R.id.drawPolygon);
        SharedPreferences prefs = getActivity().getSharedPreferences("my_prefs", getContext().MODE_PRIVATE);
        SessionManager sessionManager = new SessionManager(getContext());


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String point = mapTabActivity.PLOTSP;
                if(!Objects.equals(point, "")){
                    String type =mapTabActivity.PLOTSL;
                    if (type=="linte"){

                        PLOT_TYPE = Constants.POINT;


//                    Question question = AppDatabase.getObInstance(getContext()).questionDao().selectProperty(getActivity().getIntent().getStringExtra("surveyId"));
                        Intent intent = new Intent(getActivity(), SurveyFormActivity.class);
                        intent.putExtra("surveyID", mapTabActivity.surveyId);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(getActivity(), SurveyFormActivity.class);
                        intent.putExtra("surveyID", mapTabActivity.surveyId);
                        startActivity(intent);
                    }

                }

                else if (PLOT_TYPE.equals(Constants.POINT) && latLngPoints.size() == 1) {

                    PLOT_TYPE = Constants.POINT;


//                    Question question = AppDatabase.getObInstance(getContext()).questionDao().selectProperty(getActivity().getIntent().getStringExtra("surveyId"));
                    Intent intent = new Intent(getActivity(), SurveyFormActivity.class);

                    intent.putExtra("surveyID", mapTabActivity.surveyId);


                    startActivity(intent);

                } else if (PLOT_TYPE.equals(Constants.POLYGON) || PLOT_TYPE.equals(Constants.POLYLINE) && latLngPoints.size() > 1) {


                    String latLngListjson = new Gson().toJson(latLngPoints);
//                    Question question = AppDatabase.getObInstance(getContext()).questionDao().selectProperty(getActivity().getIntent().getStringExtra("surveyId"));
                    Intent intent = new Intent(getActivity(), SurveyFormActivity.class);
                    Log.i("json", latLngListjson);
                    intent.putExtra("surveyID", mapTabActivity.surveyId);


                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Plot the points properly", Toast.LENGTH_SHORT).show();
                }

            }
        });
        drawLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (polyline != null) polyline.remove();
                PolylineOptions polylineOptions = new PolylineOptions()
                        .addAll(latLngPoints).clickable(true);
                polyline = mGoogleMap.addPolyline(polylineOptions);
                PLOT_TYPE = Constants.POLYLINE;

                String point = mapTabActivity.PLOTSL;
                Log.d("PLOTPPP",point);

                if(!Objects.equals(point, "")) {
                    PLOTTED_DATA =  latLngPoints.get(0).longitude + " " + latLngPoints.get(0).latitude+","+latLngPoints.get(1).longitude + " " + latLngPoints.get(1).latitude+"+"+point;
                }else {
                    PLOTTED_DATA = latLngPoints.get(0).longitude + " " + latLngPoints.get(0).latitude+","+latLngPoints.get(1).longitude + " " + latLngPoints.get(1).latitude;
                }


                SharedPreferences.Editor editor = prefs.edit();

                editor.putString("PLOTLINE_"+mapTabActivity.surveyId,PLOTTED_DATA);
//                        editor.putString("PLOTTYPE_"+mapTabActivity.surveyId+"@"+i, PLOT_TYPE);
                Log.d("PLOTTTTKEYY","PLOTLINE_"+mapTabActivity.surveyId);
                Log.d("PLOTTYDATA",PLOTTED_DATA);
                editor.commit();

            }
        });

        drawPolygon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (latLngPoints.size() < 4) {
                    Toast.makeText(getContext(), "Polygon should have 4 points",
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (polygon != null)
                        polygon.remove();
                    PolygonOptions polygonOptions = new PolygonOptions().addAll(latLngPoints).clickable(true);
                    polygon = mGoogleMap.addPolygon(polygonOptions);
                    PLOT_TYPE = Constants.POLYGON;
                    StringBuilder polyPoints = new StringBuilder();
                    for (int i = 0; i < latLngPoints.size(); i++) {

                        if (i == (latLngPoints.size() - 1))
                            polyPoints.append(latLngPoints.get(i).longitude).append(" ").append(latLngPoints.get(i).latitude).append("");
                        else
                            polyPoints.append(latLngPoints.get(i).longitude).append(" ").append(latLngPoints.get(i).latitude).append(",");
                    }
                    String point = mapTabActivity.PLOTSP;
                    Log.d("PLOTPPP",point);
                    if(!Objects.equals(point, "")) {
                        PLOTTED_DATA = polyPoints.toString()+"+"+point;
                    }else {
                        PLOTTED_DATA = polyPoints.toString();
                    }


                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("PLOTSQURE_"+mapTabActivity.surveyId,PLOTTED_DATA);
                    Log.d("PLOTTTTKEYY","PLOTSQURE_"+mapTabActivity.surveyId);
                    Log.d("PLOTTYDATA",PLOTTED_DATA);
                    editor.commit();
                }
            }
        });
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
        mMapView.onResume();
        setUpMap();

        Log.d(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
        mMapView.onPause();

        Log.d(TAG, "onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();

        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();

        Log.d(TAG, "onLowMemory");
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (!hasPermission(Manifest.permission.ACCESS_FINE_LOCATION))
            return;

        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null)
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        else
            handleNewLocation(location);

        Log.d(TAG, "onConnected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed");
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);

        Log.d(TAG, "onLocationChanged");
    }

    private void setUpMap() {
        if (mGoogleMap == null)
            mMapView.getMapAsync(new OnMapReadyCallback() {

                @Override
                public void onMapReady(GoogleMap googleMap) {

                    mGoogleMap = googleMap;
//                    String point = mapTabActivity.PLOTS;
//                    Log.d("PLOT121",point);
//                    if(!Objects.equals(point, "")){
//                      Log.d("PLOT121",point);
                      addPolygonToMap();
//                    }
                    mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                            Marker marker = mGoogleMap.addMarker(markerOptions);
                            latLngPoints.add(latLng);
                            markerList.add(marker);

                            if (latLngPoints.size() == 1)
                                PLOT_TYPE = Constants.POINT;
                            else
                                PLOT_TYPE = "";
                        }
                    });
                    mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
                    mGoogleMap.getUiSettings().setCompassEnabled(true);
                    mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
                    mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
                    mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                        @Override
                        public void onMapLongClick(LatLng latLng) {
                            if (polygon != null)
                                polygon.remove();

                            for (Marker marker : markerList)
                                marker.remove();

                            markerList.clear();
                            latLngPoints.clear();
                            polygon = null;
                            polyline = null;
                            PLOT_TYPE = "";
                            PLOTTED_DATA = "";
                        }
                    });

                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mGoogleMap.setMyLocationEnabled(true);

                }
            });
    }
    private void addPolygonToMap() {
        if (mGoogleMap != null) {
            PolygonOptions polygonOptionsB = new PolygonOptions()
                    .add(new LatLng(11.8064738084855,76.03869941085577))
                    .add(new LatLng(11.807434070606327,76.06086820363998))
                    .add(new LatLng(11.790566639595493,76.06351688504219))
                    .add(new LatLng(11.79552769846612,76.042590290308));
            polygonOptionsB.fillColor(Color.parseColor("#50ff0000"));
            polygonOptionsB.strokeWidth(3);
            mGoogleMap.addPolygon(polygonOptionsB);

            String point = mapTabActivity.PLOTSP;
            String pointa= mapTabActivity.PLOTSL;
            if(!Objects.equals(point, "")) {
                String[] coordinatePairs = point.split("\\+");
                int numSplits = coordinatePairs.length;
                for (String s : coordinatePairs) {
                    String[] pairs = s.split(",");
                    String[] lats = new String[pairs.length];
                    String[] longs = new String[pairs.length];

                    for (int i = 0; i < pairs.length; i++) {
                        String[] coords = pairs[i].trim().split("\\s+");
                        longs[i] = coords[0];
                        lats[i] = coords[1];
                    }

                    PolygonOptions polygonOptions = new PolygonOptions()

                            .add(new LatLng(Double.parseDouble(lats[0]), Double.parseDouble(longs[0])))
                            .add(new LatLng(Double.parseDouble(lats[1]), Double.parseDouble(longs[1])))
                            .add(new LatLng(Double.parseDouble(lats[2]), Double.parseDouble(longs[2])))
                            .add(new LatLng(Double.parseDouble(lats[3]), Double.parseDouble(longs[3])));

                    polygonOptions.fillColor(Color.parseColor("#70ff0000"));
                    polygonOptions.strokeWidth(6);
                    mGoogleMap.addPolygon(polygonOptions);
//                    polygon.setClickable(true);
//                    polygon.setOnPolygonClickListener(new OnPolygonClickListener() {
//                        @Override
//                        public void onPolygonClick(Polygon polygon) {
//                            // Handle the polygon click event here
//                            // For example, show a message
////                            Toast.makeText(getApplicationContext(), "Polygon clicked!", Toast.LENGTH_SHORT).show();
//                        }
//                    });

                }


            }
            if(!Objects.equals(pointa, "")){
                String[] coordinatePairs = pointa.split("\\+");
                int numSplits = coordinatePairs.length;
                for (String s : coordinatePairs) {
                    String[] pairs = s.split(",");
                    String[] lats = new String[pairs.length];
                    String[] longs = new String[pairs.length];

                    for (int i = 0; i < pairs.length; i++) {
                        String[] coords = pairs[i].trim().split("\\s+");
                        longs[i] = coords[0];
                        lats[i] = coords[1];
                    }
                    PolylineOptions polylineOptions = new PolylineOptions()
                        .add(new LatLng(Double.parseDouble(lats[0]), Double.parseDouble(longs[0])))
                        .add(new LatLng(Double.parseDouble(lats[1]), Double.parseDouble(longs[1])));

                                    polylineOptions.width(5);
                mGoogleMap.addPolyline(polylineOptions);
//                    polyline.setClickable(true);
//                    polyline.setOnPolygonClickListener(new OnPolygonClickListener() {
//                        @Override
//                        public void onPolygonClick(Polygon polygon) {
//                            // Handle the polygon click event here
//                            // For example, show a message
//
//                        }
//                    });

            }}



//            if(!Objects.equals(plottype, "")) {
//                String point = mapTabActivity.PLOTS;
//                Log.d("POINTSSPP", point);
//                String[] pairs = point.split(",");
//                String[] lats = new String[pairs.length];
//                String[] longs = new String[pairs.length];
//                for (int i = 0; i < pairs.length; i++) {
//                    String[] coords = pairs[i].trim().split("\\s+");
//                    longs[i] = coords[0];
//                    lats[i] = coords[1];
//                }
//
//
//                PolygonOptions polygonOptions = new PolygonOptions()
//                        .add(new LatLng(Double.parseDouble(lats[0]), Double.parseDouble(longs[0])))
//                        .add(new LatLng(Double.parseDouble(lats[1]), Double.parseDouble(longs[1])))
//                        .add(new LatLng(Double.parseDouble(lats[2]), Double.parseDouble(longs[2])))
//                        .add(new LatLng(Double.parseDouble(lats[3]), Double.parseDouble(longs[3])));
//                polygonOptions.fillColor(Color.parseColor("#70ff0000"));
//                polygonOptions.strokeWidth(6);
//                mGoogleMap.addPolygon(polygonOptions);

            }
//            else if (plottype==Constants.POLYLINE){
//                String point = mapTabActivity.PLOTS;
//                Log.d("POINTSL", point);
//
//                String[] pairs = point.split(",");
//                String[] lats = new String[pairs.length];
//                String[] longs = new String[pairs.length];
//                for (int i = 0; i < pairs.length; i++) {
//                    String[] coords = pairs[i].trim().split("\\s+");
//                    longs[i] = coords[0];
//                    lats[i] = coords[1];
//                }
//                PolylineOptions polylineOptions = new PolylineOptions()
//                        .add(new LatLng(Double.parseDouble(lats[0]), Double.parseDouble(longs[0])))
//                        .add(new LatLng(Double.parseDouble(lats[1]), Double.parseDouble(longs[1])));
////                        .add(new LatLng(Double.parseDouble(lats[2]), Double.parseDouble(longs[2])))
////                        .add(new LatLng(Double.parseDouble(lats[3]), Double.parseDouble(longs[3])));
//
//                polylineOptions.width(5);
//                mGoogleMap.addPolyline(polylineOptions);
//                plotD = true;
//
//            }
        }
//    }

//    String point = mapTabActivity.PLOTS;
//                    Log.d("PLOT121",point);
//                    if(!Objects.equals(point, "")){
//        Log.d("PLOT121",point);
//        PolygonOptions polygonOptions = new PolygonOptions()
//                .add(new LatLng(76.08549490571022, 11.80047455545064))
//                .add(new LatLng(76.09726577997208, 11.800794540805427))
//                .add(new LatLng(76.09775628894567, 11.809563963303164))
//                .add(new LatLng(76.08484078198671, 11.81004408937047));
//        polygonOptions.fillColor(0x50ff0000);
//        polygonOptions.strokeWidth(5);
//        Polygon polygon = mGoogleMap.addPolygon(polygonOptions);
//    }

    private void handleNewLocation(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("I am here!");
        mGoogleMap.addMarker(options);
//        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));

    }

    private boolean hasPermission(String permission) {
        return ContextCompat.checkSelfPermission(getActivity(), permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_GET_JSON && resultCode == RESULT_OK) {
            if (data.hasExtra("json") && data.getStringExtra("json") != null) {
                Answer answer = new Answer();
                answer.setSurveyId(surveyId);
                answer.setProjectName(projectName);
                answer.setJsonData(data.getStringExtra("json"));
                answer.setSurveyName(surveyName);
                answer.setLineType(Constants.BASE_LINE);
                answer.setPlotType(PLOT_TYPE);
                answer.setPlottedData(PLOTTED_DATA);
                Log.d("PLOTTTTANS",PLOTTED_DATA.toString());
                answer.setPartialSurvey(data.hasExtra(Constants.PARTIAL_SURVEY_INTENT));

                answer.setCreatedDate(new SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault()).format(new Date()));

                AppDatabase.getObInstance(getContext()).answerDao().insert(answer);
                Toast.makeText(getContext(), "Saved Successfully", Toast.LENGTH_SHORT).show();
            }
        }
        switch (requestCode) {
            case REQUEST_CHECK_GPS:
                if (resultCode == getActivity().RESULT_OK) {
                    Toast.makeText(getContext(), "GPS is enabled", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    Toast.makeText(getContext(), "Please turn on GPS/ Location", Toast.LENGTH_LONG).show();
                }
                break;

            case 500:
                if (resultCode == getActivity().RESULT_OK) {
                    latLngPoints = (List<LatLng>) data.getSerializableExtra("PolygonPoints");
                    Log.d("DEBUGGING", latLngPoints.toString());
                    for (LatLng latLng : latLngPoints) {
                        MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                        Marker marker = mGoogleMap.addMarker(markerOptions);
                        markerList.add(marker);
                    }
                    drawPolygon.performClick();
                    CameraUpdate cameraUpdate =
                            CameraUpdateFactory.newLatLngZoom(latLngPoints.get(0), 18);
                    mGoogleMap.animateCamera(cameraUpdate);
                } else {
                    Toast.makeText(getContext(), "Operation cancelled", Toast.LENGTH_SHORT).show();
                }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}