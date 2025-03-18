import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ards.R

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
            if (selectedPosition == position) {
                faultImage.background = ContextCompat.getDrawable(itemView.context, R.drawable.selected_image_border)
            } else {
                faultImage.background = null
            }

            faultImage.setOnClickListener {
                val previousSelected = selectedPosition
                selectedPosition = bindingAdapterPosition

                // Update UI only for changed items
                notifyItemChanged(previousSelected)
                notifyItemChanged(selectedPosition)

                faultItem.faultTimestamp?.let { faultTimestamp ->
                    onTimestampClick(faultTimestamp) // Seek video to timestamp
                }
                onImageClick(position) // Highlight corresponding item in FaultAdapter
            }
        }
    }
    fun highlightImage(position: Int) {
        val previousSelected = selectedPosition
        selectedPosition = position

        notifyItemChanged(previousSelected)
        notifyItemChanged(selectedPosition)
    }
}
