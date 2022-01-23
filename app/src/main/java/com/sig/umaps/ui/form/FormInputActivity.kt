package com.sig.umaps.ui.form

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.DatePicker
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.sig.umaps.R
import com.sig.umaps.databinding.ActivityFormInputBinding
import com.sig.umaps.ui.bottomnav.NavigationContainerActivity
import com.sig.umaps.utils.SaveData.PREFS_BIRTH_DATE
import com.sig.umaps.utils.SaveData.PREFS_EMAIL
import com.sig.umaps.utils.SaveData.PREFS_IMAGE
import com.sig.umaps.utils.SaveData.PREFS_NAME
import com.sig.umaps.utils.SaveData.PREFS_TOKEN
import com.sig.umaps.utils.SaveData.PREFS_USERID
import com.sig.umaps.utils.SaveData.PREFS_USER_NAME
import com.sig.umaps.utils.SaveData.PREFS_USER_STATUS
import com.sig.umaps.viewmodel.FormViewModel
import java.text.SimpleDateFormat
import java.util.*

class FormInputActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormInputBinding
    private lateinit var calendar: Calendar
    private lateinit var sharedPref: SharedPreferences
    private val formViewModel by viewModels<FormViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormInputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        calendar = Calendar.getInstance()
        sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        val image = sharedPref.getString(PREFS_IMAGE, null)
        val name = sharedPref.getString(PREFS_USER_NAME, null)
        val email = sharedPref.getString(PREFS_EMAIL, null)
        val accessToken = sharedPref.getString(PREFS_TOKEN, null)
        val userId = sharedPref.getInt(PREFS_USERID, 0)

        setData(image, name, email, accessToken, userId)
        setCalendar()
        enabledButton()
    }

    private fun setData(
        image: String?,
        name: String?,
        email: String?,
        accessToken: String?,
        userId: Int?
    ) {
        binding.apply {
            Glide.with(this@FormInputActivity)
                .load(image)
                .into(imgProfile)
            edtName.setText(name)
            edtEmail.setText(email)

            btnRegister.setOnClickListener {
                formViewModel.apply {
                    val birthDate = edtDate.text.toString()
                    val userStatus = edtStatus.text.toString()
                    val editor = sharedPref.edit()
                    editor.putString(PREFS_BIRTH_DATE, birthDate)
                    editor.putString(PREFS_USER_STATUS, userStatus)
                    editor.apply()
                    putDataUser(
                        accessToken,
                        userId,
                        image,
                        birthDate,
                        userStatus
                    )
                    isLoading.observe(this@FormInputActivity, { isLoading ->
                        showLoading(isLoading)
                    })

                    Intent(this@FormInputActivity, NavigationContainerActivity::class.java).apply {
                        startActivity(this)
                    }
                }
            }
        }
    }

    private fun setCalendar() {
        val date =
            DatePickerDialog.OnDateSetListener { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val format = "yyyy-MM-dd"
                val simpleDateFormat = SimpleDateFormat(format, Locale.US)
                binding.edtDate.setText(simpleDateFormat.format(calendar.time))
            }

        binding.edtDate.setOnClickListener {
            DatePickerDialog(
                this,
                date,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun enabledButton() {
        val registerTextWatcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.apply {
                    val nameInput = edtName.text.toString()
                    val emailInput = edtName.text.toString()
                    val dateInput = edtDate.text.toString()
                    val statusInput = edtStatus.text.toString()

                    if (nameInput.isNotEmpty() && emailInput.isNotEmpty() && dateInput.isNotEmpty() && statusInput.isNotEmpty()) {
                        btnRegister.isEnabled = true
                        btnRegister.setBackgroundResource(R.drawable.bg_button)
                    } else {
                        btnRegister.isEnabled = false
                        btnRegister.setBackgroundResource(R.drawable.bg_button_disabled)
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        }
        binding.apply {
            edtName.addTextChangedListener(registerTextWatcher)
            edtEmail.addTextChangedListener(registerTextWatcher)
            edtDate.addTextChangedListener(registerTextWatcher)
            edtStatus.addTextChangedListener(registerTextWatcher)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingAnimations.visibility = View.VISIBLE

        } else {
            binding.loadingAnimations.visibility = View.GONE
        }
    }
}