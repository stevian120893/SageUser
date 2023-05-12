package com.mib.feature_home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mib.feature_home.R
import com.mib.feature_home.databinding.AdapterLoadingItemBinding
import com.mib.feature_home.databinding.AdapterPromoBinding
import com.mib.feature_home.domain.model.Promo

class PromoAdapter(
    val itemList: MutableList<Promo>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder  {
        return if (viewType == VIEW_TYPE_ITEM) {
            val itemBinding = AdapterPromoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            PromoItemHolder(parent.context, itemBinding, onItemClickListener)
        } else {
            val itemBinding = AdapterLoadingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            LoadingViewHolder(itemBinding)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemList[position].promoTitle.isBlank()) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PromoItemHolder) {
            val promo: Promo = itemList[position]
            holder.bind(promo)
        } else if (holder is LoadingViewHolder) {
            showLoadingView(holder, position)
        }
    }

    class PromoItemHolder(
        private val context: Context,
        private val itemBinding: AdapterPromoBinding,
        private val adapterListener: OnItemClickListener
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: Promo) {
            Glide.with(context).load(item.promoImageUrl).into(itemBinding.ivPromo)
            itemBinding.tvPromo.text = item.promoTitle

            itemBinding.rlPromo.setOnClickListener {
                adapterListener.onClick(item)
            }
        }
    }

    private class LoadingViewHolder(itemBinding: AdapterLoadingItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        var progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
    }

    private fun showLoadingView(viewHolder: LoadingViewHolder, position: Int) {
        // progressBar would be displayed
    }

    fun addLoadingFooter() {
        itemList.add(Promo(
            "",
            "",
            "",
            0,
            0L,
            0L,
            false,
            "",
            ""
        ))
        notifyItemInserted(itemList.size-1)
    }

    fun removeLoadingFooter() {
        itemList.removeAt(itemList.size-1)
        notifyItemRemoved(itemList.size)
    }

    interface OnItemClickListener {
        fun onClick(item: Promo)
    }

    fun addList(items: MutableList<Promo>?) {
        itemList.addAll(items ?: emptyList())
        notifyDataSetChanged()
    }

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1
    }
}