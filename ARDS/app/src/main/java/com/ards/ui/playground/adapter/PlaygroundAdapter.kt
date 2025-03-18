package com.ards.ui.playground.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ards.R
import com.ards.model.Playground
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class PlaygroundAdapter(
    private var videoList: List<Playground>,
    private val onItemClick: (Playground) -> Unit
) : RecyclerView.Adapter<PlaygroundAdapter.PlaygroundViewHolder>() {

    class PlaygroundViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.trainName)
        val trainNo: TextView = itemView.findViewById(R.id.trainNo)
        val trainDate: TextView = itemView.findViewById(R.id.trainDate)
        val trainStation: TextView = itemView.findViewById(R.id.trainStation)
        val recordingSide: TextView = itemView.findViewById(R.id.recordingSide)
        val thumbnil: ImageView = itemView.findViewById(R.id.playerView)

        fun bind(playground: Playground, onItemClick: (Playground) -> Unit) {
            tvTitle.text = playground.train_name
            trainNo.text = playground.train_number

            trainDate.text = "Date: "+playground.date
            trainStation.text = "Station: "+playground.station
            recordingSide.text = "Recording Side: "+playground.side

            itemView.setOnClickListener { onItemClick(playground) }
            Glide.with(thumbnil.context)
                .load(playground.thumbnail_url)
                .apply(RequestOptions().fitCenter()) // Ensures the image fits while maintaining aspect ratio
                .into(thumbnil)
           // loadVideoThumbnail(playground.thumbnail_url, thumbnil)

        }
        private fun loadVideoThumbnail(videoUrl: String, imageView: ImageView) {
            val options = RequestOptions()
                .frame(1000000) // Extract frame at 1 second


            Glide.with(imageView.context)
                .setDefaultRequestOptions(options)
                .load(videoUrl)
                .into(imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaygroundViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.playground_item, parent, false)
        return PlaygroundViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaygroundViewHolder, position: Int) {
        holder.bind(videoList[position], onItemClick)
    }

    override fun getItemCount(): Int = videoList.size

    fun updateData(newVideos: List<Playground>) {
        videoList = newVideos
        notifyDataSetChanged()
    }

}
