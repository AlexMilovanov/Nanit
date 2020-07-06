package com.nanit.happybirthday.detail

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.nanit.happybirthday.R
import com.nanit.happybirthday.databinding.ActivityDetailsBinding
import com.nanit.happybirthday.util.ext.afterTextChanged
import com.nanit.happybirthday.util.ext.loadImage
import com.nanit.happybirthday.util.*
import com.nanit.happybirthday.util.ext.myApplication
import com.nanit.happybirthday.birthday.BirthdayDialogFragment
import kotlinx.android.synthetic.main.activity_details.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DetailsActivity : AppCompatActivity(), BirthdayDialogFragment.OnChangePhotoListener {

    companion object {
        private const val REQUEST_GALLERY = 111
        private const val REQUEST_CAMERA = 222
        private const val REQUEST_WRITE_EXT_STORAGE_PERMISSION = 333
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val detailsVm: DetailsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[DetailsViewModel::class.java]
    }

    private lateinit var binding: ActivityDetailsBinding

    private val dateFormat by lazy {
        SimpleDateFormat(
            getString(R.string.details_field_birthday_format), Locale.getDefault()
        )
    }

    private val constraintsBuilder = CalendarConstraints.Builder()

    private val datePickerBuilder = MaterialDatePicker.Builder.datePicker()

    private var selectedBirthdayMillis: Long? = null

    private var photoUri: Uri? = null

    private var imagePickerDialog: AlertDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        this.myApplication.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        initUi()
        observeVm()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return
        when (requestCode) {
            REQUEST_GALLERY -> {
                updatePhoto(data?.data)
            }
            REQUEST_CAMERA -> updatePhoto(photoUri)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode == REQUEST_WRITE_EXT_STORAGE_PERMISSION
            && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCamera()
        }
    }

    override fun onAttachFragment(fragment: Fragment) {
        if (fragment is BirthdayDialogFragment) {
            fragment.setOnChangePhotoListener(this)
        }
    }

    override fun onChangePhotoClicked() {
        showPhotoChooser()
    }

    private fun observeVm() {
        with(detailsVm) {

            name.observe(this@DetailsActivity, Observer {
                binding.etName.setText(it)
            })

            birthDate.observe(this@DetailsActivity, Observer {
                selectedBirthdayMillis = it.time
                binding.etBirthday.setText(dateFormat.format(it))
            })

            photoUri.observe(this@DetailsActivity, Observer { uri ->
                updatePhoto(uri)
            })

            missingDataError.observe(this@DetailsActivity, Observer {
                showMandatoryFieldError()
            })

            showBirthdayScreen.observe(this@DetailsActivity, Observer {
                showBirthdayScreen()
            })
        }
    }

    private fun initUi() {

        initDatePicker()

        with(binding) {

            ivThumbnail.setOnClickListener {
                showPhotoChooser()
            }

            btnNext.setOnClickListener {
                detailsVm.updateBabyData(
                    binding.etName.text.toString(),
                    selectedBirthdayMillis,
                    photoUri
                )
            }

            etName.afterTextChanged {
                binding.tilName.error = null
            }

            etBirthday.setOnClickListener {
                tilBirthday.error = null
                showDatePicker(selectedBirthdayMillis ?: detailsVm.pickerEndDate) {
                    updateDateOfBirth(it)
                }
            }
        }
    }

    private fun initDatePicker() {
        constraintsBuilder.apply {
            setStart(detailsVm.pickerStartDate)
            setEnd(detailsVm.pickerEndDate)
            setValidator(DateValidatorPointBackward.validTo(detailsVm.pickerEndDate))
        }

        datePickerBuilder.apply {
            setTitleText(R.string.details_date_picker_title)
        }
    }

    private fun showDatePicker(preselectedDate: Long, onDateSelected: ((selection: Long) -> Unit)) {
        constraintsBuilder.setOpenAt(preselectedDate).build().also {
            datePickerBuilder.apply {
                setCalendarConstraints(it)
                setSelection(preselectedDate)
            }.build().apply {
                addOnPositiveButtonClickListener(onDateSelected)
                dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            }.also {
                it.show(supportFragmentManager, this.toString())
                supportFragmentManager.executePendingTransactions()
            }
        }
    }

    private fun updateDateOfBirth(dateMillis: Long) {
        selectedBirthdayMillis = dateMillis
        binding.etBirthday.setText(dateFormat.format(Date(dateMillis)))
    }

    private fun showPhotoChooser() {
        val builder = AlertDialog.Builder(this)
        LayoutInflater.from(this)
            .inflate(R.layout.dialog_select_photo_chooser, null).apply {
                findViewById<View>(R.id.tvChooseExistingPhoto)
                    .setOnClickListener {
                        imagePickerDialog?.dismiss()
                        chooseFromGallery()
                    }
                findViewById<View>(R.id.tvTakeNewPhoto)
                    .setOnClickListener {
                        imagePickerDialog?.dismiss()
                        if (appRequiresRuntimePermissions() &&
                            !isWriteExtStoragePermissionGranted(this@DetailsActivity)) {
                            requestWriteExtStoragePermission(this@DetailsActivity,
                                REQUEST_WRITE_EXT_STORAGE_PERMISSION
                            )
                        } else {
                            openCamera()
                        }
                    }
            }.also { builder.setView(it) }

        imagePickerDialog = builder.create().also { it.show() }
    }

    private fun chooseFromGallery() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(
            Intent.createChooser(
                intent,
                getString(R.string.details_select_from_gallery)
            ),
            REQUEST_GALLERY
        )
    }

    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            createImageFile(this)?.let {
                photoUri = it
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, it)
                startActivityForResult(takePictureIntent,
                    REQUEST_CAMERA
                )
            }
        }
    }

    private fun updatePhoto(uri: Uri?) {
        if (uri == null) return

        photoUri = uri

        val birthdayFragment = supportFragmentManager.findFragmentByTag(BirthdayDialogFragment.TAG)
        // If birthday screen is visible, just update the persisted photo path value
        if (birthdayFragment != null && birthdayFragment.isVisible) {
            detailsVm.updatePhoto(photoUri.toString())
        } else {
            binding.etPhoto.setText(uri.path.toString())
            binding.ivThumbnail.loadImage(uri, circleCrop = true, placeholderResId = R.drawable.img_placeholder_blue)
        }
    }

    private fun showMandatoryFieldError() {
        if (etName.text.isNullOrEmpty()) {
            tilName.error = getString(R.string.details_field_error_msg)
        }

        if (etBirthday.text.isNullOrEmpty()) {
            tilBirthday.error = getString(R.string.details_field_error_msg)
        }
    }

    private fun showBirthdayScreen() {
        BirthdayDialogFragment.newInstance()
            .show(supportFragmentManager, BirthdayDialogFragment.TAG)
    }
}
