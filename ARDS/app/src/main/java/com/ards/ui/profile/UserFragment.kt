package com.ards.ui.profile

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ards.R
import com.ards.databinding.FragmentUserBinding
import com.ards.remote.apimodel.MasterDataResponse
import com.ards.sharedpreference.PreferenceHelper
import com.ards.utils.ArdsConstant
import com.ards.utils.DateUtils
import com.ards.utils.PermissionManagerUtils
import java.io.File
import java.util.Calendar

class UserFragment : Fragment(), PermissionManagerUtils.PermissionCallback {
    private var selectZone: String=""
    private var selectDivision: String=""
    private var selectZoneId: Int=0
    private var selectDivisionId: Int=0
    private val TAG = "UserFragment"
    private var profileImage: File? = null
    private val REQUEST_CAMERA = 211
    private val userViewModel: UserViewModel by viewModels()
    private val fromDate = Calendar.getInstance()
    private var selectedidbranch = 0
    private var selectedbranchName: String? = null
    private var selecteddivision = 0
    private var selecteddivisionName: String? = null
    private var selectedzoneName: String? = null
    private var selectedzone = 0
    private var isBranchSet = false
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    //private val notificationViewModel: UserF by viewModels()

    private val permissionLauncher =
        PermissionManagerUtils().requestPermissionFromFragment(this, this)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        /*val notificationViewModel =
            ViewModelProvider(this).get(NotificationListViewModel::class.java)
*/
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        val root: View = binding.root

        userViewModel.isLoading.observe(requireActivity()) { isLoading ->
            //binding.libraryProgress.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        binding.userid.setText(PreferenceHelper.getInstance(requireContext()).getUserName)
        binding.dob.setText(DateUtils.getAppDateFromApiDate(PreferenceHelper.getInstance(requireContext()).getDOB))
        /*binding.tvUnion.setText(PreferenceHelper.getInstance(requireContext()).getParentId.toString())
        binding.tvUnion.setText(PreferenceHelper.getInstance(requireContext()).getParentUnionName)*/
        binding.tvBranch.setText(PreferenceHelper.getInstance(requireContext()).getUserBranchName)
        //binding.tvZone.(PreferenceHelper.getInstance(requireContext()).getUserZoneName)
        //binding.tvDivision.setText(PreferenceHelper.getInstance(requireContext()).getUserDivisionName)
        binding.sector.setText("Railways")
        binding.name.setText(PreferenceHelper.getInstance(requireContext()).getFullName)
        binding.officeaddress.setText(PreferenceHelper.getInstance(requireContext()).getUserAddress)
        binding.fathername.setText(PreferenceHelper.getInstance(requireContext()).getFathername)

        binding.update.setOnClickListener {
            //binding.update.applyWUSTextViewAnimation()
            if (selectedzoneName != null) {
                PreferenceHelper.getInstance(requireContext()).setUserZoneName(selectedzoneName!!)
                PreferenceHelper.getInstance(requireContext()).setUserZoneID(selectedzone)
            }
            if (selecteddivisionName != null) {
                PreferenceHelper.getInstance(requireContext()).setUserDivisionName(selecteddivisionName!!)
                PreferenceHelper.getInstance(requireContext()).setUserDivisionID(selecteddivision)
            }
            if (selectedbranchName != null) {
                PreferenceHelper.getInstance(requireContext()).setUserBranchName(selectedbranchName!!)
                PreferenceHelper.getInstance(requireContext()).setUserBranchID(selectedidbranch)
            }
            //presenter!!.updateall(requireContext())
        }

        binding.dob.setOnClickListener {
            //binding.dob.applyWUSViewAnimation()
            calender()
        }

        binding.update.setOnClickListener {
            if (selectedzoneName != null) {
                PreferenceHelper.getInstance(requireContext()).setUserZoneName(selectedzoneName!!)
                PreferenceHelper.getInstance(requireContext()).setUserZoneID(selectedzone)
            }
            if (selecteddivisionName != null) {
                PreferenceHelper.getInstance(requireContext()).setUserDivisionName(selecteddivisionName!!)
                PreferenceHelper.getInstance(requireContext()).setUserDivisionID(selecteddivision)
            }
            if (selectedbranchName != null) {
                PreferenceHelper.getInstance(requireContext()).setUserBranchName(selectedbranchName!!)
                PreferenceHelper.getInstance(requireContext()).setUserBranchID(selectedidbranch)
            }
            updateall(
                binding.name.text.toString(),
                binding.officeaddress.text.toString(),
                selectZoneId,
                selectDivisionId,
                binding.dob.toString(),
                PreferenceHelper.getInstance(requireContext()).getUserName,
                "91",
                PreferenceHelper.getInstance(requireContext()).getAuthToken)
        }

        //binding.tvZone.setOnClickListener {
            getUnionmaster("zone", PreferenceHelper.getInstance(requireContext()).getParentId, "")
        //}

        //binding.tvDivision.setOnClickListener {
        //    getUnionmaster("zone", PreferenceHelper.getInstance(requireContext()).getParentId, "")
        //}

        return root
    }

