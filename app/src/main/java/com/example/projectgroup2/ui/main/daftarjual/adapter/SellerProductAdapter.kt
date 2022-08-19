package com.example.projectgroup2.ui.main.daftarjual.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projectgroup2.data.api.main.sellerproduct.SellerProductResponse
import com.example.projectgroup2.databinding.ListProductHomeBinding
import com.example.projectgroup2.utils.currency

class SellerProductAdapter(private val onClick: OnClickListener): RecyclerView.Adapter<SellerProductAdapter.ViewHolder>() {

    private val diffCallBack = object: DiffUtil.ItemCallback<SellerProductResponse>(){
        override fun areItemsTheSame(oldItem: SellerProductResponse, newItem: SellerProductResponse): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: SellerProductResponse, newItem: SellerProductResponse): Boolean {
            return oldItem.name == newItem.name
        }
    }

    private val differ = AsyncListDiffer(this,diffCallBack)
    fun submitData(value: List<SellerProductResponse>?) = differ.submitList(value)

    interface OnClickListener {
        fun onClickItem (data: SellerProductResponse)
    }

    inner class ViewHolder(private val binding: ListProductHomeBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: SellerProductResponse) {
            binding.apply {
                Glide.with(binding.root)
                    .load(data.imageUrl)
                    .into(binding.ivProductImg)
                tvProductName.text = data.name
                if (data.categories.isNotEmpty()) {
                    if (data.categories.size > 2) {
                        tvProductCategory.text =
                            "${data.categories[0].name}, ${data.categories[1].name}, ${data.categories[2].name} "
                    } else if (data.categories.size > 1) {
                        tvProductCategory.text = "${data.categories[0].name}, ${data.categories[1].name}"
                    } else {
                        tvProductCategory.text = "${data.categories[0].name}"
                    }
                }
                val price = currency(data.basePrice)
                tvProductPrice.text = price
                root.setOnClickListener {
                    onClick.onClickItem(data)
                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellerProductAdapter.ViewHolder {
        val inflate = LayoutInflater.from(parent.context)
        return ViewHolder(ListProductHomeBinding.inflate(inflate,parent,false))
    }

    override fun onBindViewHolder(holder: SellerProductAdapter.ViewHolder, position: Int) {
        val data = differ.currentList[position]
        data.let {
            holder.bind(data)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}