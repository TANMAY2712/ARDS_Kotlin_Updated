package com.ards.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.ards.R
import com.ards.databinding.FragmentHomeBinding
import com.ards.ui.history.adapter.RecentAdapter
import com.ards.ui.home.graph.ChartDialog
import com.ards.ui.home.graph.GraphCardAdapter
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
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

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var recentAdapter: RecentAdapter
    private lateinit var graphCardAdapter: GraphCardAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        /*val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
*/
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.logoImage.setOnClickListener {
            Navigation.findNavController(binding.logoImage)
                .navigate(R.id.action_homeFragment_to_notificationFragment)
            }
        /*binding.recentTrainsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val trainList = listOf(
            Recent("Sabarmati Express", "12578", "DELHI", "11:00", "BHOPAL", "23:35", 2),
            Recent("Ernakulam Express", "12911", "DELHI", "17:50", "ERNAKULAM", "22:15", 1),
            Recent("Rajdhani Express", "12578", "DELHI", "17:00", "BHUBANESHWAR", "21:35", 2),
            Recent("Kranti Express", "12911", "DELHI", "17:50", "KAHIPUR", "22:15", 1),
            Recent("Avadh Express", "12578", "DELHI", "11:00", "BAREILLY", "23:35", 1)
        )

        recentAdapter = RecentAdapter(trainList)
        binding.recentTrainsRecyclerView.adapter = recentAdapter


        val graphList = listOf(
            GraphData("Faults this Month", 54, listOf(10f, 20f, 15f, 30f, 25f, 35f)),
            GraphData("Corrections this Month", 45, listOf(5f, 15f, 10f, 20f, 18f, 22f)),
            GraphData("Total Scans this Month", 72, listOf(8f, 25f, 20f, 30f, 28f, 38f))
        )

        val adapter = GraphCardAdapter(graphList)
        binding.graphRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.graphRecyclerView.adapter = adapter*/

        homeViewModel.isLoading.observe(requireActivity()) { isLoading ->
            binding.homeProgress.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
       // getGraphChart("","")
        getRecentRecord("",0,91273)
        setupPieChart(binding.pieChart)
        setupBoxPlot(binding.barChart)
        setupLineChart(binding.lineChart)
        setUpStackedBarChart(binding.defectSourceBreakdown)
        setupBarChart(binding.troubleshooting)

        binding.cardViewOne.setOnClickListener {
            ChartDialog(requireContext(), 0,"Defects per Station").showChartDialog()
        }
        binding.cardViewTwo.setOnClickListener {
            ChartDialog(requireContext(), 1,"Repair Cost by Severity").showChartDialog()
        }
        binding.cardViewThree.setOnClickListener {
            ChartDialog(requireContext(), 2,"Defects Over Time").showChartDialog()
        }
        binding.cardViewFour.setOnClickListener {
            ChartDialog(requireContext(), 3,"Defect Source Breakdown").showChartDialog()
        }
        binding.cardViewFive.setOnClickListener {
            ChartDialog(requireContext(), 4,"Troubleshooting Period").showChartDialog()
        }

        return root
    }
    private fun setupPieChart(chart: PieChart) {
        val defectsCount = listOf(25f, 20f, 18f, 15f)

        val entries = defectsCount.map { PieEntry(it) }

        val dataSet = PieDataSet(entries, "").apply {
            colors = listOf(Color.CYAN, Color.rgb(235, 125, 0), Color.LTGRAY, Color.rgb(235, 125, 235))
            valueTextColor = Color.TRANSPARENT // Hide values
            valueTextSize = 0f
        }

        chart.apply {
            data = PieData(dataSet)
            description.isEnabled = false // Remove description
            legend.isEnabled = false // Remove legend
            setDrawEntryLabels(false) // Remove labels on slices
            isDrawHoleEnabled = true // Show hole in center
            setHoleColor(Color.WHITE) // Set center color
            setDrawCenterText(false) // Hide center text
            setTransparentCircleAlpha(0) // Remove transparency effect
            animateY(1000, Easing.EaseInOutQuad)
            invalidate()
        }
    }






    // Dummy Data - BoxPlot (Severity Level vs Repair Cost)
    private fun setupBoxPlot(chart: BarChart) {
        val entries = listOf(
            BarEntry(0f, 30000f), // High Severity
            BarEntry(1f, 25000f), // Medium Severity
            BarEntry(2f, 20000f)  // Low Severity
        )

        val dataSet = BarDataSet(entries, "").apply {
            colors = listOf(Color.RED, Color.GRAY, Color.GREEN)
            valueTextColor = Color.TRANSPARENT // Hide values
            valueTextSize = 0f
        }

        chart.apply {
            data = BarData(dataSet)
            description.isEnabled = false // Remove description
            legend.isEnabled = false // Hide legend

            // Hide X-axis labels and grid lines
            xAxis.apply {
                setDrawLabels(false)
                setDrawGridLines(false)
                setDrawAxisLine(false)
            }

            // Hide Y-axis labels and grid lines
            axisLeft.apply {
                setDrawLabels(false)
                setDrawGridLines(false)
                setDrawAxisLine(false)
            }
            axisRight.isEnabled = false // Hide right axis

            setFitBars(true) // Adjust bar spacing
            animateY(1000)
            invalidate()
        }
    }



    // Dummy Data - Line Chart (Defect Reports Over Time)
    private fun setupLineChart(chart: LineChart) {
        val entries = listOf(
            Entry(0f, 10f), // Mar
            Entry(1f, 8f),  // Apr
            Entry(2f, 12f), // May
            Entry(3f, 7f),  // June
            Entry(4f, 15f), // Jul
            Entry(5f, 18f)  // Aug
        )

        val dataSet = LineDataSet(entries, "").apply {
            color = Color.BLUE
            valueTextColor = Color.TRANSPARENT // Hide values
            valueTextSize = 0f
            setCircleColor(Color.BLUE)
            circleRadius = 1f
            lineWidth = 1f
            setDrawValues(false) // Ensure no values are drawn
        }

        chart.apply {
            data = LineData(dataSet)
            description.isEnabled = false // Remove description
            legend.isEnabled = false // Hide legend

            // Hide X-axis labels and grid lines
            xAxis.apply {
                setDrawLabels(false)
                setDrawGridLines(false)
                setDrawAxisLine(false)
            }

            // Hide Y-axis labels and grid lines
            axisLeft.apply {
                setDrawLabels(false)
                setDrawGridLines(false)
                setDrawAxisLine(false)
            }
            axisRight.isEnabled = false // Hide right Y-axis

            animateX(1000)
            invalidate()
        }
    }
    private fun setUpStackedBarChart(chart: BarChart){
        val defectCategories = listOf("Environmental", "Manufacturing Fault", "Wear and Tear")

// Creating stacked bar entries (X = index, Y = array of severity levels)
        val entries = listOf(
            BarEntry(0f, floatArrayOf(5f, 10f, 7f)),  // Environmental
            BarEntry(1f, floatArrayOf(8f, 12f, 10f)), // Manufacturing Fault
            BarEntry(2f, floatArrayOf(6f, 14f, 9f))   // Wear and Tear
        )

// Creating the stacked bar dataset
        val dataSet = BarDataSet(entries, "")
        dataSet.colors = listOf(Color.RED, Color.YELLOW, Color.GREEN) // Colors for High, Medium, Low Severity
        dataSet.stackLabels = arrayOf("", "", "") // Remove labels
        dataSet.valueTextColor = Color.TRANSPARENT // Hide values
        dataSet.valueTextSize = 0f

        val barData = BarData(dataSet)
        chart.data = barData

// Hide X-axis labels
        val xAxis = chart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(listOf("", "", ""))
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.setDrawGridLines(false)
        xAxis.setDrawLabels(false) // Hide X-axis labels

// Hide Y-axis labels and grid lines
        chart.axisLeft.setDrawGridLines(false)
        chart.axisLeft.setDrawLabels(false) // Hide Y-axis labels
        chart.axisRight.isEnabled = false // Hide right Y-axis

// Hide chart description and legend
        chart.description.isEnabled = false
        chart.legend.isEnabled = false

        chart.setFitBars(true)
        chart.animateY(1000)
        chart.invalidate()

    }

    private fun setupBarChart(chart: BarChart) {
        // Station names and their troubleshooting periods
        val troubleshootingPeriods = listOf(680f, 300f, 250f, 270f, 150f)

        // Creating bar entries
        val entries = troubleshootingPeriods.indices.map { index ->
            BarEntry(index.toFloat(), troubleshootingPeriods[index])
        }

        // Creating dataset without labels
        val dataSet = BarDataSet(entries, "")
        dataSet.colors = listOf(Color.rgb(255, 120, 40), Color.rgb(100, 149, 237)) // Colors
        dataSet.setDrawValues(false) // Remove value labels on bars

        val barData = BarData(dataSet)
        chart.data = barData

        // Hide X-axis labels
        val xAxis = chart.xAxis
        xAxis.setDrawLabels(false)
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)

        // Hide Y-axis labels
        chart.axisLeft.setDrawLabels(false)
        chart.axisLeft.setDrawGridLines(false)
        chart.axisLeft.setDrawAxisLine(false)

        chart.axisRight.isEnabled = false // Hide right Y-axis

        // Hide legend and description
        chart.legend.isEnabled = false
        chart.description.isEnabled = false

        // Chart settings
        chart.setFitBars(true) // Adjust bar width
        chart.animateY(1000)
        chart.invalidate()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getGraphChart(startDate: String, endDate: String) {
        homeViewModel.getChartData(startDate, endDate)
            .observe(viewLifecycleOwner) { result ->

                result.onSuccess { chartData ->
                    // Setting up RecyclerView
                    binding.graphRecyclerView.layoutManager = GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
                    graphCardAdapter = GraphCardAdapter(requireContext(), chartData.Data.faults)
                    binding.graphRecyclerView.adapter = graphCardAdapter
                }

                result.onFailure { error ->
                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun getRecentRecord(trainNo: String, pageNo: Int, userId: Int) {
        homeViewModel.getRecentRecord(trainNo, pageNo, userId)
            .observe(viewLifecycleOwner) { result ->

                result.onSuccess { notifications ->
                    // Setting up RecyclerView
                    binding.recentTrainsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
                    recentAdapter = RecentAdapter(requireContext(), notifications.Data.faults, object : RecentAdapter.Callback {
                        override fun onItemClicked(
                            notificationId: Int
                        ) {
                            val bundle = Bundle()
                            bundle.putInt("notification_id_key", notificationId)
                            Navigation.findNavController(binding.recentTrainsRecyclerView)
                                .navigate(R.id.uploadFragment_to_predictFragment, bundle)
                        }
                    })
                    binding.recentTrainsRecyclerView.adapter = recentAdapter
                    }

                result.onFailure { error ->
                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}