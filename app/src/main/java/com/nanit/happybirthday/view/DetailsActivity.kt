package com.nanit.happybirthday.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.nanit.happybirthday.NanitApp
import com.nanit.happybirthday.R
import com.nanit.happybirthday.util.DateValidatorPointBackward
import com.nanit.happybirthday.view_model.DetailsViewModel
import com.nanit.happybirthday.databinding.ActivityDetailsBinding
import com.nanit.happybirthday.ext.afterTextChanged
import com.nanit.happybirthday.ext.loadImage
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DetailsActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_GALLERY = 111
        private const val REQUEST_CAMERA = 222
        private const val REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 333
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val detailsVm: DetailsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[DetailsViewModel::class.java]
    }

    private lateinit var binding: ActivityDetailsBinding

    private var selectedBirthdayMillis: Long? = null

    private val dateFormat by lazy {
        SimpleDateFormat(
            getString(R.string.details_field_birthday_format), Locale.getDefault()
        )
    }

    private val constraintsBuilder = CalendarConstraints.Builder()

    private val datePickerBuilder = MaterialDatePicker.Builder.datePicker()


    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as NanitApp).appComponent.inject(this)
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
            REQUEST_GALLERY -> updatePhoto(data?.data)
        }
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
        }
    }

    private fun initUi() {

        initDatePicker()

        with(binding) {

            ivPhoto.setOnClickListener {
                chooseFromGallery()
            }

            btnNext.setOnClickListener {
                detailsVm.updateBabyData(
                    binding.etName.text.toString(),
                    selectedBirthdayMillis!!
                )
            }

            etName.afterTextChanged {
                binding.tilName.error = null
            }

            etBirthday.setOnClickListener {
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
            }.build().also { picker ->
                picker.addOnPositiveButtonClickListener(onDateSelected)
                picker.show(supportFragmentManager, this.toString())
                picker.dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                supportFragmentManager.executePendingTransactions()
            }
        }
    }

    private fun updateDateOfBirth(dateMillis: Long) {
        selectedBirthdayMillis = dateMillis
        binding.etBirthday.setText(dateFormat.format(Date(dateMillis)))
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



    private fun updatePhoto(uri: Uri?) {
        if (uri == null) return
        binding.etPhoto.setText(uri.path.toString())
        binding.ivPhoto.loadImage(uri, circleCrop = true)
    }

}
