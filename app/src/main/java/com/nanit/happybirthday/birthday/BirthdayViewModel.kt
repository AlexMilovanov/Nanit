package com.nanit.happybirthday.birthday

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nanit.happybirthday.model.Repository
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random
import kotlin.random.Random.Default.nextInt

class BirthdayViewModel @Inject constructor(val repo: Repository) : ViewModel() {

    val name: LiveData<String>
        get() = mutableName
    private val mutableName = MutableLiveData<String>()

    val photoUri: LiveData<Uri?>
        get() = mutablePhotoUri
    private val mutablePhotoUri = MutableLiveData<Uri?>()

    private var style: BirthdayStyle? = null

    init {
        viewModelScope.launch {
            repo.getBabyData()?.let {
                mutableName.postValue(it.name)
                mutablePhotoUri.postValue(
                    if(it.photoPath.isEmpty()) { null } else { Uri.parse(it.photoPath) }
                )
            }
        }
    }

    fun getBirthdayStyle(): BirthdayStyle {
        return style ?: BirthdayStyle.values().run {
            this[nextInt(size)]
        }.also {
            style = it
        }
    }

}