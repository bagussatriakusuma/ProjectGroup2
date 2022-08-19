package com.example.projectgroup2.ui.main.daftarjual.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.projectgroup2.data.api.main.sellerorder.SellerOrderResponse
import com.example.projectgroup2.databinding.ListItemDiminatiBinding
import com.example.projectgroup2.utils.convertDate
import com.example.projectgroup2.utils.currency
import com.example.projectgroup2.utils.striketroughtText

class SellerOrderAdapter(private val OnItemClick: OnClickListener) :
    RecyclerView.Adapter<SellerOrderAdapter.ViewHolder>() {
    private val diffCallback = object : DiffUtil.ItemCallback<SellerOrderResponse>() {
        override fun areItemsTheSame(
            oldItem: SellerOrderResponse,
            newItem: SellerOrderResponse
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: SellerOrderResponse,
            newItem: SellerOrderResponse
        ): Boolean {
            return oldItem.id == oldItem.id
        }

    }
    private val differ = AsyncListDiffer(this, diffCallback)
    fun submitData(value: List<SellerOrderResponse>?) = differ.submitList(value)

    inner class ViewHolder(private val binding: ListItemDiminatiBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(data: SellerOrderResponse) {
            val basePrice = currency(data.product.basePrice)
            val priceNego = currency(data.price)
            val date = convertDate(data.createdAt)
            binding.apply {
                Glide.with(binding.root)
                    .load(data.product.imageUrl)
                    .transform(CenterCrop(), RoundedCorners(12))
                    .into(binding.ivTerjual)
                tvNamaProduct.text = data.product.name
                tvHargaProduct.text = basePrice
                tvDitawarProduct.text = "Ditawar $priceNego"
                tvTanggalTawar.text = date
                if (data.status != "declined") {
                    root.setOnClickListener {
                        OnItemClick.onClickItem(data)
                    }
                }
                if (data.status == "declined") {
                    root.alpha = 0.5f
                    tvPenawaranProduct.text = "Ditolak"
                    tvHargaProduct.apply {
                        text = striketroughtText(this, basePrice)
                    }
                    tvDitawarProduct.apply {
                        text = "Ditolak $priceNego"
                    }
                }
                if (data.status == "accepted") {
                    tvPenawaranProduct.text = "Diterima"
                    tvHargaProduct.apply {
                        text = striketroughtText(this, basePrice)
                    }
                }
                if (data.status == "pending") {
                    tvPenawaranProduct.text = "Ditawar"
                    tvHargaProduct.apply {
                        text = striketroughtText(this, basePrice)
                    }
                }
            }
        }
    }

    interface OnClickListener {
        fun onClickItem(data: SellerOrderResponse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ListItemDiminatiBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = differ.currentList[position]
        data.let {
            holder.bind(data)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size
}