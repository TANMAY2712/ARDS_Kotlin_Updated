package com.ards.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ards.databinding.ActivityLoginBinding
import com.ards.ui.otp.OtpActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Observe loader state
        loginViewModel.isLoading.observe(this) { isLoading ->
            binding.progress.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        binding.btnSendOtp.setOnClickListener {
            val phoneNumber = binding.etPhoneNumber.text.toString()
            sendOtp(phoneNumber)
        }
    }

    private fun sendOtp(phone: String) {
        loginViewModel.sendOtp(phone).observe(this) { result ->
            result.onSuccess { response ->
                Toast.makeText(this, "OTP Sent Successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, OtpActivity::class.java)
                intent.putExtra("userMobileNumber", phone)
                startActivity(intent)
            }

            result.onFailure { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
