package com.ards.ui.home.graph

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.ards.R
import com.ards.remote.apimodel.ChartResponse
import com.ards.ui.dashboard.GraphFragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class GraphCardAdapter(
    private val context: Context,
    private val items: List<ChartResponse.DataResponse.Faults>
) : RecyclerView.Adapter<GraphCardAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tvTitle)
        val number: TextView = itemView.findViewById(R.id.tvNumber)
        val chart: LineChart = itemView.findViewById(R.id.lineChart)
        val linear: LinearLayout = itemView.findViewById(R.id.linear)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.graph_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        if (position==0){
            holder.title.text = "Defects per Station"
            holder.number.text = "${item.FaultCount}+"
        }
        if (position==1){
            holder.title.text = "Repair Cost by Severity"
            holder.number.text = "${item.FaultCount}+"
        }
        if (position==2){
            holder.title.text = "Defects Over Time"
            holder.number.text = "${item.FaultCount}+"
        }


        // Setup Chart Data
        val entries = listOf(10f, 20f, 15f, 30f, 25f, 35f).mapIndexed { index, value ->
            Entry(
                index.toFloat(),
                value
            )
        }
        val dataSet = LineDataSet(entries, "").apply {
            color = Color.parseColor("#6A1B9A")  // Purple color
            setDrawValues(false)
            setDrawCircles(false)
            mode = LineDataSet.Mode.CUBIC_BEZIER
        }

        holder.chart.apply {
            data = LineData(dataSet)
            setTouchEnabled(false)
            setDrawGridBackground(false)
            description.isEnabled = false
            legend.isEnabled = false
            axisLeft.isEnabled = false
            axisRight.isEnabled = false
            xAxis.isEnabled = false
            invalidate()
        }
        holder.linear.setOnClickListener {
            ChartDialog(context, position).showChartDialog()
           /* Navigation.findNavController(holder.linear)
                .navigate(R.id.navigation_graph)*/
        }

    }

    override fun getItemCount() = items.size
}