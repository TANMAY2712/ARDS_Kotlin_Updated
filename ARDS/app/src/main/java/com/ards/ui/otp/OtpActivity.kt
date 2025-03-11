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
                PreferenceHelper.putBoolean("isLoggedIn", true)
                Toast.makeText(this, "OTP Verified Successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }

            result.onFailure { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
