package com.example.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    TextView longitudev;
    TextView latitudev;
    TextView accurcyv;
    TextView altitudev;
    TextView countryv;
    TextView cityv;
    TextView addressv;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Listening();
            }
        }
    public void Listening() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    public void UpdateLocationInfo( Location location) {
        longitudev = findViewById(R.id.longitude);
        latitudev = findViewById(R.id.latitude);
        accurcyv = findViewById(R.id.accurcy);
        altitudev = findViewById(R.id.altitude);
        countryv = findViewById(R.id.country);
        cityv = findViewById(R.id.city);
        addressv = findViewById(R.id.address);

        longitudev.setText("Longitude: " + Double.toString(location.getLongitude()));
        latitudev.setText("Latitude: " + Double.toString(location.getLatitude()));
        accurcyv.setText("Accuracy: " + Double.toString(location.getAccuracy()));
        altitudev.setText("Altitude: " + Double.toString(location.getAltitude()));


        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (addressList != null && addressList.size() > 0) {
                String address = "";
                String country = "";
                String city = "";

                if (addressList.get(0).getCountryName() != null) {
                    country += addressList.get(0).getCountryName();
                }
                if (addressList.get(0).getAdminArea() != null) {
                    city += addressList.get(0).getAdminArea();
                }

               if (addressList.get(0).getAddressLine(0) != null) {
                    address += addressList.get(0).getAddressLine(0);
                }
                addressv.setText(address.toString());
                countryv.setText(country.toString());
                cityv.setText(city.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                UpdateLocationInfo(location);
            }
        };


            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
            else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lastKnownLocation !=null){
                   UpdateLocationInfo(lastKnownLocation);
                }
            }
        }
    }
