package com.ruderarajput.earningapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ruderarajput.earningapp.Models.HistoryModelClass
import com.ruderarajput.earningapp.databinding.FragmentSpinBinding
import com.ruderarajput.earningapp.databinding.HistoryItemBinding
import java.sql.Timestamp
import java.util.Date

class HistoryAdapter(var ListHistory: ArrayList<HistoryModelClass>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryCoinViewHolder>() {
    class HistoryCoinViewHolder(var binding: HistoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryCoinViewHolder {
        return HistoryCoinViewHolder(
            HistoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = ListHistory.size

    override fun onBindViewHolder(holder: HistoryCoinViewHolder, position: Int) {
        var timeStamp=Timestamp(ListHistory.get(position).timeAndDate.toLong())
        holder.binding.time.text = Date(timeStamp.time).toString()
        holder.binding.status.text=if (ListHistory.get(position).isWithDrawal){"- Money Withdrawal"}else{"+ Coin Added"}
        holder.binding.coin.text = ListHistory[position].coin
    }
}