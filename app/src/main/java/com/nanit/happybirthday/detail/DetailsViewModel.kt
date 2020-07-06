package com.nanit.happybirthday.detail

import android.net.Uri
import androidx.lifecycle.*
import com.nanit.happybirthday.model.Baby
import com.nanit.happybirthday.model.Repository
import com.nanit.happybirthday.util.LiveEvent
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

    val photoUri: LiveData<Uri?>

    val missingDataError: LiveData<LiveEvent<Any>>
        get() = missingDataErrorEvent
    private val missingDataErrorEvent = MutableLiveData<LiveEvent<Any>>()

    val showBirthdayScreen: LiveData<LiveEvent<Any>>
        get() = showBirthdayScreenEvent
    private val showBirthdayScreenEvent = MutableLiveData<LiveEvent<Any>>()

    init {
        viewModelScope.launch {
            repo.getBabyData()?.let {
                mutableName.postValue(it.name)
                mutableBirthDate.postValue(it.birthday)
            }
        }
        photoUri = repo.getBabyPhotoPath()
    }

    fun updateBabyData(name: String?, dobMillis: Long?, photoUri: Uri?) {
        if (name.isNullOrEmpty() || dobMillis == null) {
            missingDataErrorEvent.postValue(LiveEvent(Any()))
            return
        }

        viewModelScope.launch {
            repo.saveBabyData(
                Baby(name, Date(dobMillis), photoUri?.toString() ?: "")
            )
        }

        showBirthdayScreenEvent.postValue(LiveEvent(Any()))
    }

    fun updatePhoto(uri: String) {
        viewModelScope.launch {
            repo.updateBabyPhoto(uri)
        }
    }

}