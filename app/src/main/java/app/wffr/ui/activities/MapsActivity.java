package app.wffr.ui.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import app.wffr.R;
import app.wffr.WffrBaseActivity;
import app.wffr.databinding.ActivityMapsBinding;
import app.wffr.directionhelpers.FetchURL;
import app.wffr.directionhelpers.TaskLoadedCallback;
import app.wffr.models.MapsResponse;
import app.wffr.repositories.LocalRepo;
import app.wffr.viewmodels.MapsViewModel;

public class MapsActivity extends WffrBaseActivity implements OnMapReadyCallback, TaskLoadedCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private int REQUEST_CODE = 44;
    private double lat = 0;
    private double lng = 0;
    private MapsViewModel mapsViewModel;
    private boolean isPaused = false;

    private MarkerOptions place1, place2;
    private Polyline currentPolyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_maps);

        binding.btnBack.setOnClickListener(v -> {
            onBackPressed();
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_search);
        mapFragment.getMapAsync(this);

        final LocationManager manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE );

        if (!manager.isProviderEnabled( LocationManager.GPS_PROVIDER) ) {
            buildAlertMessageNoGps();
        }

        mapsViewModel = new ViewModelProvider(this).get(MapsViewModel.class);

    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        lat = addresses.get(0).getLatitude();
                        lng = addresses.get(0).getLongitude();
                        LatLng currentLocation = new LatLng(lat, lng);
                        System.out.println("LAT : " + lat + " LNG : " + lng);
                        mMap.addMarker(new MarkerOptions().position(currentLocation)
                                .title("Your Location")); // .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_wffr))
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                        mMap.setMinZoomPreference(10);

                        mapsViewModel.init(MapsActivity.this);
                        mapsViewModel.loadAllLocations(
                                lat - 1 +"",
                                lat + 1 +"",
                                lng - 1 +"",
                                lng + 1 +""
                        );

                        mapsViewModel.getLocations().observe(MapsActivity.this, new Observer<List<MapsResponse>>() {
                            @Override
                            public void onChanged(List<MapsResponse> mapsResponses) {
                                //Toast.makeText(MapsActivity.this, mapsResponses.size()+"", Toast.LENGTH_SHORT).show();
                                for (MapsResponse mapsResponse : mapsResponses){
                                    LatLng newLoc = new LatLng(Double.parseDouble(mapsResponse.getX()), Double.parseDouble(mapsResponse.getY()));
                                    mMap.addMarker(new MarkerOptions().position(newLoc)
                                            .title(LocalRepo.getLanguage(MapsActivity.this).equals("en") ? mapsResponse.getShopnameen() : mapsResponse.getShopnamear()).icon(BitmapDescriptorFactory.fromResource(R.drawable.wffr_location))); // .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_wffr))
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(newLoc));
                                    mMap.setMinZoomPreference(10);
                                    System.out.println("SHOP LOCATION : " + mapsResponse.getX() + "   " + mapsResponse.getY());
                                }
                            }
                        });

                        mapsViewModel.getIsUpdating().observe(MapsActivity.this, new Observer<Boolean>() {
                            @Override
                            public void onChanged(Boolean aBoolean) {
                                if (aBoolean){
                                    showProgressBar(binding.progressBar);
                                }else {
                                    hideProgressBar(binding.progressBar);
                                }
                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        binding.btnRefresh.setOnClickListener(v -> {
            mMap.clear();
            getLocation();
        });

        binding.btnMyLoc.setOnClickListener(v -> {
            LatLng currentLocation = new LatLng(lat, lng);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
            mMap.setMinZoomPreference(10);
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                place1 = new MarkerOptions().position(new LatLng(lat, lng)).title("موقعك");
                place2 = new MarkerOptions().position(new LatLng(marker.getPosition().latitude, marker.getPosition().longitude)).title(marker.getTitle());
                new FetchURL(MapsActivity.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");
                return false;
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }

    }

    private void hideProgressBar(ProgressBar progressBar) {
        progressBar.setVisibility(View.GONE);
    }

    private void showProgressBar(ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
    }


    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.gps_alert))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    protected void onPause() {
        super.onPause();
        isPaused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPaused){
            mMap.clear();
            getLocation();
        }
    }


    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }


    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

}