package com.example.projectgroup2.ui.main.akun.editprofile

import android.app.Activity
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.avatarfirst.avatargenlib.AvatarGenerator
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.projectgroup2.R
import com.example.projectgroup2.databinding.FragmentEditProfileBinding
import com.example.projectgroup2.ui.main.akun.AkunFragment.Companion.USER_ADDRESS
import com.example.projectgroup2.ui.main.akun.AkunFragment.Companion.USER_CITY
import com.example.projectgroup2.ui.main.akun.AkunFragment.Companion.USER_IMAGE
import com.example.projectgroup2.ui.main.akun.AkunFragment.Companion.USER_NAME
import com.example.projectgroup2.ui.main.akun.AkunFragment.Companion.USER_PHONE_NUMBER
import com.example.projectgroup2.utils.uriToFile
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class EditProfileFragment : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EditProfileViewModel by viewModels()
    private var uri: String = ""
    private var fileImage: File? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binView()

        viewModel.getUserData()
    }

    private fun binView() {
        viewModel.showUser.observe(viewLifecycleOwner){
            binding.apply {
                etNamaUser.setText(it.fullName.toString())
                etKotaUser.setText(it.city.toString())
                etAlamatUser.setText(it.address.toString())
                etNohpUser.setText(it.phoneNumber.toString())
                Glide.with(requireContext())
                    .load(it.imageUrl)
                    .placeholder(
                        AvatarGenerator
                        .AvatarBuilder(requireContext())
                        .setTextSize(50)
                        .setAvatarSize(200)
                        .toSquare()
                        .setLabel(it.fullName.toString())
                        .build())
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(50)))
                    .into(ivProfileImage)
            }
        }

        binding.cardBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.ivProfileImage.setOnClickListener {
            openImagePicker()
        }

        binding.btnSimpanEdit.setOnClickListener {
            resetErrors()
            val nama = binding.etNamaUser.text.toString()
            val kota = binding.etKotaUser.text.toString()
            val alamat = binding.etAlamatUser.text.toString()
            val noHp = binding.etNohpUser.text.toString()
            val isValid = validation(nama, kota, alamat, noHp)
            if (isValid) {
                    viewModel.updateUser(
                        fileImage,
                        nama,
                        noHp,
                        alamat,
                        kota
                    )
                Toast.makeText(requireContext(), "Berhasil edit akun!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_editProfileFragment_to_akunFragment)
            }
        }

        viewModel.showError.observe(viewLifecycleOwner) {
            val snackbar = Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG)
            snackbar.view.setBackgroundColor(Color.RED)
            snackbar.show()
        }
    }

    private fun resetErrors() {
        binding.apply {
            etNamaUser.error = null
            etKotaUser.error = null
            etAlamatUser.error = null
            etNohpUser.error = null
        }
    }

    private fun validation(
        nama: String,
        kota: String,
        alamat: String,
        nohp: String
    ): Boolean {
        return when {
            nama.isEmpty() -> {
                binding.etNamaUser.error = "Nama tidak boleh kosong!"
                false
            }
            kota.isEmpty() -> {
                binding.etKotaUser.error = "Kota tidak boleh kosong!"
                false
            }
            alamat.isEmpty() -> {
                binding.etAlamatUser.error = "Alamat tidak boleh kosong!"
                false
            }
            nohp.isEmpty() -> {
                binding.etNohpUser.error = "Nomor Hp tidak boleh kosong!"
                false
            }
            else -> {
                true
            }
        }
    }

    private fun loadImage(uri: Uri) {
        binding.apply {
            Glide.with(binding.root)
                .load(uri)
                .transform(CenterCrop(), RoundedCorners(12))
                .into(ivProfileImage)
        }
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Image Uri will not be null for RESULT_OK
                    val fileUri = data?.data
                    uri = fileUri.toString()
                    if (fileUri != null) {
                        fileImage = uriToFile(fileUri, requireContext())
                        loadImage(fileUri)
                    }
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {

                }
            }
        }

    private fun openImagePicker() {
        ImagePicker.with(this)
            .crop()
            .saveDir(
                File(
                    requireContext().externalCacheDir,
                    "ImagePicker"
                )
            ) //Crop image(Optional), Check Customization for more option
            .compress(1024) //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                1080,
                1080
            )    //Final image resolution will be less than 1080 x 1080(Optional)
            .createIntent { intent ->
                startForProfileImageResult.launch(intent)
            }
    }

}