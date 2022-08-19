package com.example.projectgroup2.ui.main.daftarjual.infopenawar

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.projectgroup2.databinding.FragmentHubungiPenawarBinding
import com.example.projectgroup2.utils.currency
import com.example.projectgroup2.utils.striketroughtText
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.lang.StringBuilder

class HubungiPenawarFragment(
    private val namaPembeli: String,
    private val kotaPembeli: String,
    private val nomorPembeli: String,
    private val gambarPembeli: String,
    private val namaProduk: String,
    private val hargaProduk: Int,
    private val hargaDitawarProduk: Int,
    private val gambarProduk: String,
) : BottomSheetDialogFragment() {
    private var _binding: FragmentHubungiPenawarBinding? = null
    private val binding: FragmentHubungiPenawarBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHubungiPenawarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            tvNamaPembeli.text = namaPembeli
            tvKotaPembeli.text = kotaPembeli
            Glide.with(requireContext())
                .load(gambarPembeli)
                .centerCrop()
                .into(ivGambarPembeli)
            tvNamaProduct.text = namaProduk
            tvHargaProduct.apply {
                text = striketroughtText(this, currency(hargaProduk))
            }
            tvHargaDitawarProduct.text = "Ditawar ${currency(hargaDitawarProduk)}"
            Glide.with(requireContext())
                .load(gambarProduk)
                .into(ivGambarProduct)

            btnHubungiPembeli.setOnClickListener {
                val smilingFaceUnicode = 0x1F60A
                val waveUnicode = 0x1F44B
                val stringBuilder1 = StringBuilder()
                val stringBuilder2 = StringBuilder()
                val emoteSmile = stringBuilder1.append(Character.toChars(smilingFaceUnicode))
                val emoteWave = stringBuilder2.append(Character.toChars(waveUnicode))
                val phonenumberPenawar = "+62$nomorPembeli"
                val message = "Halo ${namaPembeli}${emoteWave} Tawaranmu pada product *$namaProduk* telah disetujui oleh penjual dengan harga *${currency(hargaDitawarProduk)}*. Penjual akan menghubungimu secepatnya$emoteSmile"

                startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse(
                        String.format("https://api.whatsapp.com/send?phone=%s&text=%s",
                            phonenumberPenawar,
                            message
                        ))
                    )
                )
            }
        }
    }

}