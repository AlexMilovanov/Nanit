package com.nanit.happybirthday.util

import android.os.Parcel
import android.os.Parcelable
import com.google.android.material.datepicker.CalendarConstraints

class DateValidatorPointBackward (private val point: Long) : CalendarConstraints.DateValidator {

    companion object CREATOR : Parcelable.Creator<DateValidatorPointBackward> {
        override fun createFromParcel(parcel: Parcel): DateValidatorPointBackward {
            return DateValidatorPointBackward(parcel.readLong())
        }

        override fun newArray(size: Int): Array<DateValidatorPointBackward?> {
            return arrayOfNulls(size)
        }

        fun validTo (point: Long): DateValidatorPointBackward {
            return DateValidatorPointBackward(point)
        }
    }

    override fun isValid(date: Long): Boolean {
        return date <= point
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(point)
    }

    override fun describeContents(): Int {
        return 0
    }
}