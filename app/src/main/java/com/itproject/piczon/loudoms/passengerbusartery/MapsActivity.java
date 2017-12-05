package com.itproject.piczon.loudoms.passengerbusartery;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.text.Text;
import com.itproject.piczon.loudoms.passengerbusartery.DirectionFinder.DirectionFinder;
import com.itproject.piczon.loudoms.passengerbusartery.DirectionFinder.DirectionFinderListener;
import com.itproject.piczon.loudoms.passengerbusartery.DirectionFinder.Route;
import com.itproject.piczon.loudoms.passengerbusartery.FareCalculation.FareCalculation;
import com.itproject.piczon.loudoms.passengerbusartery.FareCalculation.FareCalculationListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class MapsActivity extends Fragment
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        PlaceSelectionListener,
        DirectionFinderListener,
        FareCalculationListener {

    private GoogleMap mMap;
    LocationRequest locationRequest;
    GoogleApiClient googleApiClient;
    Location lastLocation;
    Marker currentLocationMarker;

    private RequestQueue requestQueue;

    String address, destination, duration, distance, endAddress;

    LatLng startLatLng, endLatLng;
    Bundle savedInstanceState;
    TextView valueEstDistance, valueEstDuration, valueEstFare;
    Button bookTrip;
    EditText edtOrigin, edtDestination, edtNumberOfSeats;
    RadioGroup rgType, rgDirection;
    RadioButton rbType;

    List<Marker> originMarkers = new ArrayList<>();
    List<Marker> destinationMarkers = new ArrayList<>();

    View view, tripInfoLayout;

    private static final String LOG_TAG = "PlaceSelectionListener";
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    private static final int REQUEST_SELECT_PLACE = 1000;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        view = inflater.inflate(R.layout.activity_maps, container, false);
        requestQueue = Volley.newRequestQueue(getActivity());

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        tripInfoLayout = view.findViewById(R.id.relative_trip_info_display);
        valueEstDistance = (TextView) view.findViewById(R.id.valueEstDistance);
        valueEstDuration = (TextView) view.findViewById(R.id.valueEstTime);
        valueEstFare = (TextView) view.findViewById(R.id.valueEstFare);

        bookTrip = (Button) view.findViewById(R.id.btnBookATrip);
        edtOrigin = (EditText) view.findViewById(R.id.edtOrigin);
        edtDestination = (EditText) view.findViewById(R.id.edtDestination);
        edtDestination.requestFocus();
        edtNumberOfSeats = (EditText) view.findViewById(R.id.edtNumberOfSeats);

        getChildFragmentManager().beginTransaction().addToBackStack(null).commit();

        rgType = (RadioGroup) view.findViewById(R.id.rgBusType);
        rgDirection = (RadioGroup) view.findViewById(R.id.rgDirection);

        rgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.rbOrdinary:
                        sendFareRequest();
                        break;
                    case R.id.rbAircon:
                        sendFareRequest();
                        break;
                }
            }
        });

        rgDirection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.rbSouth:
                        Toast.makeText(getActivity(), "You are going South", Toast.LENGTH_SHORT).show();
                        slideUp(tripInfoLayout);
                        break;
                    case R.id.rbNorth:
                        Toast.makeText(getActivity(), "You are going North", Toast.LENGTH_SHORT).show();
                        slideUp(tripInfoLayout);
                        break;
                }
            }
        });


        edtDestination.setInputType(InputType.TYPE_NULL);
        edtDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder
                            (PlaceAutocomplete.MODE_FULLSCREEN)
                            .setBoundsBias(BOUNDS_MOUNTAIN_VIEW)
                            .build(getActivity());
                    startActivityForResult(intent, REQUEST_SELECT_PLACE);
                } catch (GooglePlayServicesRepairableException |
                        GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        edtDestination.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Toast.makeText(getActivity(), "Destination entered", Toast.LENGTH_LONG).show();
                sendRequest();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    public void sendRequest() {
        try {
            new DirectionFinder(this, address, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void sendFareRequest() {
        int busType = rgType.getCheckedRadioButtonId();
        rbType = (RadioButton) view.findViewById(busType);
        final String accommodation = rbType.getText().toString();
        String type = "Student"; // temporary; get type from passenger information
        int numberOfSeats = Integer.parseInt(edtNumberOfSeats.getText().toString());

        try {
            new FareCalculation(this, distance, type, accommodation, numberOfSeats).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (googleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
                mMap.setPadding(20, 780, 20, 220); //540
            } else {
                checkLocationPermission();
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
            mMap.setPadding(20, 780, 20, 220);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        if (currentLocationMarker != null) {
            currentLocationMarker.remove();
        }

        double lat = location.getLatitude();
        double lng = location.getLongitude();
        startLatLng = new LatLng(lat, lng);

        JsonObjectRequest request =
                new JsonObjectRequest("https://maps.googleapis.com/maps/api/geocode/json?latlng="
                        + lat + "," + lng + "&key=AIzaSyCMw_k-w8T3IuYeCYuJY1gdu06cq2zrNC4", new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            address = response.getJSONArray("results").getJSONObject(0).getString("formatted_address");

                            mMap.addMarker(new MarkerOptions()
                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.passenger))
                                    .title(address)
                                    .position(startLatLng));

                            edtOrigin.setText(address);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(request);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(startLatLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        if (googleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(getActivity())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(getActivity(),
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (googleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
//                return;
            }
        }
    }

    @Override
    public void onPlaceSelected(Place place) {
        Log.i(LOG_TAG, "Place Selected: " + place.getName());

        destination = getString(R.string.place_data, place.getAddress());
        edtDestination.setText(destination);
    }

    @Override
    public void onError(Status status) {
        Log.e(LOG_TAG, "onError: Status = " + status.toString());
        Toast.makeText(getActivity(), "Place selection failed: " + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_PLACE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                this.onPlaceSelected(place);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                this.onError(status);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDirectionFinderStart() {

    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route Route : routes) {

            duration = Route.duration.text;
            distance = Route.distance.text;

            endAddress = Route.endAddress;
            endLatLng = Route.endLocation;

            Toast.makeText(getActivity(), "Duration: " + duration +
                    "\nDistance: " + distance, Toast.LENGTH_LONG).show();

            valueEstDistance.setText(distance);
            valueEstDuration.setText(duration);

            mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.dest_pinpoint))
                    .title(endAddress)
                    .position(endLatLng));

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(Route.startLocation);
            builder.include(Route.endLocation);
            LatLngBounds bounds = builder.build();

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 250);
            mMap.animateCamera(cu);
        }
    }

    public void slideUp(View view) {
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    @Override
    public void onFareCalculate(int fare) {
        valueEstFare.setText(String.valueOf(fare));
    }
}
