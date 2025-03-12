import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ards.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class FaultAdapter(
    private val faultList: List<Fault>,
    private val onTimestampClick: (String) -> Unit // Callback for timestamp click
) : RecyclerView.Adapter<FaultAdapter.FaultViewHolder>() {

    private val selectedStatus = mutableMapOf<Int, String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_fault_list, parent, false)
        return FaultViewHolder(view)
    }

    override fun onBindViewHolder(holder: FaultViewHolder, position: Int) {
        val fault = faultList[position]
        holder.bind(fault, selectedStatus[position])
    }

    override fun getItemCount(): Int = faultList.size

    inner class FaultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val faultInfoText: TextView = itemView.findViewById(R.id.tvFaultPart)
        private val tvTimeStamp: TextView = itemView.findViewById(R.id.tvTimeStamp)
        private val btnCorrect: ImageView = itemView.findViewById(R.id.btnCorrect)
        private val btnWrong: ImageView = itemView.findViewById(R.id.btnWrong)

        fun bind(fault: Fault, status: String?) {
            faultInfoText.text = fault.faultInfo

            val dateString = fault.uploadedTime
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("Asia/Kolkata")

            val dateWithoutTimezone = dateString.substring(0, 19)
            val date: Date = sdf.parse(dateWithoutTimezone)!!
            val userSdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val formattedTime = userSdf.format(date)

            val faultTimestamp = fault.faultTimestamp!!
            val faultTime = convertFaultTimestamp(faultTimestamp)

            tvTimeStamp.text = faultTime
            tvTimeStamp.setOnClickListener { onTimestampClick(faultTime) }

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
