package com.ards.ui.notification.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ards.R
import com.ards.remote.apimodel.NotificationListRequest
import com.ards.remote.apimodel.NotificationListResponse

class NotificationAdapter(
    private val context: Context,
    private var componentList: List<NotificationListResponse.DataResponse.Faults>
) :
    RecyclerView.Adapter<NotificationAdapter.ComponentViewHolder>() {

    class ComponentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.image_notification)
        val title: TextView = view.findViewById(R.id.tv_notification_title)
        val trainName: TextView = view.findViewById(R.id.tv_trainName)
        val trainSide: TextView = view.findViewById(R.id.tv_train_side)
        val station: TextView = view.findViewById(R.id.tv_station)
        val raisedby: TextView = view.findViewById(R.id.tv_raisedby)
        val createdDate: TextView = view.findViewById(R.id.tv_raisedtime)
        val btnView: ImageView = view.findViewById(R.id.btn_arrowIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComponentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return ComponentViewHolder(view)
    }

    override fun onBindViewHolder(holder: ComponentViewHolder, position: Int) {
        val component = componentList[position]
        //holder.image.setImageResource(component.imageResId)
        holder.title.text = component.title
        holder.trainName.text = component.train_name+" ("+component.train_name+")"
        holder.trainSide.text = component.coach_side
        holder.station.text = component.station_name
        holder.raisedby.text = component.UserName+" ("+component.MobileNumber+")"
        holder.createdDate.text = component.createdDate
        //holder.totalVideos.text = "Total Videos: ${component.totalVideos}"

        holder.btnView.setOnClickListener {
            // Handle button click (e.g., open video list)
        }
    }

    override fun getItemCount(): Int = componentList.size
}
