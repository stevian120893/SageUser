package com.mib.feature_home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mib.feature_home.databinding.AdapterListBinding
import com.mib.feature_home.domain.model.City

class CityAdapter(
    private val itemList: List<City>,
    private val clickListener: OnItemClickListener,
) : RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val itemBinding = AdapterListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CityViewHolder(parent.context, itemBinding, clickListener)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val item: City = itemList[position]
        holder.bind(item)
    }

    interface OnItemClickListener {
        fun onClick(city: City)
    }

    class CityViewHolder(
        private val context: Context,
        private val itemBinding: AdapterListBinding,
        private val adapterListener: OnItemClickListener
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(city: City) {
            itemBinding.tvName.text = city.name

            itemBinding.llAdapterParent.setOnClickListener {
                adapterListener.onClick(city)
            }
        }
    }
}