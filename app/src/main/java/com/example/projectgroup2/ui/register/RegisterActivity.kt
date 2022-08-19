package com.example.projectgroup2.ui.register

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import com.example.projectgroup2.R
import com.example.projectgroup2.databinding.ActivityOnBoardingBinding
import com.example.projectgroup2.databinding.ActivityRegisterBinding
import com.example.projectgroup2.ui.login.LoginActivity
import com.example.projectgroup2.ui.main.MainActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegisterViewModel by viewModels()
    private val progressDialog: ProgressDialog by lazy { ProgressDialog(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindView()
        bindViewModel()
    }

    private fun bindViewModel() {
        viewModel.shouldShowError.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.shouldShowLoading.observe(this) {
            val dialogCustom = Dialog(this)
            dialogCustom.setContentView(R.layout.alert_loading)
            dialogCustom.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogCustom.setCancelable(false)
            if (it) {
                dialogCustom.show()
            } else {
                dialogCustom.dismiss()
            }
        }

//        viewModel.shouldOpenUpdateProfile.observe(this) {
//            if (it) {
//                val intent = Intent(this, MainActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                startActivity(intent)
//            }
//        }

        viewModel.shouldOpenLoginPage.observe(this){
            if (it) {
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

        viewModel.showConfirmation.observe(this){
            val dialogCustom = Dialog(this)
            dialogCustom.setContentView(R.layout.alert_confirmation_register)
            dialogCustom.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogCustom.show()

            dialogCustom.findViewById<Button>(R.id.btnYa).setOnClickListener{
                viewModel.processToSignUp()
                dialogCustom.dismiss()
            }
            dialogCustom.findViewById<TextView>(R.id.tv_cancel).setOnClickListener {
                dialogCustom.dismiss()
            }
        }
    }

    private fun bindView() {
        binding.etNamaRegister.doAfterTextChanged {
            viewModel.onChangeFullName(it.toString())
        }
        binding.etEmailRegister.doAfterTextChanged {
            viewModel.onChangeEmail(it.toString())
        }
        binding.etEmailPasswordRegister.doAfterTextChanged {
            viewModel.onChangePassword(it.toString())
        }
        binding.etPhoneRegister.doAfterTextChanged {
            viewModel.onChangePhoneNumber(it.toString())
        }
        binding.etCityRegister.doAfterTextChanged {
            viewModel.onChangeCity(it.toString())
        }
        binding.etAddressRegister.doAfterTextChanged {
            viewModel.onChangeAddress(it.toString())
        }
        binding.btnDaftarRegister.setOnClickListener {
            viewModel.onValidate()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}