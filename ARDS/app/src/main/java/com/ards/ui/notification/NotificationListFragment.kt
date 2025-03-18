package com.ards.ui.notification

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.ards.MainActivity
import com.ards.databinding.FragmentNotificationBinding
import com.ards.ui.library.LibraryViewModel
import com.ards.ui.notification.adapter.NotificationAdapter
import com.ards.ui.otp.OtpViewModel

class NotificationListFragment : Fragment() {

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!
    private val notificationViewModel: NotificationListViewModel by viewModels()
    private lateinit var notificationAdapter: NotificationAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        /*val notificationViewModel =
            ViewModelProvider(this).get(NotificationListViewModel::class.java)
*/
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        notificationViewModel.isLoading.observe(requireActivity()) { isLoading ->
            binding.notificationProgress.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        getAllNotification("",0,92597)
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getAllNotification(trainNo: String, pageNo: Int, userId: Int) {
        notificationViewModel.getAllNotification(trainNo, pageNo, userId)
            .observe(viewLifecycleOwner) { result ->

                result.onSuccess { notifications ->
                    // Setting up RecyclerView
                    binding.rvNotification.layoutManager = GridLayoutManager(requireContext(), 1)
                    notificationAdapter = NotificationAdapter(requireContext(), notifications.Data.faults)
                    binding.rvNotification.adapter = notificationAdapter
                }

                result.onFailure { error ->
                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

}