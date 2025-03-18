package com.ards.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.ards.R
import com.ards.databinding.FragmentHistoryBinding
import com.ards.databinding.FragmentProfileBinding
import com.ards.sharedpreference.PreferenceHelper
import com.ards.ui.history.adapter.HistoryAdapter
import com.ards.ui.login.LoginActivity
import com.ards.ui.profile.adapter.ProfileMenuAdapter
import com.ards.ui.profile.model.MenuItem

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val menuItems = listOf(
            MenuItem("Profile", R.drawable.user),
            MenuItem("Notification", R.drawable.bell),
           // MenuItem("Appearance", R.drawable.bell),
           // MenuItem("Security", R.drawable.bell),
            MenuItem("Help & Support", R.drawable.help),
            MenuItem("Language", R.drawable.language),
            MenuItem("Contact Us", R.drawable.user)
        )

        val adapter = ProfileMenuAdapter(requireContext(),menuItems, object : ProfileMenuAdapter.Callback {
            override fun onItemClicked(
                menuItemName: String
            ) {
                if(menuItemName.equals("Profile")){
                    Navigation.findNavController(binding.recyclerMenu)
                        .navigate(R.id.profileFragment_to_userFragment)
                }else if(menuItemName.equals("Notification")){
                    Navigation.findNavController(binding.recyclerMenu)
                        .navigate(R.id.action_profileFragment_to_notificationFragment)
                }
                /*val bundle = Bundle()
                bundle.putInt("category_id_key", notificationId)
                Navigation.findNavController(binding.rvTrainHistory)
                    .navigate(R.id.libVideoFragment_to_processedFragment, bundle)*/
            }
        })
        binding.recyclerMenu.layoutManager = LinearLayoutManager(context)
        binding.recyclerMenu.adapter = adapter

        binding.btnLogout.setOnClickListener {
            PreferenceHelper.getInstance(requireContext()).clear()
            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            PreferenceHelper.getInstance(requireContext()).putBoolean("isLoggedIn", false)
            // Close the parent activity
            activity?.finish()

        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}