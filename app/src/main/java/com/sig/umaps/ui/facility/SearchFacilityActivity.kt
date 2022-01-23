package com.sig.umaps.ui.facility

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.sig.umaps.databinding.ActivitySearchFacilityBinding
import com.sig.umaps.ui.maps.MapsActivity
import com.sig.umaps.utils.SaveData.PREFS_FACILITY_BACKGROUND
import com.sig.umaps.utils.SaveData.PREFS_FACILITY_NAME
import com.sig.umaps.utils.SaveData.PREFS_FACILITY_RATING
import com.sig.umaps.utils.SaveData.PREFS_NAME

class SearchFacilityActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchFacilityBinding
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchFacilityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        val background = sharedPref.getString(PREFS_FACILITY_BACKGROUND, null)
        val name = sharedPref.getString(PREFS_FACILITY_NAME, null)
        val rate = sharedPref.getString(PREFS_FACILITY_RATING, null)

        binding.apply {
            Glide.with(this@SearchFacilityActivity)
                .load(background)
                .into(imgBackgroundFacility)

            layoutSearchFacility.apply {
                tvNameFacility.text = name

                val rating = rate?.toFloat()
                ratingBar.stepSize = 0.1f
                if (rating != null) {
                    ratingBar.rating = rating
                }

                btnSearchLocation.setOnClickListener {
                    Intent(this@SearchFacilityActivity, MapsActivity::class.java).apply {
                        startActivity(this)
                    }
                }
            }
        }
    }
}