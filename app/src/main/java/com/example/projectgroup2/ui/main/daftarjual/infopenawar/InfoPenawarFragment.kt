package com.example.projectgroup2.ui.main.daftarjual.infopenawar

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.avatarfirst.avatargenlib.AvatarGenerator
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.projectgroup2.R
import com.example.projectgroup2.data.api.main.approveorder.ApproveOrderRequest
import com.example.projectgroup2.databinding.FragmentInfoPenawarBinding
import com.example.projectgroup2.ui.main.daftarjual.DaftarJualFragment.Companion.ORDER_ID
import com.example.projectgroup2.ui.main.daftarjual.DaftarJualFragment.Companion.ORDER_STATUS
import com.example.projectgroup2.ui.main.daftarjual.DaftarJualFragment.Companion.PRODUCT_BID
import com.example.projectgroup2.ui.main.daftarjual.DaftarJualFragment.Companion.PRODUCT_BID_DATE
import com.example.projectgroup2.ui.main.daftarjual.DaftarJualFragment.Companion.PRODUCT_IMAGE
import com.example.projectgroup2.ui.main.daftarjual.DaftarJualFragment.Companion.PRODUCT_NAME
import com.example.projectgroup2.ui.main.daftarjual.DaftarJualFragment.Companion.PRODUCT_PRICE
import com.example.projectgroup2.ui.main.daftarjual.DaftarJualFragment.Companion.USER_CITY
import com.example.projectgroup2.ui.main.daftarjual.DaftarJualFragment.Companion.USER_IMAGE
import com.example.projectgroup2.ui.main.daftarjual.DaftarJualFragment.Companion.USER_NAME
import com.example.projectgroup2.ui.main.daftarjual.DaftarJualFragment.Companion.USER_PHONE_NUMBER
import com.example.projectgroup2.utils.convertDate
import com.example.projectgroup2.utils.currency
import com.example.projectgroup2.utils.showToastSuccess
import com.example.projectgroup2.utils.striketroughtText
import dagger.hilt.android.AndroidEntryPoint
import java.lang.StringBuilder

