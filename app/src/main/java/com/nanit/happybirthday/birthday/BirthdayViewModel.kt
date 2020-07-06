package com.nanit.happybirthday.birthday

import android.net.Uri
import androidx.lifecycle.*
import com.nanit.happybirthday.model.Repository
import com.nanit.happybirthday.util.calculateAgeYearMonths
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random.Default.nextInt

class BirthdayViewModel @Inject constructor(val repo: Repository) : ViewModel() {

    val name: LiveData<String>
        get() = mutableName
    private val mutableName = MutableLiveData<String>()

    val age: LiveData<Age>
        get() = mutableAge
    private val mutableAge = MutableLiveData<Age>()

    val photoUri: LiveData<Uri?>

    private var style: BirthdayStyle? = null

    init {
        viewModelScope.launch {
            repo.getBabyData()?.let {
                mutableName.postValue(it.name)
                mutableAge.postValue(calculateAge(it.birthday.time))
            }
        }
        photoUri = repo.getBabyPhotoPath()
    }

    fun getBirthdayStyle(): BirthdayStyle {
        return style ?: BirthdayStyle.values().run {
            this[nextInt(size)]
        }.also {
            style = it
        }
    }

    fun calculateAge(birthdayMillis: Long): Age {
        val age = calculateAgeYearMonths(birthdayMillis)
        return when {
            age.first > 0 -> {
                Age.Years(age.first, showHalfYear = age.first == 1 && age.second >= 6)
            }
            else -> {
                Age.Months(age.second, showHalfMonth = age.second == 1 && age.third >= 2)
            }
        }
    }

}