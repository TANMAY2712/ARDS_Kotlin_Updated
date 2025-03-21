package com.ards.ui.history.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ards.R
import com.ards.remote.apimodel.NotificationListResponse
import com.ards.utils.DateUtils

class RecentAdapter(
    private val context: Context,
    private var trainList: List<NotificationListResponse.DataResponse.Faults>,
    private val callback: Callback
) :
    RecyclerView.Adapter<RecentAdapter.TrainViewHolder>() {

    class TrainViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val trainName: TextView = view.findViewById(R.id.trainName)
        val trainNo: TextView = view.findViewById(R.id.trainNumber)
        val trainRoute: TextView = view.findViewById(R.id.trainRoute)
        val trainTime: TextView = view.findViewById(R.id.trainTime)
        val faults: TextView = view.findViewById(R.id.faultsCount)
        val faultImage: ImageView = view.findViewById(R.id.arrowIconFault)
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
        holder.faults.text = "Faults: "+train.TotalFaults
        holder.faultImage.setOnClickListener {
            if (train.TotalFaults.toInt()>0){
                callback.onItemClicked(train.Id)
            }else{
                Toast.makeText(context,"No Fault Found",Toast.LENGTH_SHORT).show()
               // showNoDataDialog(context)
            }

        }
    }

    override fun getItemCount(): Int = trainList.size

    interface Callback {
        fun onItemClicked(notificationId: Int)
    }
    fun showNoDataDialog(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("Data Unavailable")
            .setMessage("No fault found.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss() // Close dialog
            }
            .show()
    }
}