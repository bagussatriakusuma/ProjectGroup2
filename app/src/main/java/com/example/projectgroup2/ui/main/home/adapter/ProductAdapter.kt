package com.example.projectgroup2.ui.main.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projectgroup2.utils.currency
import com.example.projectgroup2.data.api.main.buyer.product.GetProductResponse
import com.example.projectgroup2.databinding.ListProductHomeBinding

class ProductAdapter(private val onClick: OnClickListener): RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    private val diffCallBack = object: DiffUtil.ItemCallback<GetProductResponse>(){
        override fun areItemsTheSame(oldItem: GetProductResponse, newItem: GetProductResponse): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: GetProductResponse, newItem: GetProductResponse): Boolean {
            return oldItem.name == newItem.name
        }
    }

    private val differ = AsyncListDiffer(this,diffCallBack)
    fun submitData(value: List<GetProductResponse>?) = differ.submitList(value)

    interface OnClickListener {
        fun onClickItem (data: GetProductResponse)
    }

    inner class ViewHolder(private val binding: ListProductHomeBinding): RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        var listCategory = ""
        fun bind (data: GetProductResponse){
            Glide.with(binding.root)
                .load(data.imageUrl)
                .into(binding.ivProductImg)
            binding.tvProductName.text = data.name
//            binding.tvProductCategory.text = data.categories.map { it.name }.toString()
            if (data.categories.isNotEmpty()) {
                for (data in data.categories){
                    listCategory += ", ${data.name}"
                }
                binding.tvProductCategory.text = listCategory.drop(2)
            }
            binding.tvProductPrice.text = currency(data.basePrice)
            binding.root.setOnClickListener {
                onClick.onClickItem(data)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductAdapter.ViewHolder {
        val inflate = LayoutInflater.from(parent.context)
        return ViewHolder(ListProductHomeBinding.inflate(inflate,parent,false))
    }

    override fun onBindViewHolder(holder: ProductAdapter.ViewHolder, position: Int) {
        val data = differ.currentList[position]
        data.let {
            holder.bind(data)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}