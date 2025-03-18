package com.ards.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ards.R
import com.ards.databinding.FragmentHistoryBinding
import com.ards.ui.history.adapter.HistoryAdapter
import com.ards.ui.history.adapter.RecentAdapter
import com.ards.ui.history.model.Recent
import com.ards.ui.home.HomeViewModel
import com.ards.ui.home.graph.GraphCardAdapter

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var historyAdapter: HistoryAdapter
    private val historyViewModel: HistoryViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.rvTrainHistory.layoutManager = LinearLayoutManager(requireContext())
        gethistory("",0,91273)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun gethistory(trainNo: String, pageNo: Int, userId: Int) {
        historyViewModel.getHistory(trainNo, pageNo, userId)
            .observe(viewLifecycleOwner) { result ->

                result.onSuccess { notifications ->
                    // Setting up RecyclerView
                    binding.rvTrainHistory.layoutManager = GridLayoutManager(requireContext(), 1)
                    historyAdapter = HistoryAdapter(requireContext(), notifications.Data.faults, object : HistoryAdapter.Callback {
                        override fun onItemClicked(
                            notificationId: Int
                        ) {
                            val bundle = Bundle()
                            bundle.putInt("category_id_key", notificationId)
                            Navigation.findNavController(binding.rvTrainHistory)
                                .navigate(R.id.libVideoFragment_to_processedFragment, bundle)
                        }
                    })
                    binding.rvTrainHistory.adapter = historyAdapter
                }

                result.onFailure { error ->
                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}