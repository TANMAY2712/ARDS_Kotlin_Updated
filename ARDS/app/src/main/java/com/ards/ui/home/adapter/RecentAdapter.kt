package com.ards.ui.history.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ards.R
import com.ards.remote.apimodel.NotificationListResponse
import com.ards.ui.history.model.Recent
import com.ards.utils.DateUtils

class RecentAdapter(
    private val context: Context,
    private var trainList: List<NotificationListResponse.DataResponse.Faults>
) :
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
        holder.trainName.text = train.train_name
        holder.trainNo.text = train.train_number
        holder.trainRoute.text = train.station_name//train.from+" -> "+train.to
        holder.trainTime.text = DateUtils.getAppDateFromApiDate(train.createdDate)//train.departure+" -> "+ train.arrival
        // holder.from.text = train.from
        // holder.departure.text = train.departure
        //  holder.to.text = train.to
        //  holder.arrival.text = train.arrival
        holder.faults.text = "Faults: 3"
    }

    override fun getItemCount(): Int = trainList.size
}