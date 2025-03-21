import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ards.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.chrisbanes.photoview.PhotoView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FaultImageAdapter(
    private val imageList: List<Fault>,
    private val onImageClick: (Int) -> Unit, // Callback for highlighting fault item
    private val onTimestampClick: (String) -> Unit // Callback for seeking to timestamp
) : RecyclerView.Adapter<FaultImageAdapter.ImageViewHolder>() {

    private var selectedPosition: Int = RecyclerView.NO_POSITION // Track selected image

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_fault_image_list, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(imageList[position], position)
    }

    override fun getItemCount(): Int = imageList.size

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val faultImage: ImageView = itemView.findViewById(R.id.fault_image)

        fun bind(faultItem: Fault, position: Int) {
            // Apply background based on selection
            faultImage.background = if (selectedPosition == position) {
                ContextCompat.getDrawable(itemView.context, R.drawable.selected_image_border)
            } else {
                null
            }

            Glide.with(faultImage.context)
                .load(faultItem.imageUrl)
                .apply(RequestOptions().fitCenter()) // Ensures the image fits while maintaining aspect ratio
                .into(faultImage)

            faultImage.setOnClickListener {
                val previousSelected = selectedPosition
                selectedPosition = bindingAdapterPosition

                notifyItemChanged(previousSelected)
                notifyItemChanged(selectedPosition)

                faultItem.faultTimestamp?.let { faultTimestamp ->
                    val faultTime = convertFaultTimestamp(faultTimestamp)
                    onTimestampClick(faultTime) // Seek video to timestamp
                }
                onImageClick(position) // Highlight corresponding item in FaultAdapter
            }

            // Long press to open zoomable image
            faultImage.setOnLongClickListener {
                showZoomableImageDialog(itemView.context, faultItem.imageUrl!!)
                true // Consume the event
            }
        }
    }

    private fun showZoomableImageDialog(context: Context, imageUrl: String) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_zoomable_image)

        // Find PhotoView
        val photoView = dialog.findViewById<PhotoView>(R.id.photoView)

        // Load image with Glide
        Glide.with(context)
            .load(imageUrl)
            .apply(RequestOptions().centerInside()) // Ensures the image is visible
            .into(photoView)

        // Ensure dialog is full screen
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        dialog.show()
    }


    fun convertFaultTimestamp(faultTimestamp: String): String {
        return try {
            val inputFormat = SimpleDateFormat("mm:ss.SSS", Locale.getDefault())
            val outputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

            val date = inputFormat.parse(faultTimestamp)
            outputFormat.format(date ?: Date(0)) // Default to "00:00:00" if parsing fails
        } catch (e: Exception) {
            "00:00:00" // Return a default value in case of an error
        }
    }

    fun highlightImage(position: Int) {
        val previousSelected = selectedPosition
        selectedPosition = position

        notifyItemChanged(previousSelected)
        notifyItemChanged(selectedPosition)
    }
}
