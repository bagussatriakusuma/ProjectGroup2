package com.example.projectgroup2.ui.main.jual

import android.app.ActionBar
import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.projectgroup2.R
import com.example.projectgroup2.databinding.FragmentHomeBinding
import com.example.projectgroup2.databinding.FragmentJualBinding
import com.example.projectgroup2.ui.main.jual.adapter.CategoryAdapter
import com.example.projectgroup2.utils.listCategory
import com.example.projectgroup2.utils.listCategoryId
import com.example.projectgroup2.utils.uriToFile
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class JualFragment : Fragment() {
    private var _binding: FragmentJualBinding? = null
    private val binding get() = _binding!!
    private val viewModel: JualViewModel by viewModels()

    private var uri: String = ""
    companion object {
        const val NAMA_PRODUCT_KEY = "namaProduk"
        const val HARGA_PRODUCT_KEY = "hargaProduk"
        const val DESKRIPSI_PRODUCT_KEY = "deskripsiProduk"
        const val KATEGORI_PRODUCT_KEY = "kategoriProduk"
        const val ALAMAT_PRODUCT_KEY = "alamatProduk"
        const val IMAGE_PRODUCT_KEY = "imageProduk"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentJualBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserData()
        bindViewModel()
        bindView()

    }

    private fun bindViewModel(){
        viewModel.categoryList.observe(viewLifecycleOwner){ kat ->
            if (kat.isNotEmpty()){
                var kategori = ""
                for(element in kat){
                    kategori += ", $element"
                }
                binding.etKategoriProduct.setText(kategori.drop(2))
            }
        }

        viewModel.showSuccess.observe(viewLifecycleOwner){
            showToastSuccess()
            findNavController().navigate(R.id.action_jualFragment_to_homeFragment)
            listCategoryId.clear()
        }

        val dialogCustom = Dialog(requireContext())
        dialogCustom.setContentView(R.layout.alert_loading)
        dialogCustom.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogCustom.setCancelable(false)
        viewModel.showLoading.observe(viewLifecycleOwner) {
            if (it) {
                dialogCustom.show()
            } else {
                dialogCustom.dismiss()
            }
        }
    }

    private fun bindView(){
        binding.cardBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.etKategoriProduct.setOnClickListener {
            val bottomFragment = PilihCategoryFragment(
                update = {
                    viewModel.addCategory(listCategory)
                })
            bottomFragment.show(parentFragmentManager, "Tag")
        }

        binding.ivPhotoProduct.setOnClickListener {
            openImagePicker()
        }

        val bundle = Bundle()
        binding.btnPreviewProduct.setOnClickListener {
            resetError()
            val namaProduk = binding.etNamaProduct.text.toString()
            val hargaProduk = binding.etHargaProduct.text.toString()
            val deskripsiProduk = binding.etDeskripsiProduct.text.toString()
            val kategoriProduk = binding.etKategoriProduct.text.toString()
            val alamatPenjual = binding.etLokasiProduct.text.toString()
            val validation = validation(
                listCategoryId,
                namaProduk,
                hargaProduk,
                deskripsiProduk,
                alamatPenjual,
                uri
            )
            if (validation == "passed") {
                bundle.putString(NAMA_PRODUCT_KEY, namaProduk)
                bundle.putString(HARGA_PRODUCT_KEY, hargaProduk)
                bundle.putString(DESKRIPSI_PRODUCT_KEY, deskripsiProduk)
                bundle.putString(KATEGORI_PRODUCT_KEY, kategoriProduk)
                bundle.putString(ALAMAT_PRODUCT_KEY, alamatPenjual)
                bundle.putString(IMAGE_PRODUCT_KEY, uri)
                findNavController().navigate(R.id.action_jualFragment_to_previewFragment, bundle)
            }
        }

        binding.btnPostProduct.setOnClickListener {
            resetError()
            val namaProduk = binding.etNamaProduct.text.toString()
            val hargaProduk = binding.etHargaProduct.text.toString()
            val deskripsiProduk = binding.etDeskripsiProduct.text.toString()
            val alamatPenjual = binding.etLokasiProduct.text.toString()
            val validation = validation(
                listCategoryId,
                namaProduk,
                hargaProduk,
                deskripsiProduk,
                alamatPenjual,
                uri
            )
            if (validation == "passed") {
                viewModel.uploadProduct(
                    namaProduk,
                    deskripsiProduk,
                    hargaProduk,
                    listCategoryId,
                    alamatPenjual,
                    uriToFile(Uri.parse(uri), requireContext())
                )
            }
        }
    }

    private fun loadImage(uri: Uri) {
        binding.apply {
            Glide.with(binding.root)
                .load(uri)
                .transform(CenterCrop(), RoundedCorners(12))
                .into(ivPhotoProduct)
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

    fun validation(listCategory: List<Int>, namaProduk: String, hargaProduk: String, deskripsiProduk: String, alamatPenjual: String, uriFoto: String): String {
        when {
            listCategory.isEmpty() -> {
                binding.tilKategori.error = "Kategori produk tidak boleh kosong"
                return "Kategori Produk Kosong!"
            }
            namaProduk.isEmpty() -> {
                binding.tilNamaProduct.error = "Nama Produk tidak boleh kosong"
                return "Nama Produk Kosong!"
            }
            hargaProduk.isEmpty() -> {
                binding.tilHargaProduct.error = "Harga Produk tidak boleh kosong"
                return "Harga Produk Kosong!"
            }
            hargaProduk.toInt() > 2000000000 -> {
                binding.tilHargaProduct.error = "Harga Produk tidak boleh lebih dari 2M"
                return "Harga Produk Melebihi Batas!"
            }
            deskripsiProduk.isEmpty() -> {
                binding.tilDeskripsiProduct.error = "Nama Produk tidak boleh kosong"
                return "Deskripsi Produk Kosong!"
            }
            alamatPenjual.isEmpty() -> {
                binding.tilLokasiProduct.error = "alamat Produk tidak boleh kosong"
                return "Deskripsi Produk Kosong!"
            }
            uriFoto.isEmpty() -> {
                Toast.makeText(requireContext(), "Foto Produk Kosong", Toast.LENGTH_SHORT).show()
                return "Foto Produk Kosong!"
            }
            else -> {
                return "passed"
            }
        }
    }

    private fun showToastSuccess() {
        val snackBarView =
            Snackbar.make(binding.root, "Product berhasil di terbitkan.", Snackbar.LENGTH_LONG)
        val layoutParams = ActionBar.LayoutParams(snackBarView.view.layoutParams)
        snackBarView.setAction(" ") {
            snackBarView.dismiss()
        }
        val textView =
            snackBarView.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_action)
        textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_close_24, 0)
        textView.compoundDrawablePadding = 16
        layoutParams.gravity = Gravity.TOP
        layoutParams.setMargins(30, 150, 30, 0)
        snackBarView.view.setPadding(20, 10, 0, 10)
        snackBarView.view.setBackgroundResource(R.drawable.bg_snackbar)
        snackBarView.view.layoutParams = layoutParams
        snackBarView.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
        snackBarView.show()
    }

    fun resetError() {
        binding.tilKategori.error = null
        binding.tilNamaProduct.error = null
        binding.tilHargaProduct.error = null
        binding.tilDeskripsiProduct.error = null
        binding.tilLokasiProduct.error = null
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}