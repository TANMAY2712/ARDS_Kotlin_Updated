package com.ards.ui.home.graph

import com.ards.R
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.Window
import android.widget.Button
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate

import com.github.mikephil.charting.data.*


class ChartDialog(private val context: Context, private val position: Int) {
    lateinit var barChart:BarChart
    lateinit var lineChart:LineChart
    fun showChartDialog() {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.chart_dialog)



         barChart = dialog.findViewById<BarChart>(R.id.pieChart)
         lineChart = dialog.findViewById<LineChart>(R.id.linecart)
        val btnClose = dialog.findViewById<Button>(R.id.btnClose)

        when (position) {
            0 -> setupBarChart(barChart) // Defects per Station
            1 -> setupBoxPlot(barChart)  // Repair Cost by Severity
            2 -> setupLineChart(lineChart) // Defects Over Time
        }
        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun setupBarChart(chart: BarChart) {
        // Defining station names
        val stationNames = listOf("New Delhi", "Mumbai Central", "Chennai Central", "Howrah")

        // Creating bar entries (X = index, Y = defects count)
        val entries = listOf(
            BarEntry(0f, 25f),  // New Delhi
            BarEntry(1f, 20f),  // Mumbai Central
            BarEntry(2f, 18f),  // Chennai Central
            BarEntry(3f, 15f)   // Howrah
        )

        val dataSet = BarDataSet(entries, "Defects per Station")

        // Setting different colors for each bar
        val barColors = listOf(Color.BLUE, Color.RED, Color.GREEN, Color.MAGENTA)
        dataSet.colors = barColors

        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 12f

        val barData = BarData(dataSet)
        chart.data = barData

        // Custom X-axis labels
        val xAxis = chart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(stationNames)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f // Ensures labels are spaced correctly
        xAxis.setDrawGridLines(false)

        chart.axisLeft.setDrawGridLines(false)
        chart.axisRight.isEnabled = false // Hide right axis
        chart.description.isEnabled = false
        chart.setFitBars(true) // Adjusts bars properly
        chart.animateY(1000)
        chart.invalidate()
    }



    // Dummy Data - BoxPlot (Severity Level vs Repair Cost)
    private fun setupBoxPlot(chart: BarChart) {
        // Severity labels
        val severityLevels = listOf("High", "Medium", "Low")

        // Bar entries (X = index, Y = repair cost)
        val entries = listOf(
            BarEntry(0f, 30000f), // High Severity
            BarEntry(1f, 25000f), // Medium Severity
            BarEntry(2f, 20000f)  // Low Severity
        )

        val dataSet = BarDataSet(entries, "Repair Cost by Severity")
        dataSet.colors = listOf(Color.RED, Color.GRAY, Color.GREEN)
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 12f

        val barData = BarData(dataSet)
        chart.data = barData

        // Custom X-axis labels for Severity
        val xAxis = chart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(severityLevels)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f // Ensures labels align correctly
        xAxis.setDrawGridLines(false)

        chart.axisLeft.setDrawGridLines(false)
        chart.axisRight.isEnabled = false // Hide right axis
        chart.description.isEnabled = false
        chart.setFitBars(true) // Adjusts bars properly
        chart.animateY(1000)
        chart.invalidate()
    }


    // Dummy Data - Line Chart (Defect Reports Over Time)
    private fun setupLineChart(chart: LineChart) {
        barChart.visibility=View.GONE
        lineChart.visibility=View.VISIBLE
        val entries = listOf(
            Entry(1f, 10f),
            Entry(2f, 8f),
            Entry(3f, 12f),
            Entry(4f, 7f),
            Entry(5f, 15f),
            Entry(6f, 18f)
        )

        val dataSet = LineDataSet(entries, "Defects Over Time")
        dataSet.color = Color.MAGENTA
        dataSet.valueTextColor = Color.BLACK
        dataSet.setCircleColor(Color.BLUE)

        val lineData = LineData(dataSet)
        chart.data = lineData
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.description.isEnabled = false
        chart.animateX(1000)
        chart.invalidate()
    }

    // Dummy Data - Stacked Bar Chart (Defect Source Breakdown)
    private fun setupStackedBarChart(chart: BarChart) {
        // Defining defect source categories
        val defectCategories = listOf("Environmental", "Manufacturing Fault", "Wear and Tear")

        // Creating stacked bar entries (X = index, Y = array of severity levels)
        val entries = listOf(
            BarEntry(0f, floatArrayOf(5f, 10f, 7f)),  // Environmental
            BarEntry(1f, floatArrayOf(8f, 12f, 10f)), // Manufacturing Fault
            BarEntry(2f, floatArrayOf(6f, 14f, 9f))   // Wear and Tear
        )

        // Creating the stacked bar dataset
        val dataSet = BarDataSet(entries, "Defect Sources by Severity")
        dataSet.colors = listOf(Color.RED, Color.YELLOW, Color.GREEN) // Colors for High, Medium, Low Severity
        dataSet.stackLabels = arrayOf("High", "Medium", "Low") // Labels for severity levels
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 12f

        val barData = BarData(dataSet)
        chart.data = barData

        // Custom X-axis labels
        val xAxis = chart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(defectCategories)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f // Ensures labels are spaced correctly
        xAxis.setDrawGridLines(false)

        // Chart settings
        chart.axisLeft.setDrawGridLines(false)
        chart.axisRight.isEnabled = false // Hide right axis
        chart.description.isEnabled = false
        chart.legend.isEnabled = true // Show legend for stack labels
        chart.setFitBars(true) // Adjusts bars properly
        chart.animateY(1000)
        chart.invalidate()
    }
}
