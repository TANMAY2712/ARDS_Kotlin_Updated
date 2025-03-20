package com.ards.ui.home.graph

import com.ards.R
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.github.mikephil.charting.animation.Easing
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


class ChartDialog(private val context: Context, private val position: Int, private val title: String) {
    lateinit var barChart:BarChart
    lateinit var pieChart:PieChart
    lateinit var lineChart:LineChart
    fun showChartDialog() {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.chart_dialog)



         barChart = dialog.findViewById<BarChart>(R.id.barChart)
        pieChart = dialog.findViewById<PieChart>(R.id.pieChart)
         lineChart = dialog.findViewById<LineChart>(R.id.linecart)
        val btnClose = dialog.findViewById<Button>(R.id.btnClose)
        val titl = dialog.findViewById<TextView>(R.id.title)

        titl.setText(title)


        when (position) {
            0 -> setupPieChart(pieChart) // Defects per Station
            1 -> setupBoxPlot(barChart)  // Repair Cost by Severity
            2 -> setupLineChart(lineChart) // Defects Over Time
            3 -> setupStackedBarChart(barChart)
            4 -> setupBarChart(barChart)
        }
        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun setupPieChart(chart: PieChart) {
        barChart.visibility=View.GONE
        lineChart.visibility=View.GONE
        pieChart.visibility=View.VISIBLE
        // Defining station names and defects count
        val stationNames = listOf("New Delhi", "Mumbai Central", "Chennai Central", "Howrah")
        val defectsCount = listOf(25f, 20f, 18f, 15f)

        // Creating pie entries
        val entries = stationNames.indices.map { index ->
            PieEntry(defectsCount[index], stationNames[index])
        }

        val dataSet = PieDataSet(entries, "Defects per Station")

        // Setting different colors for each slice
        val pieColors = listOf(Color.CYAN, Color.rgb(235,125,0), Color.LTGRAY, Color.rgb(235,125,235))
        dataSet.colors = pieColors

        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 12f

        val pieData = PieData(dataSet)
        chart.data = pieData

        // Customizing the chart appearance
        chart.description.isEnabled = false
        chart.isDrawHoleEnabled = true // Enables the center hole
        chart.setHoleColor(Color.WHITE)
        chart.setEntryLabelColor(Color.BLACK)
        chart.setEntryLabelTextSize(12f)
        chart.animateY(1000, Easing.EaseInOutQuad)

        chart.invalidate() // Refresh chart
    }




    // Dummy Data - BoxPlot (Severity Level vs Repair Cost)
    private fun setupBoxPlot(chart: BarChart) {
        barChart.visibility=View.VISIBLE
        lineChart.visibility=View.GONE
        pieChart.visibility=View.GONE
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
        barChart.visibility = View.GONE
        lineChart.visibility = View.VISIBLE
        pieChart.visibility = View.GONE

        // Sample dates (X-axis) and defects count (Y-axis)
        val dateLabels = listOf("Mar", "Apr", "May", "June", "Jul", "Aug")

        val entries = listOf(
            Entry(0f, 10f), // Mar
            Entry(1f, 8f),  // Apr
            Entry(2f, 12f), // May
            Entry(3f, 7f),  // June
            Entry(4f, 15f), // Jul
            Entry(5f, 18f)  // Aug
        )

        val dataSet = LineDataSet(entries, "Defects Over Time")
        dataSet.color = Color.BLUE
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 10f // **Increased value text size**
        dataSet.setCircleColor(Color.BLUE)
        dataSet.circleRadius = 6f
        dataSet.lineWidth = 2.5f

        val lineData = LineData(dataSet)
        chart.data = lineData

        // Custom X-axis labels (Dates)
        val xAxis = chart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(dateLabels)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f  // Ensures labels are spaced correctly
        xAxis.setDrawGridLines(false)
        xAxis.textSize = 14f // **Increased X-axis label size**

        // Y-Axis customization
        val yAxisLeft = chart.axisLeft
        yAxisLeft.textSize = 14f // **Increased Y-axis label size**

        // Other chart settings
        chart.axisRight.isEnabled = false
        chart.axisLeft.setDrawGridLines(false)
        chart.description.isEnabled = false
        chart.animateX(1000)
        chart.invalidate()
    }



    // Dummy Data - Stacked Bar Chart (Defect Source Breakdown)
    private fun setupStackedBarChart(chart: BarChart) {
        barChart.visibility=View.VISIBLE
        lineChart.visibility=View.GONE
        pieChart.visibility=View.GONE
        // Defining defect source categories
        val defectCategories = listOf("Environmental", "Manufacturing\nFault", "Wear\nand Tear") // Add line breaks for better readability

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
        xAxis.labelRotationAngle = -90f // Rotate labels vertically to prevent overlap

// Chart settings
        chart.axisLeft.setDrawGridLines(false)
        chart.axisRight.isEnabled = false // Hide right axis
        chart.description.isEnabled = false
        chart.legend.isEnabled = true // Show legend for stack labels
        chart.setFitBars(true) // Adjusts bars properly
        chart.animateY(1000)
        chart.invalidate()

    }

    private fun setupBarChart(chart: BarChart) {
        barChart.visibility = View.VISIBLE
        lineChart.visibility = View.GONE
        pieChart.visibility = View.GONE

        // Station names and their troubleshooting periods
        val stationNames = listOf("Delhi", "Mumbai", "Kanpur", "Howrah")
        val troubleshootingPeriods = listOf(680f, 300f, 250f, 270f)

        // Modify station names to be in two lines
        val formattedStationNames = stationNames.map { it.replace(" ", "\n") } // Add line break

        // Creating bar entries
        val entries = stationNames.indices.map { index ->
            BarEntry(index.toFloat(), troubleshootingPeriods[index])
        }

        // Highlight New Delhi in a different color
        val dataSet = BarDataSet(entries, "Troubleshooting Period")
        val colors = stationNames.map { if (it == "New Delhi") Color.rgb(255, 120, 40) else Color.rgb(100, 149, 237) }
        dataSet.colors = colors
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 12f

        val barData = BarData(dataSet)
        chart.data = barData

        // Customizing X-axis labels (Station Names with newlines)
        val xAxis = chart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(formattedStationNames) // Use formatted names
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textSize = 12f
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = 0f // Keep text straight

        // Y-axis customization
        chart.axisLeft.isEnabled = false  // Hide left Y-axis
        chart.axisRight.isEnabled = false // Hide right Y-axis

        // Chart settings
        chart.description.isEnabled = false
        chart.setFitBars(true) // Adjust bar width
        chart.animateY(1000)
        chart.invalidate()
    }




}
