package com.mib.feature_home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mib.feature_home.databinding.AdapterCategoriesItemBinding
import com.mib.feature_home.domain.model.Category

class CategoriesAdapter(
    val context: Context,
    private val itemList: MutableList<Category>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<CategoriesAdapter.CategoryItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemHolder  {
        val itemBinding = AdapterCategoriesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryItemHolder(itemBinding, onItemClickListener)
    }

    override fun getItemCount(): Int {
        val limit = 6
        val size = itemList.size
        return if(size > limit) limit else size
    }

    override fun onBindViewHolder(holder: CategoryItemHolder, position: Int) {
        val category: Category = itemList[position]
        holder.bind(category)
    }

    class CategoryItemHolder(
        private val itemBinding: AdapterCategoriesItemBinding,
        private val adapterListener: OnItemClickListener
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(category: Category) {
            itemBinding.tvCategory.text = category.categoryName

            itemBinding.llAdapterParent.setOnClickListener {
                adapterListener.onClick(category)
            }
        }
    }

    interface OnItemClickListener {
        fun onClick(category: Category)
    }
}