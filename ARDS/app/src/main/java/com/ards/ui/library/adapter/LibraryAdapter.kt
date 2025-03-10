package com.ards.ui.library.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ards.R
import com.ards.ui.library.model.Library

class LibraryAdapter(private val componentList: List<Library>) :
    RecyclerView.Adapter<LibraryAdapter.ComponentViewHolder>() {

    class ComponentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.imgLibraryItem)
        val name: TextView = view.findViewById(R.id.tvLibraryTitle)
        val description: TextView = view.findViewById(R.id.tvLibraryDescription)
        val totalVideos: TextView = view.findViewById(R.id.tvTotalVideos)
        val btnView: Button = view.findViewById(R.id.btnView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComponentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.library_item, parent, false)
        return ComponentViewHolder(view)
    }

    override fun onBindViewHolder(holder: ComponentViewHolder, position: Int) {
        val component = componentList[position]
        holder.image.setImageResource(component.imageResId)
        holder.name.text = component.name
        holder.description.text = component.description
        holder.totalVideos.text = "Total Videos: ${component.totalVideos}"

        holder.btnView.setOnClickListener {
            // Handle button click (e.g., open video list)
        }
    }

    override fun getItemCount(): Int = componentList.size
}
