@file:Suppress("SENSELESS_COMPARISON")

package com.example.projectgroup2.ui.main.notif.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projectgroup2.data.api.main.notification.NotifResponse
import com.example.projectgroup2.databinding.ListItemNotificationBinding
import com.example.projectgroup2.utils.convertDate
import com.example.projectgroup2.utils.currency
import java.lang.StringBuilder

class NotifAdapter(
    private val onItemClick : OnClickListener
) :
    RecyclerView.Adapter<NotifAdapter.ViewHolder>() {
    private val diffCallBack = object : DiffUtil.ItemCallback<NotifResponse>(){
        override fun areItemsTheSame(
            oldItem: NotifResponse,
            newItem: NotifResponse
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: NotifResponse,
            newItem: NotifResponse
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }
    private val differ = AsyncListDiffer(this,diffCallBack)
    fun submitData(value:List<NotifResponse>?) = differ.submitList(value)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ListItemNotificationBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = differ.currentList[position]
        data.let {
            holder.bind(data)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class ViewHolder(private val binding: ListItemNotificationBinding):
        RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(data: NotifResponse){
            val Unicode1 = 0x1F973
            val Unicode2 = 0x1F61E
            val Unicode3 = 0x1F603
            val stringBuilder1 = StringBuilder()
            val stringBuilder2 = StringBuilder()
            val stringBuilder3 = StringBuilder()
            val emoteParty = stringBuilder1.append(Character.toChars(Unicode1))
            val emoteSad = stringBuilder2.append(Character.toChars(Unicode2))
            val emoteHappy = stringBuilder3.append(Character.toChars(Unicode3))
            binding.apply {
                when (data.status) {
                    "create" -> {
                        tvNotifInfo.text = "Info"
                        if (data.product != null) {
                            if (data.receiverId == data.product.userId) {
                                tvNotifHeader.text = "Produk anda berhasil dibuat"
                                tvNotifBody.text =
                                    "Produk anda ${data.productName} berhasil dibuat dengan harga "+ currency(data.basePrice.toInt())
                            } else {
                                tvNotifHeader.text = "Produk anda berhasil dibuat"
                                tvNotifBody.text =
                                    "Produk anda ${data.productName} berhasil dibuat dengan harga "+ currency(data.basePrice.toInt())
                            }
                        } else {
                            tvNotifHeader.text = "Produk Sudah di hapus"
                            tvNotifBody.text = "Yahh produk ini sudah di hapus oleh penjual."
                        }
                    }
                    "bid" -> {
                        tvNotifInfo.text = "Tawar"
                        if (data.product != null) {
                            if (data.receiverId == data.product.userId) {
                                tvNotifHeader.text = "Ada yang tawar produk anda!${emoteHappy}"
                                tvNotifBody.text =
                                    "Hey, ada yang menawar produk anda ${data.productName} dengan harga "+ currency(data.basePrice.toInt())+" ditawar sebesar "+ currency(data.bidPrice.toInt())
                            } else {
                                tvNotifHeader.text = "Tawaran anda belum diterima oleh penjual"
                                tvNotifBody.text =
                                    "Tawaran anda pada produk ${data.productName} dengan harga"+ currency(data.basePrice.toInt())+" ditawar sebesar "+ currency(data.bidPrice.toInt())+" belum diterima oleh penjual nih, sabar ya!"
                            }
                        } else {
                            tvNotifHeader.text = "Produk Sudah di hapus"
                            tvNotifBody.text = "Yahh produk ini sudah di hapus oleh penjual."
                        }
                    }
                    "declined" -> {
                        tvNotifInfo.text = "Ditolak"
                        if (data.product != null) {
                            if (data.receiverId == data.product.userId) {
                                tvNotifHeader.text = "Anda menolak tawaran ini"
                                tvNotifBody.text =
                                    "Anda menolak tawaran penawar pada produk ${data.productName} dengan harga "+ currency(data.basePrice.toInt())+" ditawar "+ currency(data.bidPrice.toInt())
                            } else {
                                tvNotifHeader.text = "Tawaran anda ditolak oleh penjual${emoteSad}"
                                tvNotifBody.text =
                                    "Wah tampaknya tawaran anda pada produk ${data.productName} dengan harga "+ currency(data.basePrice.toInt())+" ditawar sebesar "+ currency(data.bidPrice.toInt())+" ditolak oleh penjual."
                            }
                        } else {
                            tvNotifHeader.text = "Produk Sudah di hapus"
                            tvNotifBody.text = "Yahh produk ini sudah di hapus oleh penjual."
                        }
                    }
                    "accepted" -> {
                        tvNotifInfo.text = "Diterima"
                        if (data.product != null) {
                            if (data.receiverId == data.product.userId) {
                                tvNotifHeader.text = "Anda menerima tawaran ini"
                                tvNotifBody.text =
                                    "Anda menerima tawaran penawar pada produk ${data.productName} dengan harga "+ currency(data.basePrice.toInt())+" ditawar sebesar "+ currency(data.bidPrice.toInt())
                            } else {
                                tvNotifHeader.text = "Tawaran anda diterima oleh penjual${emoteParty}"
                                tvNotifBody.text =
                                    "Selamat! tawaran anda pada produk ${data.productName} dengan harga "+ currency(data.basePrice.toInt())+" ditawar sebesar"+ currency(data.bidPrice.toInt())+" diterima oleh penjual."
                            }
                        } else {
                            tvNotifHeader.text = "Produk Sudah di hapus"
                            tvNotifBody.text = "Yahh produk ini sudah di hapus oleh penjual."
                        }
                    }
                    else -> {
                        tvNotifInfo.text = " "
                    }
                }
//                tvNotifBody.text =
//                    when (data.status) {
//                        "declined" -> "Ditolak " + currency(data.bidPrice)
//                        "accepted" -> "Diterima " + currency(data.bidPrice)
//                        else -> "Ditawar " + currency(data.bidPrice)
//                    }
//                if(data.status == "declined"){
//                    tvAktivitasInfo.text = "Ditolak " + currency(data.bidPrice)
//                }else if(data.status == "accepted"){
//                    tvAktivitasInfo.text = "Diterima " + currency(data.bidPrice)
//                }else if(data.status == "create"){
//                    tvAktivitasInfo.visibility = View.GONE
//                }else if(data.status == "bid"){
//                    tvAktivitasInfo.text = "Ditawar " + currency(data.bidPrice)
//                }
//                tvProdukInfo.text = data.productName
//                tvHargaInfo.apply {
//                    text = if(data.basePrice.isNotEmpty()) striketroughtText(this, currency(data.basePrice.toInt())) else "-"
//                }
                tvNotifTanggal.text = convertDate(data.updatedAt)
                if (!data.read){
                    Glide.with(binding.root)
                        .load(data.imageUrl)
                        .centerCrop()
                        .into(ivGambarProduct)
                }
                root.setOnClickListener {
                    onItemClick.onClickItem(data)
                }
            }
        }
    }

    interface OnClickListener{
        fun onClickItem(data: NotifResponse)
    }
}