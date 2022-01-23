package com.sig.umaps.ui.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sig.umaps.databinding.FragmentOnBoardingThreeBinding
import com.sig.umaps.helper.OnBoardingPreferences
import com.sig.umaps.helper.OnBoardingViewModelFactory
import com.sig.umaps.ui.login.LoginActivity
import com.sig.umaps.viewmodel.OnBoardingPreferencesViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "started")

class OnBoardingThreeFragment : Fragment() {

    private var _binding: FragmentOnBoardingThreeBinding? = null
    private val binding get() = _binding as FragmentOnBoardingThreeBinding

    private var isStarted = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnBoardingThreeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val pref = OnBoardingPreferences.getInstance(activity?.dataStore as DataStore)
        val onBoardingPreferencesViewModel =
            ViewModelProvider(
                requireActivity(),
                OnBoardingViewModelFactory(pref)
            )[OnBoardingPreferencesViewModel::class.java]

        onBoardingPreferencesViewModel.getStarted().observe(viewLifecycleOwner, { isStartedActive ->
            isStarted = if (isStartedActive) {
                Intent(activity, LoginActivity::class.java).apply {
                    startActivity(this)
                    activity?.finish()
                }
                false
            } else {
                true
            }
        })

        binding.layoutOnBoardingThree.apply {
            btnGettingStarted.setOnClickListener {
                onBoardingPreferencesViewModel.saveStarted(isStarted)
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}