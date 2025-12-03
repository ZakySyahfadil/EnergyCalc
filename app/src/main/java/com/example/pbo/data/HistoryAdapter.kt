// file: app/src/main/java/com/example/pbo/data/HistoryAdapter.kt
package com.example.pbo.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pbo.R

class HistoryAdapter : ListAdapter<HistoryEntity, HistoryAdapter.HistoryViewHolder>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<HistoryEntity>() {
            override fun areItemsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean =
                oldItem == newItem
        }
    }

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDevice = itemView.findViewById<TextView>(R.id.tvDeviceName)
        private val tvDate = itemView.findViewById<TextView>(R.id.tvDate)

        fun bind(item: HistoryEntity) {
            tvDevice.text = item.deviceName
            tvDate.text = item.date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(v)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}