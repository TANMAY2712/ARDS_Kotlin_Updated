package com.ards.ui.library.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ards.R
import com.ards.remote.apimodel.MasterDataResponse

class LibraryAdapter(
    private val context: Context,
    private var componentList: MutableList<MasterDataResponse.DataResponse>,
    private val callback: Callback
) : RecyclerView.Adapter<LibraryAdapter.ComponentViewHolder>() {

    private var selectedPosition: Int = 0 // ✅ Default selection at position 0

    class ComponentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.imgLibraryItem)
        val name: TextView = view.findViewById(R.id.tvLibraryTitle)
        val description: TextView = view.findViewById(R.id.tvLibraryDescription)
        val totalVideos: TextView = view.findViewById(R.id.tvTotalVideos)
        val btnView: Button = view.findViewById(R.id.btnView)
        val container: View = view // Store the entire item view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComponentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.library_item, parent, false)
        return ComponentViewHolder(view)
    }

    override fun onBindViewHolder(holder: ComponentViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val component = componentList[position]
        holder.name.text = component.MasterDataName
        holder.description.text = component.MasterDataDescription ?: "No description available"
        holder.totalVideos.text = "Total Videos: ${component.MasterDataId}"

        // ✅ Set background color based on selection
        if (position == selectedPosition) {
            holder.container.setBackgroundColor(Color.parseColor("#6C5FB8")) // Selected color
        } else {
            holder.container.setBackgroundColor(Color.WHITE) // Default color
        }

        // ✅ Click Effect: Update selection on click
        holder.container.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = position

            // Notify RecyclerView to refresh the old & new selected items
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)

            // Call API when item is selected
            callback.onItemClicked(component.MasterDataId)
        }
    }

    override fun getItemCount(): Int = componentList.size

    fun updateData(newData: List<MasterDataResponse.DataResponse>) {
        componentList.clear()
        componentList.addAll(newData)

        // ✅ Ensure position 0 is selected after data update
        if (componentList.isNotEmpty()) {
            selectedPosition = 0
            notifyDataSetChanged()
            callback.onItemClicked(componentList[0].MasterDataId) // ✅ Call API for first item
        }
    }

    interface Callback {
        fun onItemClicked(categoryId: Int)
    }
}
