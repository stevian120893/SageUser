package com.mib.feature_home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mib.feature_home.databinding.AdapterPromosItemBinding
import com.mib.feature_home.domain.model.Banner

class BannerAdapter(
    private val itemList: List<Banner>,
    private val clickListener: OnItemClickListener,
) : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val itemBinding = AdapterPromosItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BannerViewHolder(parent.context, itemBinding, clickListener)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        val product: Banner = itemList[position]
        holder.bind(product)
    }

    interface OnItemClickListener {
        fun onClick(banner: Banner)
    }

    class BannerViewHolder(
        private val context: Context,
        private val itemBinding: AdapterPromosItemBinding,
        private val adapterListener: OnItemClickListener
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(banner: Banner) {
            Glide.with(context).load(banner.imageUrl).into(itemBinding.ivBanner)
            itemBinding.tvBanner.text = banner.title

            itemBinding.ivBanner.setOnClickListener {
                adapterListener.onClick(banner)
            }
        }
    }
}