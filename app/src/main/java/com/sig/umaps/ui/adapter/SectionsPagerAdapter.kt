package com.sig.umaps.ui.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sig.umaps.ui.onboarding.OnBoardingOneFragment
import com.sig.umaps.ui.onboarding.OnBoardingThreeFragment
import com.sig.umaps.ui.onboarding.OnBoardingTwoFragment

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3
    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = OnBoardingOneFragment()
            1 -> fragment = OnBoardingTwoFragment()
            2 -> fragment = OnBoardingThreeFragment()
        }
        return fragment as Fragment
    }

}