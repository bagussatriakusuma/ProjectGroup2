package com.example.projectgroup2.ui.main.daftarjual.editproduct

import android.app.*
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.projectgroup2.R
import com.example.projectgroup2.databinding.FragmentEditProductBinding
import com.example.projectgroup2.ui.main.daftarjual.DaftarJualFragment.Companion.PRODUCT_DESCRIPTION
import com.example.projectgroup2.ui.main.daftarjual.DaftarJualFragment.Companion.PRODUCT_ID
import com.example.projectgroup2.ui.main.daftarjual.DaftarJualFragment.Companion.PRODUCT_IMAGE
import com.example.projectgroup2.ui.main.daftarjual.DaftarJualFragment.Companion.PRODUCT_LOCATION
import com.example.projectgroup2.ui.main.daftarjual.DaftarJualFragment.Companion.PRODUCT_NAME
import com.example.projectgroup2.ui.main.daftarjual.DaftarJualFragment.Companion.PRODUCT_PRICE
import com.example.projectgroup2.utils.listCategory
import com.example.projectgroup2.utils.listCategoryId
import com.example.projectgroup2.utils.showToastSuccess
import com.example.projectgroup2.utils.uriToFile
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class EditProductFragment : Fragment() {
    private var _binding: FragmentEditProductBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EditProductViewModel by viewModels()
    private var uri: String = ""
    private var productId = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        bindView()

        viewModel.addCategory(listCategory)
        updateProduct()
        deleteProduct()

        binding.cardBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun bindViewModel() {
        viewModel.categoryList.observe(viewLifecycleOwner){ kat ->
            if (kat.isNotEmpty()){
                var kategori = ""
                for(element in kat){
                    kategori += ", $element"
                }
                binding.etEditKategoriProduct.setText(kategori.drop(2))
            }else{
                binding.etEditKategoriProduct.setText("Pilih Kategori")
            }
        }

        viewModel.showSuccess.observe(viewLifecycleOwner){
            showToastSuccess()
            findNavController().navigate(R.id.action_editProductFragment_to_daftarJualFragment)
            listCategoryId.clear()
        }

        viewModel.showDelete.observe(viewLifecycleOwner){
            showToastDelete()
            findNavController().navigate(R.id.action_editProductFragment_to_daftarJualFragment)
        }

        viewModel.showErrorDeleteProduct.observe(viewLifecycleOwner){
            showToastError()
        }
    }

    private fun bindView() {
        productId = arguments?.getInt(PRODUCT_ID)!!
        binding.etEditNamaProduct.setText(arguments?.getString(PRODUCT_NAME))
        binding.etEditHargaProduct.setText(arguments?.getInt(PRODUCT_PRICE).toString())
        binding.etEditDeskripsiProduct.setText(arguments?.getString(PRODUCT_DESCRIPTION))
        binding.etEditLokasiProduct.setText(arguments?.getString(PRODUCT_LOCATION))
        Glide.with(requireContext())
            .load(arguments?.getString(PRODUCT_IMAGE))
            .into(binding.ivEditPhotoProduct)

        binding.ivEditPhotoProduct.setOnClickListener {
            openImagePicker()
        }

        binding.etEditKategoriProduct.setOnClickListener {
            val bottomFragment = EditPilihCategoryFragment(
                update = {
                    viewModel.addCategory(listCategory)
                })
            bottomFragment.show(parentFragmentManager, "Tag")
        }
    }

    private fun updateProduct() {
        productId = arguments?.getInt(PRODUCT_ID)!!
        binding.btnEditProduct.setOnClickListener {
            resetError()
            val namaProduk = binding.etEditNamaProduct.text.toString()
            val hargaProduk = binding.etEditHargaProduct.text.toString()
            val deskripsiProduk = binding.etEditDeskripsiProduct.text.toString()
            val alamatProduk = binding.etEditLokasiProduct.text.toString()
            var file: File? = null
            val validation = validation(
                namaProduk,
                hargaProduk,
                deskripsiProduk,
                alamatProduk,
                listCategoryId
            )
            if (validation == "passed") {
                if (uri.isNotEmpty()) {
                    file = uriToFile(Uri.parse(uri), requireContext())
                }
                viewModel.uploadProduk(
                    productId,
                    namaProduk,
                    deskripsiProduk,
                    hargaProduk.toString(),
                    listCategoryId,
                    alamatProduk,
                    file
                )
            }
        }
    }

    private fun deleteProduct(){
        productId = arguments?.getInt(PRODUCT_ID)!!
        binding.btnHapusProduct.setOnClickListener {
            val dialogCustom = Dialog(requireContext())
            dialogCustom.setContentView(R.layout.alert_confirmation_delete_product)
            dialogCustom.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogCustom.setCancelable(false)
            dialogCustom.show()

            dialogCustom.findViewById<Button>(R.id.btnYa).setOnClickListener {
                viewModel.deleteSellerProduct(
                    id = productId
                )
                dialogCustom.dismiss()
            }
            dialogCustom.findViewById<TextView>(R.id.tv_cancel).setOnClickListener {
                dialogCustom.dismiss()
            }
        }

    }



    fun validation(
        namaProduk: String,
        hargaProduk: String,
        deskripsiProduk: String,
        alamatPenjual: String,
        listCategory: List<Int>
    ): String {
        when {
            namaProduk.isEmpty() -> {
                binding.etEditNamaProduct.error = "Nama Produk tidak boleh kosong"
                return "Nama Produk Kosong!"
            }
            hargaProduk.isEmpty() -> {
                binding.etEditHargaProduct.error = "Harga Produk tidak boleh kosong"
                return "Harga Produk Kosong!"
            }
            hargaProduk.toInt() > 2000000000 -> {
                binding.etEditHargaProduct.error = "Harga Produk tidak boleh lebih dari 2M"
                return "Harga Produk Melebihi Batas!"
            }
            deskripsiProduk.isEmpty() -> {
                binding.etEditDeskripsiProduct.error = "Nama Produk tidak boleh kosong"
                return "Deskripsi Produk Kosong!"
            }
            alamatPenjual.isEmpty() -> {
                binding.etEditLokasiProduct.error = "alamat Produk tidak boleh kosong"
                return "Deskripsi Produk Kosong!"
            }
            listCategory.isEmpty() -> {
                binding.etEditKategoriProduct.error = "Kategori produk tidak boleh kosong"
                Toast.makeText(requireContext(), "Kategori Produk Kosong", Toast.LENGTH_SHORT).show()
                return "Kategori Produk Kosong!"
            }
            else -> {
                return "passed"
            }
        }
    }

    fun resetError() {
        binding.etEditNamaProduct.error = null
        binding.etEditHargaProduct.error = null
        binding.etEditKategoriProduct.error = null
        binding.etEditDeskripsiProduct.error = null
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            when (resultCode) {
                Activity.RESULT_OK -> {
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

    private fun loadImage(uri: Uri) {
        binding.apply {
            Glide.with(binding.root)
                .load(uri)
                .transform(CenterCrop(), RoundedCorners(12))
                .into(ivEditPhotoProduct)

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
            )
            .compress(1024)
            .maxResultSize(
                1080,
                1080
            )
            .createIntent { intent ->
                startForProfileImageResult.launch(intent)
            }
    }

    private fun showToastSuccess() {
        val snackBarView =
            Snackbar.make(binding.root, "Product berhasil di update.", Snackbar.LENGTH_LONG)
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

    private fun showToastError() {
        val snackBarView =
            Snackbar.make(binding.root, "Product telah di order.", Snackbar.LENGTH_LONG)
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
        snackBarView.view.setBackgroundResource(R.drawable.bg_snackbar_error)
        snackBarView.view.layoutParams = layoutParams
        snackBarView.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
        snackBarView.show()
    }

    private fun showToastDelete() {
        val snackBarView =
            Snackbar.make(binding.root, "Product berhasil di hapus.", Snackbar.LENGTH_LONG)
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}