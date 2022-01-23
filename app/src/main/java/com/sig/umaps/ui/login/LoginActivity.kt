package com.sig.umaps.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.sig.umaps.R
import com.sig.umaps.databinding.ActivityLoginBinding
import com.sig.umaps.model.LoginRequest
import com.sig.umaps.ui.bottomnav.NavigationContainerActivity
import com.sig.umaps.ui.form.FormInputActivity
import com.sig.umaps.utils.SaveData.PREFS_BIRTH_DATE
import com.sig.umaps.utils.SaveData.PREFS_EMAIL
import com.sig.umaps.utils.SaveData.PREFS_IMAGE
import com.sig.umaps.utils.SaveData.PREFS_LOGIN
import com.sig.umaps.utils.SaveData.PREFS_NAME
import com.sig.umaps.utils.SaveData.PREFS_TOKEN
import com.sig.umaps.utils.SaveData.PREFS_USERID
import com.sig.umaps.utils.SaveData.PREFS_USER_NAME
import com.sig.umaps.utils.SaveData.PREFS_USER_STATUS
import com.sig.umaps.viewmodel.FormViewModel
import com.sig.umaps.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences

    private lateinit var binding: ActivityLoginBinding
    private lateinit var gsc: GoogleSignInClient
    private lateinit var fAuth: FirebaseAuth

    private val loginViewModel by viewModels<LoginViewModel>()
    private val formViewModel by viewModels<FormViewModel>()

    private var accessToken: String? = null
    private var userId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id_auth))
            .requestEmail()
            .build()
        gsc = GoogleSignIn.getClient(this, gso)

        fAuth = FirebaseAuth.getInstance()

        Log.d("TOKEN", accessToken.toString())

        binding.layoutLogin.apply {
            btnLogin.setOnClickListener {
                signIn()
                val editor = sharedPref.edit()
                editor.putBoolean(PREFS_LOGIN, true)
                editor.apply()
            }
        }
    }

    private fun signIn() {
        val signInIntent = gsc.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val sat = GoogleSignIn.getSignedInAccountFromIntent(data)
            if (sat.isSuccessful) {
                try {
                    val gsa = sat.getResult(ApiException::class.java)
                    val authCredential = GoogleAuthProvider.getCredential(gsa.idToken, null)
                    fAuth.signInWithCredential(authCredential)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                checkLogin()
                            } else {
                                Toast.makeText(
                                    this,
                                    "Authentication Failed : " + task.exception?.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } catch (e: ApiException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun checkLogin() {
        val user = fAuth.currentUser

        user?.apply {
            val loginRequest =
                LoginRequest(displayName, email, photoUrl.toString())

            loginViewModel.apply {
                sendLoginData(loginRequest)

                isLoading.observe(this@LoginActivity, { isLoading ->
                    showLoading(isLoading)
                })
                data.observe(this@LoginActivity, { data ->
                    val editor = sharedPref.edit()
                    editor.putString(PREFS_TOKEN, data.accessToken)
                    editor.putInt(PREFS_USERID, data.userId as Int)
                    editor.putString(PREFS_IMAGE, user.photoUrl.toString())
                    editor.putString(PREFS_USER_NAME, user.displayName)
                    editor.putString(PREFS_EMAIL, user.email)
                    editor.apply()

                    formViewModel.apply {
                        accessToken = sharedPref.getString(PREFS_TOKEN, null)
                        userId = sharedPref.getInt(PREFS_USERID, 0)
                        getDataUser(accessToken, userId)
                        getData.observe(this@LoginActivity, { data ->
                            editor.putString(PREFS_BIRTH_DATE, data.birthDate.toString())
                            editor.putString(PREFS_USER_STATUS, data.userStatus.toString())
                            editor.apply()
                            if (data.birthDate == null && data.userStatus == null) {
                                goToFormPage()
                            } else {
                                goToHomePage()
                            }
                        })
                    }
                })
            }
        }
    }

    private fun goToHomePage() {
        Intent(this@LoginActivity, NavigationContainerActivity::class.java).apply {
            startActivity(this)
            finish()
        }

    }

    private fun goToFormPage() {
        Intent(this@LoginActivity, FormInputActivity::class.java).apply {
            startActivity(this)
            finish()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingAnimations.visibility = View.VISIBLE
        } else {
            binding.loadingAnimations.visibility = View.GONE
        }
    }

    companion object {
        private const val RC_SIGN_IN = 100
    }
}