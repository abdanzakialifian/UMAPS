package com.sig.umaps.ui.bottomnav

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.sig.umaps.R
import com.sig.umaps.databinding.FragmentProfileBinding
import com.sig.umaps.ui.form.FormEditActivity
import com.sig.umaps.ui.login.LoginActivity
import com.sig.umaps.utils.SaveData.PREFS_IMAGE
import com.sig.umaps.utils.SaveData.PREFS_LOGIN
import com.sig.umaps.utils.SaveData.PREFS_NAME
import com.sig.umaps.utils.SaveData.PREFS_USER_NAME
import com.sig.umaps.utils.SaveData.PREFS_USER_STATUS

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding as FragmentProfileBinding
    private lateinit var gsc: GoogleSignInClient
    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref =
            requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        val image = sharedPref.getString(PREFS_IMAGE, null)
        val name = sharedPref.getString(PREFS_USER_NAME, null)
        val userStatus = sharedPref.getString(PREFS_USER_STATUS, null)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id_auth))
            .requestEmail()
            .build()
        gsc = GoogleSignIn.getClient(requireActivity(), gso)

        binding.apply {
            Glide.with(requireActivity())
                .load(image)
                .into(imgProfile)

            tvName.text = name
            tvStatus.text = userStatus
            btnEdit.setOnClickListener {
                Intent(activity, FormEditActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }


        binding.btnLogout.setOnClickListener {
            val builder = AlertDialog.Builder(activity)
            builder.setTitle(R.string.logout)
            builder.setMessage(R.string.are_you_sure)

            builder.setPositiveButton("Ya") { _, _ ->
                gsc.revokeAccess()
                    .addOnCompleteListener {
                        Intent(activity, LoginActivity::class.java).apply {
                            startActivity(this)
                            activity?.finish()
                        }
                    }
                val editor = sharedPref.edit()
                editor.putBoolean(PREFS_LOGIN, false)
                editor.apply()
            }

            builder.setNegativeButton("Tidak") { _, _ ->

            }
            val alertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}