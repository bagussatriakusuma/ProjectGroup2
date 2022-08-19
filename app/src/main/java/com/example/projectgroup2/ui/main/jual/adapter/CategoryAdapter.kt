package com.example.projectgroup2.ui.main.jual.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.projectgroup2.R
import com.example.projectgroup2.data.api.main.category.CategoryResponse
import com.example.projectgroup2.databinding.ListProductCategoryBinding
import com.example.projectgroup2.utils.listCategory

class CategoryAdapter(
    private val selected: (CategoryResponse)->Unit,
    private val unselected: (CategoryResponse)->Unit
) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    private val diffCallBack = object : DiffUtil.ItemCallback<CategoryResponse>(){

        override fun areItemsTheSame(
            oldItem: CategoryResponse,
            newItem: CategoryResponse
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: CategoryResponse,
            newItem: CategoryResponse
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }
    private val differ = AsyncListDiffer(this,diffCallBack)
    fun submitData(value:List<CategoryResponse>?) = differ.submitList(value)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ListProductCategoryBinding.inflate(inflater,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = differ.currentList[position]
        data.let {
            holder.bind(data)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class ViewHolder(private val binding: ListProductCategoryBinding):
        RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(data: CategoryResponse){
            binding.apply {
                cardCategoryJual.setBackgroundResource(R.drawable.bg_roundstroke)
                cbCategory.text = data.name
                cbCategory.isChecked = listCategory.contains(data.name)
                cbCategory.setOnClickListener{
                    if (!listCategory.contains(data.name)){
                        selected.invoke(data)
                    } else {
                        unselected.invoke(data)
                    }
                }
            }
        }
    }

}