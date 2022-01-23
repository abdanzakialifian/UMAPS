package com.sig.umaps.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.sig.umaps.R
import com.sig.umaps.helper.OnBoardingPreferences
import com.sig.umaps.helper.OnBoardingViewModelFactory
import com.sig.umaps.ui.bottomnav.NavigationContainerActivity
import com.sig.umaps.ui.login.LoginActivity
import com.sig.umaps.ui.onboarding.ContainerActivity
import com.sig.umaps.utils.SaveData.PREFS_LOGIN
import com.sig.umaps.utils.SaveData.PREFS_NAME
import com.sig.umaps.viewmodel.OnBoardingPreferencesViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "started")

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val logoSplash = findViewById<TextView>(R.id.splash_screen)
        val slideAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_splash)
        logoSplash.startAnimation(slideAnimation)

        sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val isLogin = sharedPref.getBoolean(PREFS_LOGIN, false)
        Log.d("ISLOGIN", isLogin.toString())

        val pref = OnBoardingPreferences.getInstance(dataStore)
        val onBoardingPreferencesViewModel =
            ViewModelProvider(
                this,
                OnBoardingViewModelFactory(pref)
            )[OnBoardingPreferencesViewModel::class.java]

        onBoardingPreferencesViewModel.getStarted().observe(this, { isStartedActive ->
            if (isStartedActive) {
                if (isLogin) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        Intent(this, NavigationContainerActivity::class.java).apply {
                            startActivity(this)
                            finish()
                        }
                    }, DELAY_SPLASH)
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        Intent(this, LoginActivity::class.java).apply {
                            startActivity(this)
                            finish()
                        }
                    }, DELAY_SPLASH)
                }
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    Intent(this, ContainerActivity::class.java).apply {
                        startActivity(this)
                        finish()
                    }
                }, DELAY_SPLASH)
            }
        })
    }

    companion object {
        const val DELAY_SPLASH = 3000L
    }
}