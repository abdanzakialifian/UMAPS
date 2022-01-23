package com.sig.umaps.ui.onboarding

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import androidx.annotation.IntegerRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.sig.umaps.R
import com.sig.umaps.ui.adapter.SectionsPagerAdapter

class ContainerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)

        supportActionBar?.hide()

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tab_layout)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.icon = ContextCompat.getDrawable(this, TAB_ROUNDED[position])

            tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        tab?.icon?.colorFilter = BlendModeColorFilter(
                            ContextCompat.getColor(
                                this@ContainerActivity,
                                R.color.end_color
                            ), BlendMode.SRC_ATOP
                        )
                    } else {
                        tab?.icon?.setColorFilter(
                            ContextCompat.getColor(
                                this@ContainerActivity,
                                R.color.end_color
                            ), PorterDuff.Mode.SRC_ATOP
                        )
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        tab?.icon?.colorFilter = BlendModeColorFilter(
                            ContextCompat.getColor(
                                this@ContainerActivity,
                                R.color.grey_swipe
                            ), BlendMode.SRC_ATOP
                        )
                    } else {
                        tab?.icon?.setColorFilter(
                            ContextCompat.getColor(
                                this@ContainerActivity,
                                R.color.grey_swipe
                            ), PorterDuff.Mode.SRC_ATOP
                        )
                    }
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            })
        }.attach()
    }

    companion object {
        @IntegerRes
        private val TAB_ROUNDED = intArrayOf(
            R.drawable.ic_circle_swipe,
            R.drawable.ic_circle_swipe,
            R.drawable.ic_circle_swipe
        )
    }
}