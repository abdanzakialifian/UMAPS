package com.sig.umaps.ui.facility

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.sig.umaps.databinding.ActivityFacultyBinding
import com.sig.umaps.model.FacilitiesResponseItem
import com.sig.umaps.ui.adapter.FacilityAdapter
import com.sig.umaps.utils.SaveData.PREFS_ADDRESS
import com.sig.umaps.utils.SaveData.PREFS_FACILITY_BACKGROUND
import com.sig.umaps.utils.SaveData.PREFS_FACILITY_ID
import com.sig.umaps.utils.SaveData.PREFS_FACILITY_NAME
import com.sig.umaps.utils.SaveData.PREFS_FACILITY_RATING
import com.sig.umaps.utils.SaveData.PREFS_NAME
import com.sig.umaps.utils.SaveData.PREFS_TOKEN
import com.sig.umaps.viewmodel.FacilityViewModel

class FacultyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFacultyBinding
    private lateinit var sharedPref: SharedPreferences
    private val facilityViewModel by viewModels<FacilityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFacultyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        val accessToken = sharedPref.getString(PREFS_TOKEN, null)
        val faculty = intent.getStringExtra("EXTRA_FACULTY")

        facilityViewModel.apply {
            getFacility(accessToken, "faculty")
            isLoading.observe(this@FacultyActivity, { isLoading ->
                showLoading(isLoading)
            })
            data.observe(this@FacultyActivity, { data ->
                binding.apply {
                    rvFaculty.layoutManager = LinearLayoutManager(this@FacultyActivity)
                    val facilityAdapter = FacilityAdapter(data)
                    rvFaculty.adapter = facilityAdapter
                    rvFaculty.setHasFixedSize(true)

                    facilityAdapter.setItemClickCallBack(object :
                        FacilityAdapter.IOnItemClickCallback {
                        override fun onItemClicked(facility: FacilitiesResponseItem) {
                            val editor = sharedPref.edit()
                            editor.putString(
                                PREFS_FACILITY_BACKGROUND,
                                facility.portraitImageUrl
                            )
                            editor.putString(PREFS_FACILITY_NAME, facility.name)
                            editor.putString(PREFS_FACILITY_RATING, facility.rate)
                            editor.putInt(PREFS_FACILITY_ID, facility.id as Int)
                            editor.putString(PREFS_ADDRESS, facility.address)
                            editor.apply()
                            Intent(this@FacultyActivity, SearchFacilityActivity::class.java).apply {
                                startActivity(this)
                            }
                        }
                    })

                    srcView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            return false
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {
                            facilityAdapter.filter.filter(newText)
                            return true
                        }
                    })

                    tvTitle.text = faculty
                    btnBack.setOnClickListener {
                        onBackPressed()
                    }
                }
            })
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.apply {
                shimmerFaculty.startShimmer()
                shimmerFaculty.visibility = View.VISIBLE
                rvFaculty.visibility = View.GONE
            }
        } else {
            binding.apply {
                shimmerFaculty.stopShimmer()
                shimmerFaculty.visibility = View.GONE
                rvFaculty.visibility = View.VISIBLE
            }
        }
    }
}