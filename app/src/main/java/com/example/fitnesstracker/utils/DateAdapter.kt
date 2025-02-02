package com.example.fitnesstracker.utils

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesstracker.R
import java.text.SimpleDateFormat
import java.util.*

class  DateAdapter(private val dates: List<String>, private val listener: OnDateClickListener) :
    RecyclerView.Adapter<DateAdapter.DateViewHolder>() {

    private var selectedPosition = -1

    interface OnDateClickListener {
        fun onDateClick(date: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.date_item, parent, false)
        return DateViewHolder(view)
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        val date = dates[position]
        holder.bind(date)

        val sdf = SimpleDateFormat("dd\nMMM", Locale.getDefault())
        val currentDate = sdf.format(Date())

        val context = holder.dateTextView.context
        val drawable: Drawable? = ContextCompat.getDrawable(context, R.drawable.date_background)

        if (position == selectedPosition) {
            drawable?.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(context,
                R.color.yellow
            ), PorterDuff.Mode.SRC_IN)
        } else if (date == currentDate && selectedPosition == -1) {
            drawable?.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(context,
                R.color.yellow
            ), PorterDuff.Mode.SRC_IN)
            selectedPosition = position // Set the initial selected position to today's date
        } else {
            drawable?.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(context,
                R.color.gray
            ), PorterDuff.Mode.SRC_IN)
        }

        holder.dateTextView.background = drawable
    }

    override fun getItemCount(): Int = dates.size

    inner class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)

        fun bind(date: String) {
            dateTextView.text = date
            itemView.setOnClickListener {
                listener.onDateClick(date)
                selectedPosition = adapterPosition
                notifyDataSetChanged()
            }
        }
    }
}
