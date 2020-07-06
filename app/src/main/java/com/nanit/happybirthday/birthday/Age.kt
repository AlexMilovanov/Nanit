package com.nanit.happybirthday.birthday

sealed class Age(val num: Int, val showHalf: Boolean) {
    data class Months(val months: Int, val showHalfMonth: Boolean = false) : Age(months, showHalfMonth)
    data class Years(val years: Int, val showHalfYear: Boolean = false) : Age(years, showHalfYear)
}