@AndroidEntryPoint
class InfoPenawarFragment : Fragment() {
    private var _binding : FragmentInfoPenawarBinding? = null
    private val binding get() = _binding!!
    private val viewModel: InfoPenawarViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInfoPenawarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        bindView()
    }

    private fun bindViewModel() {
        val bundlePenawar = arguments
        val idOrder = bundlePenawar?.getInt(ORDER_ID)
        val statusOrder = bundlePenawar?.getString(ORDER_STATUS)
        val namaPenawar = bundlePenawar?.getString(USER_NAME)
        val kotaPenawar = bundlePenawar?.getString(USER_CITY)
        val nomorPenawar = bundlePenawar?.getString(USER_PHONE_NUMBER)
        val gambarPenawar = bundlePenawar?.getString(USER_IMAGE)
        val namaProduk = bundlePenawar?.getString(PRODUCT_NAME)
        val hargaAwalProduk = bundlePenawar?.getString(PRODUCT_PRICE)
        val hargaDitawarProduk = bundlePenawar?.getString(PRODUCT_BID)
        val gambarProduk = bundlePenawar?.getString(PRODUCT_IMAGE)
        var status: String

        viewModel.showApproveOrder.observe(viewLifecycleOwner){
            if(it.status == "accepted"){
                binding.groupBtnTolakTerima.visibility = View.GONE
                binding.groupBtnStatusHubungi.visibility = View.VISIBLE
                val bottomFragment = HubungiPenawarFragment(
                    namaPenawar.toString(),
                    kotaPenawar.toString(),
                    nomorPenawar.toString(),
                    gambarPenawar.toString(),
                    namaProduk.toString(),
                    hargaAwalProduk.toString().toInt(),
                    hargaDitawarProduk.toString().toInt(),
                    gambarProduk.toString()
                )
                bottomFragment.show(parentFragmentManager, "Tag")
            }else {
                showToastSuccess(binding.root, "Tawaran $namaPenawar di Tolak!", resources.getColor(R.color.success))
                findNavController().navigate(R.id.action_infoPenawarFragment_to_daftarJualFragment)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bindView() {
        val bundlePenawar = arguments
        val idOrder = bundlePenawar?.getInt(ORDER_ID)
        val statusOrder = bundlePenawar?.getString(ORDER_STATUS)
        val namaPenawar = bundlePenawar?.getString(USER_NAME)
        val kotaPenawar = bundlePenawar?.getString(USER_CITY)
        val nomorPenawar = bundlePenawar?.getString(USER_PHONE_NUMBER)
        val gambarPenawar = bundlePenawar?.getString(USER_IMAGE)
        val namaProduk = bundlePenawar?.getString(PRODUCT_NAME)
        val hargaAwalProduk = bundlePenawar?.getString(PRODUCT_PRICE)?.toInt()
        val hargaDitawarProduk = bundlePenawar?.getString(PRODUCT_BID)?.toInt()
        val gambarProduk = bundlePenawar?.getString(PRODUCT_IMAGE)
        var status: String

        binding.cardBack.setOnClickListener {
            findNavController().popBackStack()
        }

        val Unicode3 = 0x1F603
        val stringBuilder3 = StringBuilder()
        val emoteHappy = stringBuilder3.append(Character.toChars(Unicode3))

        binding.textView5.text = "Hey, ada yang menawar produk anda$emoteHappy Silahkan terima tawaran jika anda setuju dan hubungi penawar melalui whatsapp!"

        binding.tvNamaPenawar.text = namaPenawar
        binding.tvKotaPenawar.text = kotaPenawar
        Glide.with(requireContext())
            .load(gambarPenawar)
            .placeholder(
                AvatarGenerator
                    .AvatarBuilder(requireContext())
                    .setTextSize(50)
                    .setAvatarSize(200)
                    .toSquare()
                    .setLabel(namaPenawar.toString())
                    .build())
            .apply(RequestOptions.bitmapTransform(RoundedCorners(50)))
            .into(binding.ivGambarPenawar)

        binding.tvProductNameTawar.text = namaProduk
        binding.tvProductPriceTawar.text = striketroughtText(binding.tvProductPriceTawar, currency(hargaAwalProduk!!))
        binding.tvProductPriceDitawar.text = "Ditawar "+currency(hargaDitawarProduk!!)
        binding.tvProductDateTawar.text = convertDate(bundlePenawar?.getString(PRODUCT_BID_DATE).toString())
        Glide.with(requireContext())
            .load(gambarProduk)
            .into(binding.ivProductImageTawar)

        if(statusOrder == "accepted"){
            binding.groupBtnTolakTerima.visibility = View.GONE
            binding.groupBtnStatusHubungi.visibility = View.VISIBLE
        }

        binding.btnTolakTawar.setOnClickListener {
            val dialogCustom = Dialog(requireContext())
            dialogCustom.setContentView(R.layout.alert_confirmation_decline)
            dialogCustom.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogCustom.setCancelable(false)
            dialogCustom.show()

            dialogCustom.findViewById<Button>(R.id.btnYa).setOnClickListener{
                status = "declined"
                val body = ApproveOrderRequest(
                    status
                )
                if (idOrder != null) {
                    viewModel.declineOrder(idOrder, body)
                    findNavController().navigate(R.id.action_infoPenawarFragment_to_daftarJualFragment)
                    dialogCustom.dismiss()
                }
            }
            dialogCustom.findViewById<TextView>(R.id.tv_cancel).setOnClickListener {
                dialogCustom.dismiss()
            }

//            AlertDialog.Builder(requireContext())
//                .setTitle("Pesan")
//                .setMessage("Tolak Tawaran?")
//                .setPositiveButton("Iya"){ positive, _ ->
//                    status = "declined"
//                    val body = ApproveOrderRequest(
//                        status
//                    )
//                    if (idOrder != null) {
//                        viewModel.declineOrder(idOrder, body)
//                        findNavController().navigate(R.id.action_infoPenawarFragment_to_daftarJualFragment)
//                        positive.dismiss()
//                    }
//                }
//                .setNegativeButton("Tidak"){ negative, _ ->
//                    negative.dismiss()
//                }
//                .show()
        }

        binding.btnTerimaTawar.setOnClickListener {
            val dialogCustom = Dialog(requireContext())
            dialogCustom.setContentView(R.layout.alert_confirmation_accept)
            dialogCustom.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogCustom.setCancelable(false)
            dialogCustom.show()

            dialogCustom.findViewById<Button>(R.id.btnYa).setOnClickListener{
                status = "accepted"
                val body = ApproveOrderRequest(
                    status
                )
                if (idOrder != null) {
                    viewModel.declineOrder(idOrder, body)
                    findNavController().navigate(R.id.action_infoPenawarFragment_to_daftarJualFragment)
                    dialogCustom.dismiss()
                }
            }
            dialogCustom.findViewById<TextView>(R.id.tv_cancel).setOnClickListener {
                dialogCustom.dismiss()
            }

//            AlertDialog.Builder(requireContext())
//                .setTitle("Pesan")
//                .setMessage("Terima Tawaran?")
//                .setPositiveButton("Iya"){ positive, _ ->
//                    status = "accepted"
//                    val body = ApproveOrderRequest(
//                        status
//                    )
//                    if (idOrder != null) {
//                        viewModel.declineOrder(idOrder, body)
//                        findNavController().navigate(R.id.action_infoPenawarFragment_to_daftarJualFragment)
//                        positive.dismiss()
//                    }
//                }
//                .setNegativeButton("Tidak"){ negative, _ ->
//                    negative.dismiss()
//                }
//                .show()
        }

        binding.btnHubungiTawar.setOnClickListener {
            val dialogCustom = Dialog(requireContext())
            dialogCustom.setContentView(R.layout.alert_confirmation_hubungi)
            dialogCustom.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogCustom.setCancelable(false)
            dialogCustom.show()

            dialogCustom.findViewById<Button>(R.id.btnYa).setOnClickListener{
                val smilingFaceUnicode = 0x1F60A
                val waveUnicode = 0x1F44B
                val stringBuilder1 = StringBuilder()
                val stringBuilder2 = StringBuilder()
                val emoteSmile = stringBuilder1.append(Character.toChars(smilingFaceUnicode))
                val emoteWave = stringBuilder2.append(Character.toChars(waveUnicode))
                val phonenumberPenawar = "+62$nomorPenawar"
                val message = "Halo ${namaPenawar}${emoteWave} Tawaranmu pada product *$namaProduk* telah disetujui oleh penjual dengan harga *${currency(hargaDitawarProduk)}*. Penjual akan menghubungimu secepatnya$emoteSmile"

                startActivity(
                    Intent(
                        Intent.ACTION_VIEW, Uri.parse(
                        String.format("https://api.whatsapp.com/send?phone=%s&text=%s",
                            phonenumberPenawar,
                            message
                        ))
                    )
                )
                dialogCustom.dismiss()
            }
            dialogCustom.findViewById<TextView>(R.id.tv_cancel).setOnClickListener {
                dialogCustom.dismiss()
            }

//            val bottomFragment = HubungiPenawarFragment(
//                namaPenawar.toString(),
//                kotaPenawar.toString(),
//                nomorPenawar.toString(),
//                gambarPenawar.toString(),
//                namaProduk.toString(),
//                hargaAwalProduk.toString().toInt(),
//                hargaDitawarProduk.toString().toInt(),
//                gambarProduk.toString()
//            )
//            bottomFragment.show(parentFragmentManager, "Tag")
        }

        binding.btnStatusTawar.setOnClickListener {
            Toast.makeText(requireContext(), "on-progress", Toast.LENGTH_SHORT).show()
        }

    }
}