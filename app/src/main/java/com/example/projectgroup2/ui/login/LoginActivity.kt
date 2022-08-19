package com.example.projectgroup2.ui.login

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import com.example.projectgroup2.R
import com.example.projectgroup2.databinding.ActivityLoginBinding
import com.example.projectgroup2.databinding.ActivityRegisterBinding
import com.example.projectgroup2.ui.main.MainActivity
import com.example.projectgroup2.ui.onboarding.OnBoardingActivity
import com.example.projectgroup2.ui.register.RegisterActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.lang.StringBuilder

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindViewModel()
        bindView()
    }

    private fun bindViewModel() {
        viewModel.shouldShowError.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.shouldOpenHomePage.observe(this) {
            if (it) {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

        val dialogCustom = Dialog(this)
        dialogCustom.setContentView(R.layout.alert_loading)
        dialogCustom.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogCustom.setCancelable(false)
        viewModel.showLoading.observe(this) {
            if (it) {
                dialogCustom.show()
            } else {
                dialogCustom.dismiss()
            }
        }
    }

    private fun bindView() {
        val smilingFaceUnicode = 0x1F44B
        val stringBuilder1 = StringBuilder()
        val emoteSmile = stringBuilder1.append(Character.toChars(smilingFaceUnicode))

        binding.tvHeader.text = "Selamat datang kembali$emoteSmile"

        binding.etEmailLogin.doAfterTextChanged {
            viewModel.onChangeEmail(it.toString())
        }

        binding.etPasswordLogin.doAfterTextChanged {
            viewModel.onChangePassword(it.toString())
        }

        binding.btnMasukLogin.setOnClickListener {
            viewModel.onClickSignIn()
        }

        binding.tvGoToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.ivBack.setOnClickListener {
            startActivity(Intent(this, OnBoardingActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}