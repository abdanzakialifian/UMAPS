package com.sig.umaps.ui.maps

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.sig.umaps.R
import com.sig.umaps.databinding.ActivityMapsBinding
import com.sig.umaps.utils.SaveData.PREFS_ADDRESS
import com.sig.umaps.utils.SaveData.PREFS_FACILITY_ID
import com.sig.umaps.utils.SaveData.PREFS_FACILITY_NAME
import com.sig.umaps.utils.SaveData.PREFS_NAME
import com.sig.umaps.utils.SaveData.PREFS_TOKEN
import com.sig.umaps.viewmodel.MapsViewModel

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var sharedPref: SharedPreferences
    private val mapsViewModel by viewModels<MapsViewModel>()
    private var id: Int? = null
    private var accessToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(
                R.id.map
            ) as SupportMapFragment
        mapFragment.getMapAsync(this)

        sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        id = sharedPref.getInt(PREFS_FACILITY_ID, 0)
        accessToken = sharedPref.getString(PREFS_TOKEN, null)
        val name = sharedPref.getString(PREFS_FACILITY_NAME, null)
        val address = sharedPref.getString(PREFS_ADDRESS, null)

        binding.layoutMaps.apply {
            tvNameFacility.text = name
            tvAddressFacility.text = address
            tvBack.setOnClickListener {
                onBackPressed()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mapsViewModel.apply {
            getFacilityById(accessToken, id)
            data.observe(this@MapsActivity, { data ->
                val latitude = data.latitude?.toDouble() as Double
                val longitude = data.longitude?.toDouble() as Double
                val name = data.name
                mMap = googleMap
                mMap.uiSettings.isZoomControlsEnabled = true
                val location = LatLng(latitude, longitude)
                mMap.addMarker(
                    MarkerOptions().position(location).title(name).draggable(true)
                )
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14f))
            })
        }
    }
}