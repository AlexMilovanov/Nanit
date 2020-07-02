package com.nanit.happybirthday

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.nanit.happybirthday.databinding.ActivityDetailsBinding
import com.nanit.happybirthday.util.DateValidatorPointBackward
import com.nanit.happybirthday.util.afterTextChanged
import com.nanit.happybirthday.view_model.DetailsViewModel
import kotlinx.android.synthetic.main.activity_details.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DetailsActivity : AppCompatActivity() {

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
    }

    private fun initUi() {

        initDatePicker()

        with(binding) {
            btnNext.setOnClickListener {

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

        val constraints = constraintsBuilder.setOpenAt(preselectedDate).build()

        val picker = datePickerBuilder.apply {
            setCalendarConstraints(constraints)
            setSelection(preselectedDate)
        }.build().also {
            it.addOnPositiveButtonClickListener(onDateSelected)
        }

        with (picker) {
            show(supportFragmentManager, this.toString())
            supportFragmentManager.executePendingTransactions()
            dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }
    }

    private fun updateDateOfBirth(dateMillis: Long) {
        selectedBirthdayMillis = dateMillis
        etBirthday.setText(dateFormat.format(Date(dateMillis)))
    }
}
