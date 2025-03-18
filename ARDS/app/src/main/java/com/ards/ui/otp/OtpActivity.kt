package com.ards.ui.otp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ards.MainActivity
import com.ards.databinding.ActivityOtpBinding
import com.ards.sharedpreference.PreferenceHelper

class OtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpBinding
    private lateinit var mobileNumber: String
    private val verifyViewModel: OtpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mobileNumber = intent.getStringExtra("userMobileNumber").toString()
        binding.tvPhoneNumber.text = mobileNumber

        verifyViewModel.isLoading.observe(this) { isLoading ->
            binding.progress.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        binding.btnVerify.setOnClickListener {
            val otp = binding.pinView.text.toString()

            if (otp.length == 6) {
                verifyOtp(otp.toInt())
            } else {
                Toast.makeText(this, "Enter a valid 6-digit OTP", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun verifyOtp(otp: Int) {
        verifyViewModel.verifyOTP(mobileNumber, otp).observe(this) { result ->

            result.onSuccess {
                PreferenceHelper.getInstance(this).putBoolean("isLoggedIn", true)
                Toast.makeText(this, "OTP Verified Successfully", Toast.LENGTH_SHORT).show()
                loginApi()
            }

            result.onFailure { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginApi() {
        verifyViewModel.siginIn(mobileNumber).observe(this) { result ->

            result.onSuccess { t ->
                PreferenceHelper.getInstance(this).putBoolean("isLoggedIn", true)
                //PreferenceHelper.getInstance(this).setLogin(true)
                PreferenceHelper.getInstance(this).setAuthToken(t.Data.AuthToken)
                PreferenceHelper.getInstance(this).setDOB(t.Data.DOB)
                PreferenceHelper.getInstance(this).setUserName(t.Data.Username)
                PreferenceHelper.getInstance(this).setFULLName(t.Data.FullName)
                PreferenceHelper.getInstance(this).setParentId(t.Data.ParentUnionId)
                PreferenceHelper.getInstance(this).setpfnumber(t.Data.PFNumber)
                PreferenceHelper.getInstance(this).setFathername(t.Data.FatherName)
                PreferenceHelper.getInstance(this).setUserImage(t.Data.ProfileImage.toString())
                PreferenceHelper.getInstance(this).setUserAddress(t.Data.OfficeAddress)
                PreferenceHelper.getInstance(this).setParentUnionName(t.Data.ParentUnionName)
                PreferenceHelper.getInstance(this).setRoleId(t.Data.RoleId)
                PreferenceHelper.getInstance(this).setUserZoneID(t.Data.ZoneId)
                PreferenceHelper.getInstance(this).setUserDivisionID(t.Data.DivisionId)
                PreferenceHelper.getInstance(this).setUserBranchID(t.Data.BranchId)
                PreferenceHelper.getInstance(this).setEmailId("")
                PreferenceHelper.getInstance(this).setUserAge(t.Data.Age)
                PreferenceHelper.getInstance(this).setAdmin(t.Data.IsAdmin)
                PreferenceHelper.getInstance(this).setStudent(t.Data.IsStudent)
                PreferenceHelper.getInstance(this).setUserId(t.Data.UserId)
                //getView().loginsuccess()
                Toast.makeText(this, "Logged In Successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }

            result.onFailure { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
