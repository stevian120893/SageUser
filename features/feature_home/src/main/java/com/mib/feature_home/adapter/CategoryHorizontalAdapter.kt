package com.mib.feature_home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mib.feature_home.R
import com.mib.feature_home.contents.bottom_menu.home.HomeFragment.Companion.CATEGORY_ALL
import com.mib.feature_home.databinding.AdapterCategoryHorizontalBinding
import com.mib.feature_home.domain.model.Category

class CategoryHorizontalAdapter(
    private val itemList: MutableList<Category>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<CategoryHorizontalAdapter.CategoryItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemHolder  {
        val itemBinding = AdapterCategoryHorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryItemHolder(parent.context, itemBinding, onItemClickListener)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: CategoryItemHolder, position: Int) {
        val category: Category = itemList[position]
        holder.bind(category, position)
    }

    class CategoryItemHolder(
        private val context: Context,
        private val itemBinding: AdapterCategoryHorizontalBinding,
        private val adapterListener: OnItemClickListener
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(category: Category, pos: Int) {
            when(category.categoryCode) {
                CATEGORY_ALL -> {
                    itemBinding.ivCategory.setImageResource(R.drawable.ic_all_menu)
                }
                else -> {
                    Glide.with(context).load(category.imageUrl).into(itemBinding.ivCategory)
                }
            }
            itemBinding.tvCategory.text = category.categoryName

            // update bg
            val background = if(category.selected) R.drawable.bg_rounded_stroke_6 else R.drawable.bg_rounded_6
            itemBinding.llAdapterParent.setBackgroundResource(background)

            itemBinding.llAdapterParent.setOnClickListener {
                adapterListener.onClick(category, pos)
            }
        }
    }

    fun getItemIndex(categoryCode: String?): Int {
        val item = itemList.first { item -> item.categoryCode == categoryCode }
        return itemList.indexOf(item)
    }

    fun setItemSelected(pos: Int, selected: Boolean) {
        if (itemCount > pos) {
            itemList[pos].selected = selected
            notifyItemChanged(pos)
        }
    }

    interface OnItemClickListener {
        fun onClick(category: Category, pos: Int)
    }
}