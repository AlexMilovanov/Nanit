package com.nanit.happybirthday.view_model

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
        get() = mutablePhotoUri
    private val mutablePhotoUri = MutableLiveData<Uri?>()

    val missingDataError: LiveData<LiveEvent<Any>>
        get() = missingDataErrorEvent
    private val missingDataErrorEvent = MutableLiveData<LiveEvent<Any>>()

    init {
        viewModelScope.launch {
            repo.getBabyData()?.let {
                mutableName.postValue(it.name)
                mutableBirthDate.postValue(it.birthday)
                mutablePhotoUri.postValue(
                    if(it.photoPath.isEmpty()) { null } else { Uri.parse(it.photoPath) }
                )
            }
        }
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
    }

}