package com.mib.feature_home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.mib.feature_home.R
import com.mib.feature_home.databinding.AdapterLoadingItemBinding
import com.mib.feature_home.databinding.AdapterOrderHistoryBinding
import com.mib.feature_home.domain.model.OrderHistory
import com.mib.feature_home.utils.CustomUtils
import com.mib.feature_home.utils.withThousandSeparator
import java.math.BigDecimal

class OrderHistoryAdapter(
    val itemList: MutableList<OrderHistory>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder  {
        return if (viewType == VIEW_TYPE_ITEM) {
            val itemBinding = AdapterOrderHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            OrderHistoryItemHolder(parent.context, itemBinding, onItemClickListener)
        } else {
            val itemBinding = AdapterLoadingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            LoadingViewHolder(itemBinding)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemList[position].code.isBlank()) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is OrderHistoryItemHolder) {
            val orderHistory: OrderHistory = itemList[position]
            holder.bind(orderHistory)
        } else if (holder is LoadingViewHolder) {
            showLoadingView(holder, position)
        }
    }

    class OrderHistoryItemHolder(
        private val context: Context,
        private val itemBinding: AdapterOrderHistoryBinding,
        private val adapterListener: OnItemClickListener
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: OrderHistory) {
            itemBinding.tvOrderName.text = item.code
            itemBinding.tvMerchantName.text = item.merchantName
            // TODO: image

            itemBinding.tvOrderId.text = item.code
            itemBinding.tvDate.text = item.orderDate
            itemBinding.tvPrice.text = context.getString(R.string.currency_format, item.totalPayment.toString().withThousandSeparator())
            itemBinding.tvStatus.text = CustomUtils.getUserFriendlyOrderStatusName(context, item.status)

            itemBinding.rlOrderHistory.setOnClickListener {
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
        itemList.add(OrderHistory(
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            BigDecimal.ZERO,
            ""
        ))
        notifyItemInserted(itemList.size-1)
    }

    fun removeLoadingFooter() {
        itemList.removeAt(itemList.size-1)
        notifyItemRemoved(itemList.size)
    }

    interface OnItemClickListener {
        fun onClick(item: OrderHistory)
    }

    fun addList(items: MutableList<OrderHistory>?) {
        itemList.addAll(items ?: emptyList())
        notifyDataSetChanged()
    }

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1
    }
}