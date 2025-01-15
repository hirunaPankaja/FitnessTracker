package com.example.fitnesstracker

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.example.fitnesstracker.databinding.ActivityMap1Binding

class Map1 : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMap1Binding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var polylineOptions: PolylineOptions
    private lateinit var currentPolyline: Polyline

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMap1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))

        val sriLanka = LatLng(7.8731, 80.7718)
        mMap.addMarker(MarkerOptions().position(sriLanka).title("Marker in Sri Lanka"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sriLanka, 7.0f))

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                1
            )
            return
        }
        mMap.isMyLocationEnabled = true

        // Initialize PolylineOptions and Polyline
        polylineOptions = PolylineOptions().width(5f).color(android.graphics.Color.RED)
        currentPolyline = mMap.addPolyline(polylineOptions)

        // Create LocationRequest
        val locationRequest = LocationRequest.create().apply {
            interval = 5000 // Update interval in milliseconds
            fastestInterval = 2000 // Fastest update interval in milliseconds
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        // Create LocationCallback to handle location updates
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    polylineOptions.add(currentLatLng)
                    currentPolyline.points = polylineOptions.points
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15.0f))
                }
            }
        }

        // Start location updates
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    override fun onStop() {
        super.onStop()
        // Stop location updates to save battery
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}
