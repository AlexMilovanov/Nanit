package com.nanit.happybirthday.view_model

import androidx.lifecycle.*
import com.nanit.happybirthday.model.Baby
import com.nanit.happybirthday.model.Repository
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

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

    val birthDate: LiveData<Date>
        get() = mutableBirthDate
    private val mutableBirthDate = MutableLiveData<Date>()

    init {
        viewModelScope.launch {
            repo.getBabyData()?.let {
                mutableName.postValue(it.name)
                mutableBirthDate.postValue(it.birthday)
            }
        }
    }

    fun updateBabyData(name: String, dobMillis: Long) {
        viewModelScope.launch {
            repo.saveBabyData(Baby(name, Date(dobMillis), null))
        }
    }

}