package com.ards.ui.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ards.R
import com.ards.ui.profile.model.MenuItem

class ProfileMenuAdapter(
    private val context: Context,
    private val menuList: List<MenuItem>,
    private val callback: Callback
) :
    RecyclerView.Adapter<ProfileMenuAdapter.MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.menu_item, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menuItem = menuList[position]
        holder.tvMenuTitle.text = menuItem.title
        holder.imgIcon.setImageResource(menuItem.icon)
        holder.itemMenuList.setOnClickListener {
            callback.onItemClicked(menuItem.title)
        }
    }

    override fun getItemCount(): Int = menuList.size

    class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemMenuList: LinearLayout = itemView.findViewById(R.id.itemMenuList)
        val imgIcon: ImageView = itemView.findViewById(R.id.imgIcon)
        val tvMenuTitle: TextView = itemView.findViewById(R.id.tvMenuTitle)
    }

    interface Callback {
        fun onItemClicked(title: String)
    }
}
