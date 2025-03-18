import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ards.R
import com.ards.ui.processed.dialog.CommentDialog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FaultAdapter(
    private val faultList: List<Fault>,
    private val onTimestampClick: (String,Int) -> Unit // Callback for timestamp click
) : RecyclerView.Adapter<FaultAdapter.FaultViewHolder>() {
    private val selectedStatus = mutableMapOf<Int, String>()
    var selectedPosition: Int = RecyclerView.NO_POSITION // Track selected item
    private var selectedCorrectPosition: Int? = null
    private var selectedWrongPosition: Int? = null
    private var editingPosition: Int? = null // Track which item is being edited
    private val commentsMap = mutableMapOf<Int, String>() // Store comments



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_fault_list, parent, false)
        return FaultViewHolder(view)
    }

    override fun onBindViewHolder(holder: FaultViewHolder, position: Int) {
        val fault = faultList[position]
        holder.bind(fault, position,selectedStatus[position])
    }

    override fun getItemCount(): Int = faultList.size

    inner class FaultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val faultInfoText: TextView = itemView.findViewById(R.id.tvFaultPart)
        private val tvTimeStamp: TextView = itemView.findViewById(R.id.tvTimeStamp)
        private val btnCorrect: ImageView = itemView.findViewById(R.id.btnCorrect)
        private val btnWrong: ImageView = itemView.findViewById(R.id.btnWrong)
        private val linear: LinearLayout = itemView.findViewById(R.id.linear)

        private val btnComment: ImageView = itemView.findViewById(R.id.btnEdit)
        private val btnViewComment: ImageView = itemView.findViewById(R.id.btnView)
      //  private val btnSave: ImageView = itemView.findViewById(R.id.btnSave)

        fun bind(fault: Fault, position: Int, status: String?) {

            faultInfoText.text = fault.faultInfo

            val faultTimestamp = fault.faultTimestamp ?: "00:00:00"
            val faultTime = convertFaultTimestamp(faultTimestamp)
            tvTimeStamp.text = faultTime
            tvTimeStamp.setOnClickListener { onTimestampClick(faultTime,position) }

            // Highlight selected item
            linear.setBackgroundColor(
                if (selectedPosition == position)
                    ContextCompat.getColor(itemView.context, R.color.selected_background)
                else
                    ContextCompat.getColor(itemView.context, android.R.color.transparent)
            )

            tvTimeStamp.setOnClickListener {
                val previousSelected = selectedPosition
                selectedPosition = bindingAdapterPosition

                // Update UI efficiently
                notifyItemChanged(previousSelected)
                notifyItemChanged(selectedPosition)
                onTimestampClick(faultTime,position)
            }
            btnViewComment.visibility = if (commentsMap.containsKey(position)) View.VISIBLE else View.GONE
            btnComment.visibility = if (btnViewComment.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            btnComment.setOnClickListener {
                val context = itemView.context
                CommentDialog(context, commentsMap[position]) { newComment ->
                    commentsMap[position] = newComment
                    btnViewComment.visibility = View.VISIBLE
                    notifyItemChanged(position)
                }.show()
            }
            btnViewComment.setOnClickListener {
                val context = itemView.context
                CommentDialog(context, commentsMap[position]) { newComment ->
                    commentsMap[position] = newComment
                }.show()
            }

            // Update button states
            when (status) {
                "correct" -> {
                    btnCorrect.background = ContextCompat.getDrawable(itemView.context, R.drawable.ic_correct_green)
                    btnWrong.background = ContextCompat.getDrawable(itemView.context, R.drawable.ic_wrong)
                }
                "wrong" -> {
                    btnCorrect.background = ContextCompat.getDrawable(itemView.context, R.drawable.ic_correct)
                    btnWrong.background = ContextCompat.getDrawable(itemView.context, R.drawable.ic_cross_red)
                }
                else -> {
                    btnCorrect.background = ContextCompat.getDrawable(itemView.context, R.drawable.ic_correct)
                    btnWrong.background = ContextCompat.getDrawable(itemView.context, R.drawable.ic_wrong)
                }
            }

            btnCorrect.setOnClickListener {
                selectedStatus[adapterPosition] = "correct"
                notifyDataSetChanged()
            }

            btnWrong.setOnClickListener {
                selectedStatus[adapterPosition] = "wrong"
                notifyDataSetChanged()
            }
        }
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
}
