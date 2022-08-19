package com.example.projectgroup2.ui.main.home.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.avatarfirst.avatargenlib.AvatarGenerator
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.projectgroup2.R
import com.example.projectgroup2.databinding.FragmentDetailsBinding
import com.example.projectgroup2.ui.main.home.HomeFragment.Companion.PRODUCT_ID
import com.example.projectgroup2.utils.currency
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailsViewModel by viewModels()
    var productName = ""
    var imageURL = ""
    private var isBid = false
    private lateinit var convertBasePrice: String
    private var basePrice = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()

        val bundle = arguments
        val productID = bundle?.getInt(PRODUCT_ID)
        if(productID != null){
            viewModel.getProductById(productID)
        }
    }

    private fun bindViewModel(){
        viewModel.showShimerProduct.observe(viewLifecycleOwner){
            if(it){
                binding.shimerDetailsProduct.visibility = View.VISIBLE
            }else{
                binding.shimerDetailsProduct.visibility = View.GONE
            }
        }

        viewModel.showProductDetails.observe(viewLifecycleOwner){
            if (it != null){
                productName = it.name
                imageURL = it.imageUrl

                //product name
                binding.tvProductName.text = it.name
                //product image
                Glide.with(this@DetailsFragment)
                    .load(it.imageUrl)
                    .into(binding.ivProductDetails)
                //product category
                var listCategory = ""
                for (data in it.categories) {
                    listCategory += ", ${data.name}"
                }
                binding.tvProductCategory.text = listCategory.drop(2)
                //product baseprice
                convertBasePrice = currency(it.basePrice)
                binding.tvProductharga.text = convertBasePrice
                basePrice = it.basePrice
                //image penjual
                Glide.with(this@DetailsFragment)
                    .load(it.user.imageUrl)
                    .placeholder(
                        AvatarGenerator
                        .AvatarBuilder(requireContext())
                        .setTextSize(50)
                        .setAvatarSize(200)
                        .toSquare()
                        .setLabel(it.user.fullName.toString())
                        .build())
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(50)))
                    .into(binding.rivDetailPhotoPenjual)
                //nama penjual & kota penjual
                binding.tvDetailNamaPenjual.text = it.user.fullName
                binding.tvDetailKotaPenjual.text = it.user.address
                //product description
                binding.tvDeskripsiProduct.text = it.description
            }
            val bundle = arguments
            val productID = bundle?.getInt(PRODUCT_ID)

            binding.btnSayaTertarik.setOnClickListener {
                val bottomFragment = TawarFragment(
                    productID!!,
                    productName,
                    basePrice,
                    imageURL,
                    refreshButton = {
                        viewModel.getBuyerOrder()
                    }
                )
                bottomFragment.show(parentFragmentManager, "Tag")
            }

            binding.cardBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }

        viewModel.showGetBuyerOrder.observe(viewLifecycleOwner){
            val bundle = arguments
            val productID = bundle?.getInt(PRODUCT_ID)

            for (data in 0 until (it.size ?: 0)) {
                if (it.get(data).productId == productID) {
                    isBid = true
                }
            }
            if (isBid) {
                binding.btnSayaTertarik.isEnabled = false
                binding.btnSayaTertarik.backgroundTintList =
                    requireContext().getColorStateList(R.color.dark_grey)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}