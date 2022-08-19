package com.example.projectgroup2.ui.main.home.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.projectgroup2.R
import com.example.projectgroup2.data.api.main.buyerorder.BuyerOrderRequest
import com.example.projectgroup2.databinding.FragmentTawarBinding
import com.example.projectgroup2.utils.currency
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.lang.StringBuilder

@AndroidEntryPoint
class TawarFragment(
    productId: Int,
    namaProduct: String,
    hargaProduct: Int,
    gambarProduct: String,
    private val refreshButton: () -> Unit
) : BottomSheetDialogFragment() {
    private var _binding: FragmentTawarBinding? = null
    private val binding get() = _binding!!
    private val namaProduct = namaProduct
    private val hargaProduct = hargaProduct
    private val gambarProduct = gambarProduct
    private val productId = productId
    private val viewModel: DetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTawarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        bindView()

        val smilingFaceUnicode = 0x1F60A
        val stringBuilder1 = StringBuilder()
        val emoteSmile = stringBuilder1.append(Character.toChars(smilingFaceUnicode))

        binding.textView10.text = "Masukan harga tawar anda$emoteSmile"

        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    private fun bindViewModel(){
        viewModel.showBuyerOrder.observe(viewLifecycleOwner){
            Toast.makeText(context, "Harga Tawarmu berhasil dikirim ke Penjual", Toast.LENGTH_SHORT).show()
            refreshButton.invoke()
        }
        viewModel.showError.observe(viewLifecycleOwner){
            Toast.makeText(context, "Produk sudah terlalu banyak yang menawar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun bindView(){
        binding.tvProductName.text = namaProduct
        binding.tvProductPrice.text = currency(hargaProduct)
        Glide.with(binding.root)
            .load(gambarProduct)
            .into(binding.ivProductImage)

        binding.btnTawarProduct.setOnClickListener {
            validation()
        }
    }

    private fun validation() {
        if (binding.etHargaTawarProduct.text.isNullOrEmpty()) {
            binding.containerHargaTawar.error = "Input tawar harga tidak boleh kosong"
        }else if(binding.etHargaTawarProduct.text.toString().toInt() >= hargaProduct){
            binding.containerHargaTawar.error = "Tawaranmu lebih tinggi dari harga produk"
        }else {
            viewModel.getToken()
            viewModel.showToken.observe(viewLifecycleOwner) {
                val inputHargaTawar = binding.etHargaTawarProduct.text
                val requestHargaTawar =
                    BuyerOrderRequest(productId, inputHargaTawar.toString().toInt())
                viewModel.buyerOrder(requestHargaTawar)
            }
        }
    }
}