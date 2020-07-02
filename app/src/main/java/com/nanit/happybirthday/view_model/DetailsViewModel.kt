package com.nanit.happybirthday.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nanit.happybirthday.model.Repository
import javax.inject.Inject
import java.util.Calendar

class DetailsViewModel @Inject constructor(val repo: Repository) : ViewModel() {

    companion object {
        private const val START_BIRTH_YEAR = 2000
    }

    val pickerStartDate: Long = Calendar.getInstance().apply {
        set(START_BIRTH_YEAR, 1, 1)
    }.timeInMillis

    val pickerEndDate: Long = Calendar.getInstance().timeInMillis

    val name: LiveData<String>
        get() = mutableName
    private val mutableName = MutableLiveData<String>()

    val birthDateMillis: LiveData<Long>
        get() = mutableBirthDateMillis
    private val mutableBirthDateMillis = MutableLiveData<Long>()





}