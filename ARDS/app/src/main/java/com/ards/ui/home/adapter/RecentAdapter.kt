package com.ards.ui.history.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ards.R
import com.ards.ui.history.model.Recent

class RecentAdapter(private val trainList: List<Recent>) :
    RecyclerView.Adapter<RecentAdapter.TrainViewHolder>() {

    class TrainViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val trainName: TextView = view.findViewById(R.id.trainName)
        val trainNo: TextView = view.findViewById(R.id.trainNumber)
        val trainRoute: TextView = view.findViewById(R.id.trainRoute)
        val trainTime: TextView = view.findViewById(R.id.trainTime)
       // val from: TextView = view.findViewById(R.id.tvFrom)
       // val departure: TextView = view.findViewById(R.id.tvDeparture)
       // val to: TextView = view.findViewById(R.id.tvTo)
       // val arrival: TextView = view.findViewById(R.id.tvArrival)
        val faults: TextView = view.findViewById(R.id.faultsCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recents_item, parent, false)
        return TrainViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrainViewHolder, position: Int) {
        val train = trainList[position]
        holder.trainName.text = train.trainName
        holder.trainNo.text = train.trainNo
        holder.trainRoute.text = train.from+" -> "+train.to
        holder.trainTime.text = train.departure+" -> "+ train.arrival
       // holder.from.text = train.from
       // holder.departure.text = train.departure
      //  holder.to.text = train.to
      //  holder.arrival.text = train.arrival
        holder.faults.text = "Faults: ${train.faults}"
    }

    override fun getItemCount(): Int = trainList.size
}