    private fun updateall(
        name: String,
        address: String,
        zoneId: Int,
        DivisionId: Int,
        dob: String,
        Username: String,
        CountryCode:String,
        AuthToken:String
    ) {
        userViewModel.updateUserProfile(
            name,
            address,
            zoneId,
            DivisionId,
            dob,
            Username,
            CountryCode,
            AuthToken
        )
            .observe(viewLifecycleOwner) { result ->

                result.onSuccess { union ->
                    // Setting up RecyclerView
                    ArdsConstant.showShortToast(union.SuccessMessage.toString(), requireContext())
                }

                result.onFailure { error ->
                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun calender() {
        val dialog = DatePickerDialog(
            requireContext(),
            R.style.pickerTheme,
            DatePickerDialog.OnDateSetListener { view: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                fromDate.set(Calendar.YEAR, year)
                fromDate.set(Calendar.MONTH, month)
                fromDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                binding.dob.setText(DateUtils.getAppDateFromDate(fromDate.getTime()))
                binding.dob.setTextColor(Color.BLACK)
            },
            fromDate.get(Calendar.YEAR),
            fromDate.get(Calendar.MONTH),
            fromDate.get(Calendar.DAY_OF_MONTH)
        )
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR, -18)
        dialog.datePicker.maxDate = calendar.time.time
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onPermissionCallback(permissions: Map<String, Boolean>) {

    }

    private fun getUnionmaster(type: String,
                               selectedparentid: Int,
                               errMsg: String) {
        userViewModel.getUnionmaster(type, selectedparentid, errMsg)
            .observe(viewLifecycleOwner) { result ->

                result.onSuccess { union ->
                    // Setting up RecyclerView
                    //binding.rvTrainLibrary.layoutManager = GridLayoutManager(requireContext(), 1)
                    if (type == "zone") {
                        showunionlist(union.Data, type)
                    } else if (type == "division") {
                        showDivisionlist(union.Data, type)
                    }

                    getUnionmaster("division", PreferenceHelper.getInstance(requireContext()).getParentId, "")

                    //binding.rvTrainLibrary.adapter = libraryAdapter
                }

                result.onFailure { error ->
                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun showunionlist(listUnion: List<MasterDataResponse.DataResponse>, type: String) {
        // Extract only the names for display
        val nameList = listUnion.map { it.MasterDataName }

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, nameList)
        binding.tvUnion.adapter = adapter

        binding.tvUnion.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = listUnion[position] // Get the full object
                if (position != 0) {
                    if (type == "zone") {
                        selectZone = selectedItem.MasterDataName
                        selectZoneId = selectedItem.MasterDataId
                    } else if (type == "division") {
                        selectDivision = selectedItem.MasterDataName
                        selectDivisionId = selectedItem.MasterDataId
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }

    fun showDivisionlist(listUnion: List<MasterDataResponse.DataResponse>, type: String) {
        // Extract only the names for display
        val nameList = listUnion.map { it.MasterDataName }

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, nameList)
        binding.tvZone.adapter = adapter

        binding.tvZone.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = listUnion[position] // Get the full object
                if (position != 0) {
                    if (type == "zone") {
                        selectZone = selectedItem.MasterDataName
                        selectZoneId = selectedItem.MasterDataId
                    } else if (type == "division") {
                        selectDivision = selectedItem.MasterDataName
                        selectDivisionId = selectedItem.MasterDataId
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }


}