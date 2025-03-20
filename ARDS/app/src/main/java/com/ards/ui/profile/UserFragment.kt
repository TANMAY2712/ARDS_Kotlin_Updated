package com.ards.ui.profile

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ards.R
import com.ards.databinding.FragmentUserBinding
import com.ards.remote.apimodel.MasterDataResponse
import com.ards.sharedpreference.PreferenceHelper
import com.ards.ui.login.LoginActivity
import com.ards.utils.ArdsConstant
import com.ards.utils.DateUtils
import com.ards.utils.PermissionManagerUtils
import com.bumptech.glide.Glide
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Objects

class UserFragment : Fragment(), PermissionManagerUtils.PermissionCallback {
    private var selectZone: String = ""
    private var selectDivision: String = ""
    private var selectZoneId: Int = 0
    private var selectDivisionId: Int = 0
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
    private val permissionLauncher =
        PermissionManagerUtils().requestPermissionFromFragment(this, this)
    private val fileLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val selectedImageURI = it.data?.data
                val path: String = getPathFromURI(selectedImageURI!!)!!
                if (path != null) {
                    profileImage = File(path)
                    userViewModel.uploadProfileImage(profileImage, ArdsConstant.MEDIA_TYPE_FILE)
                        .observe(viewLifecycleOwner) { result ->

                            result.onSuccess { user ->
                                PreferenceHelper.getInstance(requireContext())
                                    .setUserImage(user.Data)
                                ArdsConstant.showShortToast(
                                    user.SuccessMessage.toString(),
                                    requireContext()
                                )
                            }

                            result.onFailure { error ->
                                Toast.makeText(
                                    requireContext(),
                                    "Error: ${error.message}",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                }
            }
        }
    private val binding get() = _binding!!
    //private val notificationViewModel: UserF by viewModels()


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
        binding.dob.setText(
            DateUtils.getAppDateFromApiDate(
                PreferenceHelper.getInstance(
                    requireContext()
                ).getDOB
            )
        )
        /*binding.tvUnion.setText(PreferenceHelper.getInstance(requireContext()).getParentId.toString())
        binding.tvUnion.setText(PreferenceHelper.getInstance(requireContext()).getParentUnionName)*/
        binding.tvBranch.setText(PreferenceHelper.getInstance(requireContext()).getUserBranchName)
        //binding.tvZone.(PreferenceHelper.getInstance(requireContext()).getUserZoneName)
        //binding.tvDivision.setText(PreferenceHelper.getInstance(requireContext()).getUserDivisionName)
        binding.sector.setText("Railways")
        Glide.with(binding.imageView12)
            .load(PreferenceHelper.getInstance(requireContext()).getUserImage).circleCrop()
            .placeholder(R.drawable.user).into(
                binding.imageView12
            )
        binding.name.setText(PreferenceHelper.getInstance(requireContext()).getFullName)
        binding.officeaddress.setText(PreferenceHelper.getInstance(requireContext()).getUserAddress)
        binding.fathername.setText(PreferenceHelper.getInstance(requireContext()).getFathername)

        binding.imageView13.setOnClickListener {
            //binding.imageView13.applyWUSImageViewAnimation()
            requestCameraAndStoragePermission()
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
                PreferenceHelper.getInstance(requireContext())
                    .setUserDivisionName(selecteddivisionName!!)
                PreferenceHelper.getInstance(requireContext()).setUserDivisionID(selecteddivision)
            }
            if (selectedbranchName != null) {
                PreferenceHelper.getInstance(requireContext())
                    .setUserBranchName(selectedbranchName!!)
                PreferenceHelper.getInstance(requireContext()).setUserBranchID(selectedidbranch)
            }
            updateall(
                binding.name.text.toString(),
                binding.officeaddress.text.toString(),
                selectZoneId,
                selectDivisionId,
                binding.dob.text.toString(),
                PreferenceHelper.getInstance(requireContext()).getUserName,
                "91",
                PreferenceHelper.getInstance(requireContext()).getAuthToken
            )
        }

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        getUnionmaster("zone", PreferenceHelper.getInstance(requireContext()).getParentId, "")

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

