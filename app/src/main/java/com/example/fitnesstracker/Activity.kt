package com.example.fitnesstracker

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesstracker.utils.DateAdapter
import com.example.fitnesstracker.utils.DateUtils
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.text.SimpleDateFormat
import java.util.*

class Activity : Fragment(), DateAdapter.OnDateClickListener, OnMapReadyCallback, SensorEventListener {

    private lateinit var mMap: GoogleMap
    private lateinit var mapPanel: LinearLayout
    private lateinit var sensorManager: SensorManager
    private var stepCounter: Sensor? = null
    private var totalSteps = 0f
    private var previousTotalSteps = 0f
    private lateinit var footStepsTextView: TextView

    // Declare polyline options and polyline at the class level
    private lateinit var polylineOptions: PolylineOptions
    private lateinit var currentPolyline: Polyline

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            // Additional initialization code if needed
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_activity, container, false)

        val dateRecyclerView: RecyclerView = view.findViewById(R.id.dateRecyclerView)
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        dateRecyclerView.layoutManager = linearLayoutManager

        val calendar = Calendar.getInstance()
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        val dates = DateUtils().generateDates(month, year)
        val adapter = DateAdapter(dates, this)
        dateRecyclerView.adapter = adapter

        // Scroll to and center today's date
        val sdf = SimpleDateFormat("dd\nMMM", Locale.getDefault())
        val currentDate = sdf.format(Date())
        val todayPosition = dates.indexOf(currentDate)
        if (todayPosition != -1) {
            dateRecyclerView.post {
                val offset = (dateRecyclerView.width / 2) - (view.findViewById<View>(R.id.dateTextView).width / 2)
                linearLayoutManager.scrollToPositionWithOffset(todayPosition, offset)
            }
        }

        // Map initialization
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Map Panel click event
        mapPanel = view.findViewById(R.id.mapPanel)
        mapPanel.setOnClickListener {
            val intent = Intent(context, Map1::class.java)
            startActivity(intent)
        }

        // Initialize step counter
        footStepsTextView = view.findViewById(R.id.footSteps)
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        // Register the sensor listener
        sensorManager.registerListener(this, stepCounter, SensorManager.SENSOR_DELAY_UI)

        // Initialize location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style))

        polylineOptions = PolylineOptions().width(5f).color(Color.RED)
        currentPolyline = mMap.addPolyline(polylineOptions)

        // Enable My Location layer if permissions are granted
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            // Start location updates
            setupLocationUpdates()
        } else {
            // Request location permissions
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun setupLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            interval = 5000 // Update interval in milliseconds
            fastestInterval = 2000 // Fastest update interval in milliseconds
            priority = Priority.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    // Add the new point to the polyline
                    val points = currentPolyline.points
                    points.add(currentLatLng)
                    currentPolyline.points = points

                    // Optionally, add a marker at the current location
                    // mMap.addMarker(MarkerOptions().position(currentLatLng).title("You're here"))

                    // Move the camera to the current location
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15.0f))

                    Log.d("LocationUpdate", "Location: ${location.latitude}, ${location.longitude}")
                }
            }
        }

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permissions granted, initialize map
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    mMap.isMyLocationEnabled = true
                    setupLocationUpdates()
                }
            } else {
                // Permission denied - handle appropriately (e.g., show a message)
            }
        }
    }

    override fun onDateClick(date: String) {
        val dateInformation = retrieveDateInformation(date)
        // Handle date click events here
    }

    private fun retrieveDateInformation(date: String): DateUtils {
        return DateUtils()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
            totalSteps = event.values[0]
            val currentSteps = (totalSteps - previousTotalSteps).toInt()
            if (currentSteps >= 0) {
                updateStepCount(currentSteps)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private fun updateStepCount(steps: Int) {
        footStepsTextView.text = steps.toString()
    }

    override fun onPause() {
        super.onPause()
        // Unregister sensor listener
        sensorManager.unregisterListener(this)
        // Remove location updates
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onResume() {
        super.onResume()
        // Re-register sensor listener
        sensorManager.registerListener(this, stepCounter, SensorManager.SENSOR_DELAY_UI)
        // Re-request location updates if needed
        if (::mMap.isInitialized && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            setupLocationUpdates()
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Activity().apply {
                arguments = Bundle().apply {
                    // Additional parameters if needed
                }
            }
    }
}
