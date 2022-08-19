package com.example.projectgroup2.ui.main.akun

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.avatarfirst.avatargenlib.AvatarConstants
import com.avatarfirst.avatargenlib.AvatarGenerator
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.projectgroup2.R
import com.example.projectgroup2.databinding.FragmentAkunBinding
import com.example.projectgroup2.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AkunFragment : Fragment() {
    private lateinit var binding: FragmentAkunBinding
    private val viewModel: AkunViewModel by viewModels()

    private val bundle = Bundle()
    companion object {
        const val USER_NAME = "fullname"
        const val USER_EMAIL = "email"
        const val USER_PHONE_NUMBER = "phone_number"
        const val USER_ADDRESS = "address"
        const val USER_CITY = "city"
        const val USER_IMAGE = "image_url"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAkunBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        bindView()

        viewModel.getUserData()
    }

    private fun bindViewModel() {
        viewModel.showLogin.observe(viewLifecycleOwner){
            Intent(activity, LoginActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }
        }

        viewModel.showUser.observe(viewLifecycleOwner){
            Glide.with(requireContext())
                .load(it.imageUrl.toString())
                .placeholder(AvatarGenerator
                    .AvatarBuilder(requireContext())
                    .setTextSize(50)
                    .setAvatarSize(200)
                    .toSquare()
                    .setLabel(it.fullName.toString())
                    .build())
                .apply(RequestOptions.bitmapTransform(RoundedCorners(50)))
                .into(binding.ivProfileAcc)
            bundle.putString(USER_IMAGE, it.imageUrl)

            binding.tvProfileName.text = it.fullName
            binding.tvProfileAddress.text = it.address
        }
    }

    private fun bindView(){
        binding.tvLogout.setOnClickListener {
            viewModel.clearCredential()
        }

        binding.tvEdit.setOnClickListener {
            findNavController().navigate(R.id.action_akunFragment_to_editProfileFragment)
        }
    }
}