    private fun updateall(
        name: String,
        address: String,
        zoneId: Int,
        DivisionId: Int,
        birthDate: String,
        Username: String,
        CountryCode: String,
        AuthToken: String
    ) {
        userViewModel.updateUserProfile(
            name,
            address,
            zoneId,
            DivisionId,
            birthDate,
            Username,
            CountryCode,
            AuthToken
        )
            .observe(viewLifecycleOwner) { result ->

                result.onSuccess { user ->
                    // Setting up RecyclerView
                    PreferenceHelper.getInstance(requireContext()).setFULLName(user.Data.FullName)
                    PreferenceHelper.getInstance(requireContext()).setDOB(user.Data.DOB)
                    //PreferenceHelper.getInstance(requireContext()).setdesignation(user.Data.DesignationName)
                    PreferenceHelper.getInstance(requireContext()).setPhone(user.Data.MobileNumber)
                    //PreferenceHelper.getInstance(requireContext()).setParentId(getView().getPreferences().getParentId)
                    //PreferenceHelper.getInstance(requireContext()).setFathername(user.Data.F)
                    PreferenceHelper.getInstance(requireContext()).setUserAge(user.Data.Age)
                    PreferenceHelper.getInstance(requireContext())
                        .setUserAddress(user.Data.OfficeAddress)
                    PreferenceHelper.getInstance(requireContext())
                        .setUserImage(user.Data.ProfileImage)
                    /*getView().getPreferences()
                        .setParentUnionName(getView().getPreferences().getParentUnionName)
                    getView().setdataall(t)*/
                    ArdsConstant.showShortToast(user.SuccessMessage.toString(), requireContext())
                }

                result.onFailure { error ->
                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT)
                        .show()
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
        for (permission in permissions) {
            when (permission.key) {
                Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                }

                Manifest.permission.READ_EXTERNAL_STORAGE -> {
                    selectImage()
                }

                Manifest.permission.CAMERA -> {
                }

                Manifest.permission.READ_MEDIA_IMAGES -> {
                    selectImage()
                }
            }
        }
    }

    private fun selectImage() {
        val options =
            arrayOf<CharSequence>(
                getString(R.string.take_photo),
                getString(R.string.choose_from_gallery),
                getString(R.string.cancel)
            )
        val builder =
            AlertDialog.Builder(requireContext())
        builder.setItems(
            options
        ) { dialog: DialogInterface, item: Int ->
            if (options[item] == getString(R.string.take_photo)) {
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                profileImage = null
                try {
                    profileImage = createImageFile()
                } catch (ex: IOException) {
                    Log.d(TAG, "Exception while creating file: $ex")
                }
                if (profileImage != null) {
                    Log.d(TAG, "Photofile not null")
                    val photoURI = FileProvider.getUriForFile(
                        requireContext(),
                        "com.bb.indianmajdoorunion.fileprovider",
                        profileImage!!
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(
                        takePictureIntent,
                        REQUEST_CAMERA
                    )
                }
            } else if (options[item] == getString(R.string.choose_from_gallery)) {
                val photoPickerIntent = Intent(Intent.ACTION_PICK)
                photoPickerIntent.type = "image/*"
                val chooser = Intent.createChooser(photoPickerIntent, null)
                fileLauncher.launch(chooser)
            } else if (options[item] == getString(R.string.cancel)) {
                dialog.dismiss()
            }
        }
        val cameradialog = builder.create()
        cameradialog.show()
    }

    @Throws(IOException::class)
    private fun createImageFile(): File? {
        // Create an image file name
        val timeStamp =
            SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )
        val mCurrentPhotoPath = image.absolutePath
        Log.d(TAG, "Path: $mCurrentPhotoPath")
        return image
    }

    private fun getPathFromURI(contentUri: Uri): String? {
        var res: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor =
            requireActivity().contentResolver.query(contentUri, proj, null, null, null)
        if (Objects.requireNonNull(cursor)!!.moveToFirst()) {
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            res = cursor.getString(column_index)
        }
        cursor!!.close()
        return res
    }

    private fun getUnionmaster(
        type: String,
        selectedparentid: Int,
        errMsg: String
    ) {
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

                    getUnionmaster(
                        "division",
                        PreferenceHelper.getInstance(requireContext()).getParentId,
                        ""
                    )

                    //binding.rvTrainLibrary.adapter = libraryAdapter
                }

                result.onFailure { error ->
                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    fun showunionlist(listUnion: List<MasterDataResponse.DataResponse>, type: String) {
        // Extract only the names for display
        val nameList = listUnion.map { it.MasterDataName }

        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, nameList)
        binding.tvUnion.adapter = adapter

        binding.tvUnion.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
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

    private fun requestCameraAndStoragePermission() {
        PermissionManagerUtils.message = getString(R.string.uploadProfilePhotoRequest)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_MEDIA_IMAGES
                )
            )
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            )
        }
    }

    fun showDivisionlist(listUnion: List<MasterDataResponse.DataResponse>, type: String) {
        // Extract only the names for display
        val nameList = listUnion.map { it.MasterDataName }

        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, nameList)
        binding.tvZone.adapter = adapter

        binding.tvZone.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                userViewModel.uploadProfileImage(profileImage, ArdsConstant.MEDIA_TYPE_FILE)
                    .observe(viewLifecycleOwner) { result ->

                        result.onSuccess { user ->
                            PreferenceHelper.getInstance(requireContext())
                                .setUserImage(user.Data)
                            ArdsConstant.showShortToast(
                                user.SuccessMessage.toString(),
                                requireContext()
                            )
                        }

                        result.onFailure { error ->
                            Toast.makeText(
                                requireContext(),
                                "Error: ${error.message}",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
            }
        }
    }

}