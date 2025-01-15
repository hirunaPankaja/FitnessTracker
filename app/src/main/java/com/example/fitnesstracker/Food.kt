package com.example.fitnesstracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class Food : Fragment(), DateAdapter.OnDateClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_food, container, false)

        val dateRecyclerView: RecyclerView = view.findViewById(R.id.dateRecyclerView)
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        dateRecyclerView.layoutManager = linearLayoutManager

        val calendar = Calendar.getInstance()
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        val dates = DateUtils().generateDates(month, year)
        val adapter = DateAdapter(dates, this)
        dateRecyclerView.adapter = adapter

        // Scroll to and center today's date
        val sdf = SimpleDateFormat("dd\nMMM", Locale.getDefault())
        val currentDate = sdf.format(Date())
        val todayPosition = dates.indexOf(currentDate)
        if (todayPosition != -1) {
            dateRecyclerView.post {
                val offset = (dateRecyclerView.width / 2) - (view.findViewById<View>(R.id.dateTextView).width / 2)
                linearLayoutManager.scrollToPositionWithOffset(todayPosition, offset)
            }
        }

        return view
    }

    override fun onDateClick(date: String) {
        val dateInformation = retrieveDateInformation(date)
    }

    private fun retrieveDateInformation(date: String): DateUtils {
        return DateUtils()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Food().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
