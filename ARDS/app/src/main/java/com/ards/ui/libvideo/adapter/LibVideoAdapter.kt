package com.ards.ui.libvideo.adapter

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ards.R
import com.ards.remote.apimodel.VideoByCategoryResponse
import com.bumptech.glide.Glide

class LibVideoAdapter(
    private val context: Context,
    private val componentList: List<VideoByCategoryResponse.DataResponse>,
    private val callback: Callback
) : RecyclerView.Adapter<LibVideoAdapter.ComponentViewHolder>() {

    private val likedVideos = HashSet<String>()

    class ComponentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.videoThumbnail)
        val name: TextView = view.findViewById(R.id.videoTitle)
        val description: TextView = view.findViewById(R.id.videoDescription)
        val btnView: LinearLayout = view.findViewById(R.id.linear)

        val btnLike: ImageView = view.findViewById(R.id.btnLike)
        val btnShare: ImageView = view.findViewById(R.id.btnShare)
        val btnComment: ImageView = view.findViewById(R.id.btnComment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComponentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lib_video, parent, false)
        return ComponentViewHolder(view)
    }

    override fun onBindViewHolder(holder: ComponentViewHolder, position: Int) {
        val component = componentList[position]

        holder.name.text = component.VideoTiltle
        holder.description.text =component.VideoDesc

        val videoId = extractYouTubeId(component.VideoUrl)
        val thumbnailUrl = "https://img.youtube.com/vi/$videoId/0.jpg"

        Glide.with(holder.itemView.context)
            .load(thumbnailUrl)
            .placeholder(R.drawable.train_spring)
            .into(holder.image)

        updateLikeButton(holder.btnLike, component.VideoUrl)

        holder.btnView.setOnClickListener {
            callback.onItemClickedVideo(component.VideoUrl)
        }

        holder.btnLike.setOnClickListener {
            if (likedVideos.contains(component.VideoUrl)) {
                likedVideos.remove(component.VideoUrl)
            } else {
                likedVideos.add(component.VideoUrl)
            }
            updateLikeButton(holder.btnLike, component.VideoUrl)
            callback.onLikeClicked(component.VideoUrl, position)
        }

        holder.btnShare.setOnClickListener {
            shareVideo(component.VideoUrl)
        }

        holder.btnComment.setOnClickListener {
            openCommentDialog(component.VideoUrl)
        }
    }

    override fun getItemCount(): Int = componentList.size

    interface Callback {
        fun onItemClickedVideo(videoUrl: String)
        fun onLikeClicked(videoUrl: String, position: Int)
        fun onCommentSubmitted(videoUrl: String, comment: String)
    }

    private fun extractYouTubeId(url: String): String {
        return try {
            val regex =
                "(?<=watch\\?v=|/videos/|embed/|youtu.be/|/v/|/e/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*"
            val pattern = Regex(regex)
            val match = pattern.find(url)
            match?.value ?: ""
        } catch (e: Exception) {
            ""
        }
    }

    private fun shareVideo(videoUrl: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Check out this video!")
            putExtra(Intent.EXTRA_TEXT, videoUrl)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    private fun updateLikeButton(btnLike: ImageView, videoUrl: String) {
        if (likedVideos.contains(videoUrl)) {
            btnLike.setColorFilter(ContextCompat.getColor(context, R.color.red))
        } else {
            btnLike.setColorFilter(ContextCompat.getColor(context, R.color.black))
        }
    }

    // Function to Show Comment Dialog
    private fun openCommentDialog(videoUrl: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Add a Comment")

        val input = EditText(context)
        input.hint = "Enter your comment"
        builder.setView(input)

        builder.setPositiveButton("Submit") { dialog, _ ->
            val comment = input.text.toString().trim()
            if (comment.isNotEmpty()) {
                callback.onCommentSubmitted(videoUrl, comment)
                Toast.makeText(context, "Comment Submitted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Comment cannot be empty!", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }
}
