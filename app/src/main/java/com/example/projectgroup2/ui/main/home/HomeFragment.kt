package com.example.projectgroup2.ui.main.home

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentValues.TAG
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.avatarfirst.avatargenlib.AvatarConstants
import com.avatarfirst.avatargenlib.AvatarGenerator
import com.bumptech.glide.Glide
import com.example.projectgroup2.R
import com.example.projectgroup2.data.api.main.buyer.product.GetProductResponse
import com.example.projectgroup2.data.api.main.buyer.product.ProductResponseItem
import com.example.projectgroup2.data.api.main.category.CategoryResponse
import com.example.projectgroup2.databinding.FragmentHomeBinding
import com.example.projectgroup2.ui.main.home.adapter.CategoryAdapter
import com.example.projectgroup2.ui.main.home.adapter.ProductAdapter
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    companion object {
        var result = 0
        const val PRODUCT_ID = "id"
    }
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var productAdapter: ProductAdapter
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        bindAdapterAndItem()
        changeToolbar()

        val status = "available"
        val categoryId = ""
        val search = ""
        viewModel.getProductBuyer(status = status, categoryId = categoryId, search = search)
        viewModel.getCategory()
        viewModel.getUserData()
    }

    private fun bindViewModel() {
        viewModel.showShimmerProduct.observe(viewLifecycleOwner){
            if(it){
                binding.shimerProduct.visibility = View.VISIBLE
            }else{
                binding.shimerProduct.visibility = View.GONE
            }
        }

        viewModel.showShimmerCategory.observe(viewLifecycleOwner){
            if(it){
                binding.shimerImageHome.visibility = View.VISIBLE
            }else{
                binding.shimerImageHome.visibility = View.GONE
            }
        }

        viewModel.showShimmerUser.observe(viewLifecycleOwner){
            if(it){
                binding.shimerCategory.visibility = View.VISIBLE
            }else{
                binding.shimerCategory.visibility = View.GONE
            }
        }

        viewModel.showEmpty.observe(viewLifecycleOwner){
            if(it){
                binding.lottieEmpty.visibility = View.VISIBLE
                binding.tvEmpty.visibility = View.VISIBLE
            }else{
                binding.lottieEmpty.visibility = View.GONE
                binding.tvEmpty.visibility = View.GONE
            }
        }

        viewModel.showRv.observe(viewLifecycleOwner){
            if(it){
                binding.rvProductHome.visibility = View.VISIBLE
            }else{
                binding.rvProductHome.visibility = View.GONE
            }
        }

        viewModel.showProductBuyer.observe(viewLifecycleOwner){
            productAdapter.submitData(it)
        }

        viewModel.showCategory.observe(viewLifecycleOwner){
            categoryAdapter.submitData(it)
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
                .into(binding.rivUserImage)
        }
    }

    private fun bindAdapterAndItem(){
        productAdapter = ProductAdapter(object: ProductAdapter.OnClickListener{
            override fun onClickItem(data: GetProductResponse) {
                val bundle = Bundle()
                bundle.putInt(PRODUCT_ID, data.id)
                findNavController().navigate(R.id.action_homeFragment_to_detailsFragment, bundle)
            }
        })
        binding.rvProductHome.layoutManager =
            GridLayoutManager(requireContext(), 2)
        binding.rvProductHome.isNestedScrollingEnabled = false
        binding.rvProductHome.adapter = productAdapter

        categoryAdapter = CategoryAdapter(object: CategoryAdapter.OnClickListener{
            override fun onClickItem(data: CategoryResponse) {
                val status = "available"
                val search = ""
                viewModel.getProductBuyer(categoryId = data.id.toString(), status = status, search = search)
            }
        })
        binding.rvCategoryHome.adapter = categoryAdapter

        binding.etSearchProduct.setOnEditorActionListener { textView, i, keyEvent ->
            if(i == EditorInfo.IME_ACTION_SEARCH){
                val onSearch = binding.etSearchProduct.text.toString()
                viewModel.getProductBuyer(search = onSearch, status = "available", categoryId = "")
                true
            }else{
                false
            }
        }

        binding.rivUserImage.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_editProfileFragment)
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun changeToolbar() {
        var toolbarColored = false
        var toolbarTransparent = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.scrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
                val bannerHeight =
                    (binding.ivBanner.height / 2) - result - binding.statusBar.height
                val colored = ContextCompat.getColor(requireContext(), R.color.white)
                val transparent =
                    ContextCompat.getColor(requireContext(), android.R.color.transparent)

                when {
                    scrollY > bannerHeight -> {
                        if (toolbarTransparent) {
                            val colorAnimate =
                                ValueAnimator.ofObject(ArgbEvaluator(), transparent, colored)
                            colorAnimate.addUpdateListener {
                                binding.statusBar.setBackgroundColor(it.animatedValue as Int)
                                binding.toolbar.setBackgroundColor(it.animatedValue as Int)
                            }
                            colorAnimate.duration = 250
                            colorAnimate.start()
                            toolbarColored = true
                            toolbarTransparent = false
                        }
                    }
                    else -> {
                        if (toolbarColored) {
                            val colorAnimate =
                                ValueAnimator.ofObject(ArgbEvaluator(), colored, transparent)
                            colorAnimate.addUpdateListener {
                                binding.statusBar.setBackgroundColor(it.animatedValue as Int)
                                binding.toolbar.setBackgroundColor(it.animatedValue as Int)
                            }
                            colorAnimate.duration = 250
                            colorAnimate.start()
                            toolbarColored = false
                            toolbarTransparent = true
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}