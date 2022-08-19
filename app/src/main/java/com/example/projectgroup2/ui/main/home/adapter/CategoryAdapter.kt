package com.example.projectgroup2.ui.main.home.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projectgroup2.R
import com.example.projectgroup2.data.api.main.buyer.product.ProductResponseItem
import com.example.projectgroup2.data.api.main.category.CategoryResponse
import com.example.projectgroup2.databinding.ListCategoryHomeBinding
import com.example.projectgroup2.databinding.ListProductHomeBinding

class CategoryAdapter(private val onClick: OnClickListener): RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    var rowIndex = -1
    private val diffCallBack = object: DiffUtil.ItemCallback<CategoryResponse>(){
        override fun areItemsTheSame(oldItem: CategoryResponse, newItem: CategoryResponse): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: CategoryResponse, newItem: CategoryResponse): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this,diffCallBack)
    fun submitData(value: List<CategoryResponse>?) = differ.submitList(value)

    interface OnClickListener {
        fun onClickItem (data: CategoryResponse)
    }

    inner class ViewHolder(private val binding: ListCategoryHomeBinding): RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
        fun bind (data: CategoryResponse, position: Int){
            binding.tvCategoryHome.text = data.name
            binding.root.setOnClickListener {
                onClick.onClickItem(data)
                rowIndex = position
                notifyDataSetChanged()
            }
            if (rowIndex == position){
                binding.cardCategory.setBackgroundColor(Color.parseColor("#39E1C1"))
                binding.tvCategoryHome.setTextColor(Color.parseColor("#FFFFFF"))
            }else{
                binding.cardCategory.setBackgroundColor(Color.parseColor("#FFFFFF"))
                binding.tvCategoryHome.setTextColor(Color.parseColor("#000000"))
                binding.cardCategory.setBackgroundResource(R.drawable.bg_category_home)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.ViewHolder {
        val inflate = LayoutInflater.from(parent.context)
        return ViewHolder(ListCategoryHomeBinding.inflate(inflate,parent,false))
    }

    override fun onBindViewHolder(holder: CategoryAdapter.ViewHolder, position: Int) {
        val data = differ.currentList[position]
        data.let {
            holder.bind(data, position)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}