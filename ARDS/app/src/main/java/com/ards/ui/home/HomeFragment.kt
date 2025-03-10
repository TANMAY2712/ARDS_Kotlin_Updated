package com.ards.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.ards.databinding.FragmentHomeBinding
import com.ards.ui.history.adapter.RecentAdapter
import com.ards.ui.home.graph.GraphCardAdapter

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
        getGraphChart("","")
        getRecentRecord("",0,91273)
        return root
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
                    binding.graphRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
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
                    recentAdapter = RecentAdapter(requireContext(), notifications.Data.faults)
                    binding.recentTrainsRecyclerView.adapter = recentAdapter
                }

                result.onFailure { error ->
                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}