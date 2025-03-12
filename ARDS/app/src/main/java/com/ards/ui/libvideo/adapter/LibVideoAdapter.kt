package com.ards.ui.libvideo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ards.R
import com.ards.remote.apimodel.VideoByCategoryResponse
import com.ards.ui.library.adapter.LibraryAdapter

class LibVideoAdapter(
    private val context: Context,
    private val componentList: List<VideoByCategoryResponse.DataResponse>,
    private val callback: Callback
) :
    RecyclerView.Adapter<LibVideoAdapter.ComponentViewHolder>() {

    class ComponentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.image_video_thumbnail)
        val name: TextView = view.findViewById(R.id.tv_video_title)
        val description: TextView = view.findViewById(R.id.tv_video_desc)
        val btnView: Button = view.findViewById(R.id.btn_video_play)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComponentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lib_video, parent, false)
        return ComponentViewHolder(view)
    }

    override fun onBindViewHolder(holder: ComponentViewHolder, position: Int) {
        val component = componentList[position]
        //holder.image.setImageResource(component.imageResId)
        holder.name.text = component.VideoTiltle
        //holder.description.text = component.MasterDataDescription
        holder.description.text = "Total Videos: ${component.VideoDesc}"

        holder.btnView.setOnClickListener {
            // Handle button click (e.g., open video list)
            callback.onItemClicked(component.VideoUrl)
        }
    }

    override fun getItemCount(): Int = componentList.size

    interface Callback {
        fun onItemClicked(videoUrl: String)
    }
}
