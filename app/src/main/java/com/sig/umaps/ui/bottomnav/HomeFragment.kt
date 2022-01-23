package com.sig.umaps.ui.bottomnav

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sig.umaps.R
import com.sig.umaps.databinding.FragmentHomeBinding
import com.sig.umaps.ui.about.AboutActivity
import com.sig.umaps.ui.facility.BuildingActivity
import com.sig.umaps.ui.facility.FacultyActivity
import com.sig.umaps.utils.SaveData.PREFS_NAME
import com.sig.umaps.utils.SaveData.PREFS_USER_NAME

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding as FragmentHomeBinding
    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref =
            requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        val name = sharedPref.getString(PREFS_USER_NAME, null)

        binding.apply {
            helloName.text = getString(R.string.hello, name)
            layoutMenu.apply {
                layoutMenuHome.apply {
                    menuBuilding.setOnClickListener {
                        Intent(activity, BuildingActivity::class.java).apply {
                            putExtra("EXTRA_BUILDING", "GEDUNG")
                            startActivity(this)
                        }
                    }

                    menuFaculty.setOnClickListener {
                        Intent(activity, FacultyActivity::class.java).apply {
                            putExtra("EXTRA_FACULTY", "FAKULTAS")
                            startActivity(this)
                        }
                    }

                    menuAbout.setOnClickListener {
                        Intent(activity, AboutActivity::class.java).apply {
                            startActivity(this)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}